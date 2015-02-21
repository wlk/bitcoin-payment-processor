package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._

import scala.concurrent.duration._
import scala.concurrent.{Future, Await, ExecutionContextExecutor}

import scala.language.postfixOps

class PaymentProcessorActor extends Actor with ProcessorRouting {
  this: PaymentProcessorBase =>
  override implicit lazy val system = ActorSystem("payment-processor")

  def actorRefFactory = context


  def receive = runRoute(route)
}

trait ProcessorRouting extends HttpService with JsonFormats with BitcoinPaymentControllers  {
  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher
  implicit lazy val system = ActorSystem("payment-processor")

  val walletActor = SingleActorExtension(system).walletActor


  implicit val timeout = Timeout(5 seconds)

  val route = {
    path("newPayment") {
      post {
        handleWith { newPaymentRequest: NewPaymentRequest =>
          Future[NewPaymentResponse] {
            walletController.newPayment(newPaymentRequest)
          }
        }
      }
    } ~
      get {
        path("walletInfo") {
          complete {
            Future[String] {
              val result = Await.result(walletActor ? "walletInfo", timeout.duration).asInstanceOf[String]
              s"walletInfo: $result"
            }
          }
        } ~
          path("newReceiveAddress") {
            complete {
              Future[String] {
                val result = Await.result(walletActor ? "newReceiveAddress", timeout.duration).asInstanceOf[String]
                s"newReceiveAddress: $result"
              }
            }
          } ~
          path("getHeight") {
            complete {
              Future[String] {
                val result = Await.result(walletActor ? "getHeight", timeout.duration).asInstanceOf[String]
                s"getHeight: $result"
              }
            }
          } ~
          path("test") {
            complete {
              Future[String] {
                Await.result(walletActor ? "test", timeout.duration).asInstanceOf[String]
              }
            }
          } ~
          path("getReceiveAddressCount") {
            complete {
              Future[String] {
                Await.result(walletActor ? "getReceiveAddressCount", timeout.duration).asInstanceOf[String]
              }
            }
          }
      }
  }
}
