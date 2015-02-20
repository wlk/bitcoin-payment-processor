package com.wlangiewicz.bitcoinpaymentprocessor.actors

import java.io.{FileInputStream, File}
import java.net.InetAddress
import java.util.concurrent.TimeUnit

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.bitcoinj.core._
import org.bitcoinj.core.{Address => BitcoinAddress}
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.store.{WalletProtobufSerializer, SPVBlockStore, MemoryBlockStore}
import org.bitcoinj.wallet.{Protos, KeyChainGroup}
import scala.collection.JavaConversions._

class WalletActor extends Actor with ActorLogging {
  var params: NetworkParameters = _
  var chainStore: MemoryBlockStore = _
  var chain: BlockChain = _
  var wallet: Wallet = _
  var peers: PeerGroup = _
  var balance: Option[String] = _
  var walletFile: File = _

  private def createOrLoadWallet(): Wallet = {
    var wallet: Wallet = null
    if (walletFile.exists) {
      wallet = loadWallet()
    }
    else {
      wallet = Wallet.fromWatchingKey(params, DeterministicKey.deserializeB58(null, ConfigFactory.load().getString("watchingKey")))
      wallet.freshReceiveKey
      wallet.saveToFile(walletFile)
    }
    wallet.autosaveToFile(walletFile, 200, TimeUnit.MILLISECONDS, null)
    wallet
  }

  private def loadWallet(): Wallet = {
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

  override def preStart() = {
    params = MainNetParams.get
    //chainStore = new MemoryBlockStore(params)
    walletFile = new File("wallet.wallet")
    val chainStore: SPVBlockStore = new SPVBlockStore(params, new File("wallet.spvchain"))
    chain = new BlockChain(params, chainStore)

    wallet = createOrLoadWallet()

    peers = new PeerGroup(params, chain)
    peers.addPeerDiscovery(new DnsDiscovery(params))
    peers.setFastCatchupTimeSecs(1418515200) //Sun, 14 Dec 2014 00:00:00 GMT
    peers.setUseLocalhostPeerWhenPossible(true)
    peers.addAddress(new PeerAddress(InetAddress.getByName("192.168.56.101"), 8333)) //btcd running in my local network

    chain.addWallet(wallet)
    peers.addWallet(wallet)
    peers.setBloomFilterFalsePositiveRate(0.0)

    peers.startAsync
    peers.awaitRunning()
    peers.startBlockChainDownload(new DownloadListener)

    balance = Some(wallet.getWatchedBalance.toFriendlyString)
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
  }
}