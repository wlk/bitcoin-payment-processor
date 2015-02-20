package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Main extends App with PaymentProcessorBase {
  val service = system.actorOf(Props[PaymentProcessorActor], "service")

  IO(Http) ! Http.Bind(service, "0.0.0.0", port = 8080)
}