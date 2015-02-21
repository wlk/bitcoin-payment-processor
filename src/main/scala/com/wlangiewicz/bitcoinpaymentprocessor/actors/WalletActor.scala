package com.wlangiewicz.bitcoinpaymentprocessor.actors

import java.io.{FileInputStream, File}
import java.net.InetAddress
import java.util.concurrent.TimeUnit

import akka.actor._
import com.typesafe.config.ConfigFactory
import com.wlangiewicz.bitcoinpaymentprocessor.NewPaymentRequest
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor.RegisterCallback
import org.bitcoinj.core._
import org.bitcoinj.core.{Address => BitcoinAddress}
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params._
import org.bitcoinj.store.{WalletProtobufSerializer, SPVBlockStore, MemoryBlockStore}
import org.bitcoinj.wallet.{Protos, KeyChainGroup}
import scala.collection.JavaConversions._

object WalletActor {

  case class RegisterCallback(paymentRequest: NewPaymentRequest, address: String)

}

class WalletActor extends Actor with ActorLogging {
  var params: NetworkParameters = _
  var chainStore: MemoryBlockStore = _
  var chain: BlockChain = _
  var wallet: Wallet = _
  var peers: PeerGroup = _
  var balance: Option[String] = _
  var walletFile: File = _

  override def preStart() = {
    Console.println("AAAAAAAAAAAAa preStart")
    def createOrLoadWallet(): Wallet = {
      var wallet: Wallet = null
      if (walletFile.exists) {
        wallet = loadWallet()
      }
      else {
        wallet = Wallet.fromWatchingKey(params, DeterministicKey.deserializeB58(ConfigFactory.load().getString("watchingKey"), params))
        wallet.freshReceiveKey
        wallet.saveToFile(walletFile)
      }
      wallet.autosaveToFile(walletFile, 200, TimeUnit.MILLISECONDS, null)
      wallet
    }

    def loadWallet(): Wallet = {
      var wallet: Wallet = null
      val walletStream: FileInputStream = new FileInputStream(walletFile)
      try {
        wallet = new Wallet(params)
        val proto: Protos.Wallet = WalletProtobufSerializer.parseToProto(walletStream)
        val serializer = new WalletProtobufSerializer
        wallet = serializer.readWallet(params, null, proto)
      } finally {
        walletStream.close()
      }
      wallet
    }

    params = TestNet3Params.get
    walletFile = new File("wallet.wallet")
    val chainStore: SPVBlockStore = new SPVBlockStore(params, new File("wallet.spvchain"))
    chain = new BlockChain(params, chainStore)

    wallet = createOrLoadWallet()

    peers = new PeerGroup(params, chain)
    peers.addPeerDiscovery(new DnsDiscovery(params))
    peers.setFastCatchupTimeSecs(1418515200) //Sun, 14 Dec 2014 00:00:00 GMT
    peers.setUseLocalhostPeerWhenPossible(true)
    //peers.addAddress(new PeerAddress(InetAddress.getByName("192.168.56.101"), 8333)) //btcd running in my local network

    chain.addWallet(wallet)
    peers.addWallet(wallet)
    peers.setBloomFilterFalsePositiveRate(0.0)

    peers.startAsync
    peers.startBlockChainDownload(new DownloadProgressTracker)

    balance = Some(wallet.getWatchedBalance.toFriendlyString)
  }

  def registerCallback(request: NewPaymentRequest, address: String) = {
    wallet.addEventListener(new AbstractWalletEventListener() {
      override def onCoinsReceived(w: Wallet, tx: Transaction, prevBalance: Coin, newBalance: Coin) {
        val value: Coin = tx.getValueSentToMe(w)
        System.out.println("Received tx for " + value.toFriendlyString + ": " + tx)
        System.out.println("Transaction will be forwarded after it confirms.")
      }
    }
    )
  }

  def receive = {
    case "walletInfo" =>
      sender ! balance.getOrElse(Coin.NEGATIVE_SATOSHI)
    case "newReceiveAddress" =>
      sender ! wallet.freshReceiveAddress().toString
    case "getHeight" =>
      sender ! wallet.getLastBlockSeenHeight.toString
    case "test" =>
      sender ! System.currentTimeMillis.toString
    case "getReceiveAddressCount" =>
      sender ! wallet.getKeychainSize.toString
    case "getAllAddresses" =>
      sender ! wallet.serializeKeychainToProtobuf().map(k => new BitcoinAddress(params, k.getPublicKey.toByteArray)).mkString("\n")
    case RegisterCallback(newPayment, address) =>
      registerCallback(newPayment, address)
      sender ! "ok"
  }
}