package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor._
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor

class SingleActorExtensionImpl extends Extension with PaymentProcessorBase {
  val walletActor = system.actorOf(Props[WalletActor], "wallet")
}

object SingleActorExtension extends ExtensionId[SingleActorExtensionImpl] with ExtensionIdProvider {
  override def lookup() = SingleActorExtension
  override def createExtension(system: ExtendedActorSystem) = new SingleActorExtensionImpl
  override def get(system: ActorSystem): SingleActorExtensionImpl = super.get(system)
}
