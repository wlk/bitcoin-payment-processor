package com.wlangiewicz.bitcoinpaymentprocessor.controllers

import akka.actor.{ActorSystem, ActorRef}
import akka.util.Timeout
import akka.pattern.ask
import com.wlangiewicz.bitcoinpaymentprocessor._
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor.RegisterCallback
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.language.postfixOps

class WalletController(system: ActorSystem, walletActor: ActorRef) {
  implicit val timeout = Timeout(15 seconds)

  def newPayment(request: NewPaymentRequest): NewPaymentResponse = {
    val result = Await.result(walletActor ? "newReceiveAddress", timeout.duration).asInstanceOf[String]
    walletActor ! RegisterCallback(request, result)
    NewPaymentResponse(result, request.amountBTC, request.message)
  }

  def walletInfo: String = {
    val result = Await.result(walletActor ? "walletInfo", timeout.duration).asInstanceOf[String]
    s"walletInfo: $result"
  }

  def newReceiveAddress: String = {
    val result = Await.result(walletActor ? "newReceiveAddress", timeout.duration).asInstanceOf[String]
    s"newReceiveAddress: $result"
  }

  def getHeight: String = {
    val result = Await.result(walletActor ? "getHeight", timeout.duration).asInstanceOf[String]
    s"getHeight: $result"
  }

  def test: String = {
    Await.result(walletActor ? "test", timeout.duration).asInstanceOf[String]
  }

  def getReceiveAddressCount: String = {
    Await.result(walletActor ? "getReceiveAddressCount", timeout.duration).asInstanceOf[String]
  }
}
