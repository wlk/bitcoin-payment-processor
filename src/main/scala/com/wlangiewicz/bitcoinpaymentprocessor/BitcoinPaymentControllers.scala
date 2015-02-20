package com.wlangiewicz.bitcoinpaymentprocessor

import com.wlangiewicz.bitcoinpaymentprocessor.controllers.WalletController

trait BitcoinPaymentControllers  {
  this: PaymentProcessorBase =>
  lazy val walletController = new WalletController with PaymentProcessorBase

}
