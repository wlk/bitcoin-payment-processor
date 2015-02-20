package com.wlangiewicz.bitcoinpaymentprocessor

import com.wlangiewicz.bitcoinpaymentprocessor.controllers.WalletController

trait BitcoinPaymentControllers {
  val walletController = new WalletController

}
