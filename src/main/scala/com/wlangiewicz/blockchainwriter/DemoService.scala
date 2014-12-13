package com.wlangiewicz.blockchainwriter

import scala.concurrent.ExecutionContextExecutor
import akka.actor._
import spray.routing.HttpService

class DemoServiceActor extends Actor with DemoService {
  def actorRefFactory = context
  def receive = runRoute(demoRoute)
}

trait DemoService extends HttpService {

  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher

  val demoRoute = {
    get {
        path("walletInfo") {
          complete ("INFO")
        }
      }
  }
}
