package com.wlangiewicz.bitcoinpaymentprocessor

import spray.json.DefaultJsonProtocol

trait JsonFormats extends DefaultJsonProtocol {
  implicit val NewPaymentRequestFormat = jsonFormat3(NewPaymentRequest)
  implicit val NewPaymentResponseFormat = jsonFormat3(NewPaymentResponse)
  implicit val PaymentCompletedResponseFormat = jsonFormat2(PaymentCompletedResponse)

}
