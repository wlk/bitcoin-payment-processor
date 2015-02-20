package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor.{Props, ActorSystem}

trait PaymentProcessorBase {
  implicit val system = ActorSystem("payment-processor")
}
