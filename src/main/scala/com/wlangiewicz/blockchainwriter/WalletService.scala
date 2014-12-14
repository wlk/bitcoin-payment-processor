package com.wlangiewicz.blockchainwriter

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import com.wlangiewicz.blockchainwriter.actors.WalletActor
import spray.routing.HttpService

import scala.concurrent.duration._
import scala.concurrent.{Future, Await, ExecutionContextExecutor}

class WalletServiceActor extends Actor with WalletService {
  def actorRefFactory = context
  def receive = runRoute(route)
}

trait WalletService extends HttpService {
  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher
  val actor = actorRefFactory.actorOf(Props[WalletActor])
  implicit val timeout = Timeout(5 minutes)

  val route = {
    get {
      path("walletInfo") {
        complete{
          Future[String]{
            val result = Await.result(actor ? "walletInfo", timeout.duration).asInstanceOf[String]
            s"walletInfo: $result"
          }
        }
      } ~
        path("newReceiveAddress") {
          complete{
            Future[String]{
              val result = Await.result(actor ? "newReceiveAddress", timeout.duration).asInstanceOf[String]
              s"newReceiveAddress: $result"
            }
          }
        } ~
        path("getHeight") {
          complete{
            Future[String]{
              val result = Await.result(actor ? "getHeight", timeout.duration).asInstanceOf[String]
              s"getHeight: $result"
            }
          }
        } ~
        path("test") {
          complete{
            Future[String]{
              Await.result(actor ? "test", timeout.duration).asInstanceOf[String]
            }
          }
        } ~
        path("getReceiveAddressCount") {
          complete{
            Future[String]{
              Await.result(actor ? "getReceiveAddressCount", timeout.duration).asInstanceOf[String]
            }
          }
        } ~
        path("getAllAddresses") {
          complete{
            Future[String]{
              Await.result(actor ? "getAllAddresses", timeout.duration).asInstanceOf[String]
            }
          }
        }
    }
  }
}
