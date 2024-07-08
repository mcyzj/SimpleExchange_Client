package com.mcyzj.exchange.client.socket

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.util.*


class UDPClient(val host: String, val port: Int) {

    var socket: DatagramSocket
    var isclose = false
    var listenPoet: Int

    init {
        val rand = Random()
        try {
            listenPoet = rand.nextInt(20000) + 10000
            socket = DatagramSocket(listenPoet)
        } catch (_: Exception) {
            listenPoet = rand.nextInt(20000) + 10000
            socket = DatagramSocket(listenPoet)
        }
    }

    fun send(data: ByteArray) {
        if (isclose) {
            return
        }
        // 发送端
        try {
            // 构造数据报包，用来将长度为 length 的包发送到指定主机上的指定端口号。
            val packet = DatagramPacket(
                data, data.size,
                InetAddress.getByName(host), port
            )
            // 从此套接字发送数据报包
            socket.send(packet)
        } catch (e: SocketException) {
            e.printStackTrace()
            close()
        } catch (e: IOException) {
            e.printStackTrace()
            close()
        }
    }

    fun receive(size: Int = 1024): ByteArray? {
        if (isclose) {
            return null
        }
        // 接收端
        return try{
            //接收数据的buf数组并指定大小
            val buf = ByteArray(size)
            //创建接收数据包，存储在buf中
            val packet = DatagramPacket(buf, buf.size)
            //接收操作
            socket.receive(packet)
            val data = packet.data // 接收的数据
            data
        } catch (e: SocketException) {
            e.printStackTrace()
            close()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            close()
            null
        }
    }

    fun close() {
        println("连接关闭")
        socket.close()
        isclose = true
    }
}