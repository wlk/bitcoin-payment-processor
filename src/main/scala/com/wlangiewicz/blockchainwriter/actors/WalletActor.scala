package com.wlangiewicz.blockchainwriter.actors

import java.net.InetAddress

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.bitcoinj.core._
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.store.MemoryBlockStore

class WalletActor extends Actor with ActorLogging {

  var params: NetworkParameters = _
  var chainStore: MemoryBlockStore = _
  var chain: BlockChain = _
  var wallet: Wallet = _
  var peers: PeerGroup = _
  var balance: Option[String] = _

  override def preStart() = {
    val conf = ConfigFactory.load()
    params = MainNetParams.get
    chainStore = new MemoryBlockStore(params)
    chain = new BlockChain(params, chainStore)

    wallet = Wallet.fromWatchingKey(params, DeterministicKey.deserializeB58(null, conf.getString("watchingKey")))

    log.error(wallet.toString)

    peers = new PeerGroup(params, chain)
    //peers.addPeerDiscovery(new DnsDiscovery(params))
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
  }
}