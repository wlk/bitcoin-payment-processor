package com.wlangiewicz.blockchainwriter.actors

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.bitcoinj.core._
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.net.discovery.DnsDiscovery
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.store.MemoryBlockStore

case class Balance(c: Coin)


class WalletActor extends Actor with ActorLogging {

  var params: NetworkParameters = _
  var chainStore: MemoryBlockStore = _
  var chain: BlockChain = _
  var wallet: Wallet = _
  var peers: PeerGroup = _
  var balance: Option[Coin] = _

  override def preStart() = {
    val conf = ConfigFactory.load()
    params = MainNetParams.get
    chainStore = new MemoryBlockStore(params)
    chain = new BlockChain(params, chainStore)

    wallet = Wallet.fromWatchingKey(params, DeterministicKey.deserializeB58(null, conf.getString("watchingKey")))

    for( i <- 1 to 10){
      wallet.freshReceiveAddress()
    }

    Console.println(wallet)

    peers  = new PeerGroup(params, chain)
    peers.addPeerDiscovery(new DnsDiscovery(params))
    peers.setUseLocalhostPeerWhenPossible(true)

    chain.addWallet(wallet)
    peers.addWallet(wallet)
    peers.setBloomFilterFalsePositiveRate(0.0)

    peers.startAsync
    peers.awaitRunning()
    peers.downloadBlockChain()

    balance = Some(wallet.getWatchedBalance)
  }

  def receive = {
    case Balance => sender ! balance.getOrElse(Coin.NEGATIVE_SATOSHI)
  }
}