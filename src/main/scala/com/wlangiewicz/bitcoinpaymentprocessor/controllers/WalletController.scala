package com.wlangiewicz.bitcoinpaymentprocessor.controllers

import akka.actor.Props
import com.wlangiewicz.bitcoinpaymentprocessor._
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor

trait WalletController {
  this: PaymentProcessorBase =>
  val walletActor = system.actorOf(Props[WalletActor], "wallet")

  def newPayment(request: NewPaymentRequest): NewPaymentResponse = ???

}
