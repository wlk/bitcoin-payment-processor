package com.wlangiewicz.bitcoinpaymentprocessor

case class NewPaymentRequest(amountBTC: BigDecimal, notificationUrl: String, message: String)

case class NewPaymentResponse(address: String, amountBTC: BigDecimal, message: String)

case class PaymentCompletedResponse(amountBTC: BigDecimal, message: String)