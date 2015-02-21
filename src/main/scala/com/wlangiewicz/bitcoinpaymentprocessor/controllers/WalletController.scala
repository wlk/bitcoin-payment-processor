package com.wlangiewicz.bitcoinpaymentprocessor.controllers

import akka.actor.Props
import akka.util.Timeout
import akka.pattern.ask
import com.wlangiewicz.bitcoinpaymentprocessor._
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor.RegisterCallback
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.language.postfixOps

trait WalletController extends PaymentProcessorBase {
  implicit val timeout = Timeout(15 seconds)

  def newPayment(request: NewPaymentRequest): NewPaymentResponse = {
    val result = Await.result(walletActor ? "newReceiveAddress", timeout.duration).asInstanceOf[String]
    walletActor ! RegisterCallback(request, result)
    NewPaymentResponse(result, request.amountBTC, request.message)
  }

}
