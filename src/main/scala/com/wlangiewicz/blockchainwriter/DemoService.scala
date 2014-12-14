package com.wlangiewicz.blockchainwriter

import com.wlangiewicz.blockchainwriter.actors.WalletActor

import scala.concurrent.ExecutionContextExecutor
import akka.actor._
import spray.routing.HttpService

class DemoServiceActor extends Actor with DemoService {
  def actorRefFactory = context
  def receive = runRoute(demoRoute)
}

trait DemoService extends HttpService {

  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher

  val wallet = ActorSystem("blockchain-writer").actorOf(Props[WalletActor], "wallet")

  val demoRoute = {
    get {
      path("walletInfo") {
        wallet ! "abc"
        complete("DDD")
      }
    }
  }
}
