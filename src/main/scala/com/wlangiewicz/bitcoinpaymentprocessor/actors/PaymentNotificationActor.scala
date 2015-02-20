package com.wlangiewicz.bitcoinpaymentprocessor.actors

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.wlangiewicz.bitcoinpaymentprocessor.PaymentCompletedResponse

object PaymentNotificationActor {

  case class NotifyPaid(notificationUrl: String, payload: PaymentCompletedResponse)

}

class PaymentNotificationActor extends Actor {
  override def receive: Receive = ???
}
