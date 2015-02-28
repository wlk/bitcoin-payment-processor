package com.wlangiewicz.bitcoinpaymentprocessor

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import com.wlangiewicz.bitcoinpaymentprocessor.actors.WalletActor
import com.wlangiewicz.bitcoinpaymentprocessor.controllers.WalletController
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._

import scala.concurrent.duration._
import scala.concurrent.{Future, Await, ExecutionContextExecutor}

import scala.language.postfixOps

trait ProcessorService extends HttpService with JsonFormats {

  val walletController: WalletController

  val paymentProcessorRoute = {
    path("newPayment") {
      post {
        handleWith { newPaymentRequest: NewPaymentRequest =>
          walletController.newPayment(newPaymentRequest)
        }
      }
    } ~
      get {
        path("walletInfo") {
          complete {
            walletController.walletInfo
          }
        } ~
          path("newReceiveAddress") {
            complete {
              walletController.newReceiveAddress
            }
          } ~
          path("getHeight") {
            complete {
              walletController.getHeight
            }
          } ~
          path("test") {
            complete {
              walletController.test
            }
          } ~
          path("getReceiveAddressCount") {
            complete {
              walletController.getReceiveAddressCount
            }
          }
      }
  }
}
