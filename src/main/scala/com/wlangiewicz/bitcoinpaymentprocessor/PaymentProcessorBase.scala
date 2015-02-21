package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor.{Props, ActorSystem}
import akka.contrib.pattern.ClusterSingletonManager
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor

import scala.collection.script.End

trait PaymentProcessorBase {
  implicit val system = ActorSystem("payment-processor")
  val walletActor = system.actorOf(Props[WalletActor], "wallet")
}
