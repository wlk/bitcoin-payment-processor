package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor
import com.wlangiewicz.bitcoinpaymentprocessor.controllers.WalletController
import spray.can.Http
import spray.routing.RouteConcatenation

object Main extends App {
  implicit val system = ActorSystem("payment-processor")

  val service = system.actorOf(Props[ApiActor], "service")

  IO(Http) ! Http.Bind(service, "0.0.0.0", port = 8080)
}

class ApiActor extends Actor with RouteConcatenation with JsonFormats with ProcessorService {
  def actorRefFactory = context

  val walletActor = context.system.actorOf(Props[WalletActor], "wallet")

  val walletController: WalletController = new WalletController(context.system, walletActor)

  def receive = runRoute(paymentProcessorRoute)
}