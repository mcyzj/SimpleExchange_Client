package com.mcyzj.exchange.client.socket

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.*


class TCPClient(val host: String, val port: Int) {

    var socket: Socket
    var isclose = false
    var inputStream: InputStream
    var outputStream: OutputStream

    init {
        socket = Socket(host, port)
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
    }

    fun send(data: ByteArray) {
        if (isclose) {
            return
        }
        // 发送端
        try {
            // 从此套接字发送数据报包
            outputStream.write(data)
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
            val data = ByteArray(size) // 接收的数据
            val bytesRead = inputStream.read(data)
            val finalData = ByteArray(bytesRead)
            data.inputStream().read(finalData)
            finalData
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

    fun receiveJsonData(size: Int = 1024): JsonObject? {
        val data = receive(size) ?: return null
        return try {
            val str = String(data, 0, data.size)
            println(str)
            val json = Gson()
            json.fromJson(str, JsonObject::class.java)
        } catch (_:Exception) {
            null
        }
    }

    fun close() {
        println("连接关闭")
        socket.close()
        inputStream.close()
        outputStream.close()
        isclose = true
    }
}