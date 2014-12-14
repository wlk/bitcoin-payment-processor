package com.wlangiewicz.blockchainwriter

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import com.wlangiewicz.blockchainwriter.actors.WalletActor
import spray.routing.HttpService

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContextExecutor }

class WalletServiceActor extends Actor with WalletService {
  def actorRefFactory = context
  def receive = runRoute(route)
}

trait WalletService extends HttpService {
  implicit val timeout = Timeout(5 minutes)

  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher
  val actor = actorRefFactory.actorOf(Props[WalletActor])

  val route = {
    get {
      path("walletInfo") {
        val future = actor ? "walletInfo"
        val result = Await.result(future, timeout.duration).asInstanceOf[String]

        complete("balance: " + result)
      } ~
        path("newReceiveAddress") {
          val future = actor ? "newReceiveAddress"
          val result = Await.result(future, timeout.duration).asInstanceOf[String]

          complete("newAddress: " + result)
        } ~
        path("getHeight") {
          val future = actor ? "getHeight"
          val result = Await.result(future, timeout.duration).asInstanceOf[String]

          complete("getHeight: " + result)
        } ~
        path("test") {
          val future = actor ? "test"
          val result = Await.result(future, timeout.duration).asInstanceOf[String]

          complete(result)
        }
    }
  }
}
