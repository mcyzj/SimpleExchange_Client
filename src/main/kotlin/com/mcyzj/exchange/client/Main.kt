package com.mcyzj.exchange.client


/**
 * 主入口
 */
class Main {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println("启动SimpleExchange客户端")
            SimpleExchangeClient().start()
        }

    }

}
