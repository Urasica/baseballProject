package com.example.baseballapp

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatWebSocketListener(private val fragment: ChatingFragment) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "Connected")
        fragment.activity?.runOnUiThread {
            fragment.showMessage("Connected to server")
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Message received: $text")
        fragment.activity?.runOnUiThread {
            fragment.showMessage(text)
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        val message = bytes.hex()
        Log.d("WebSocket", "ByteString received: $message")
        fragment.activity?.runOnUiThread {
            fragment.showMessage(message)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Error: ${t.message}", t)
        fragment.activity?.runOnUiThread {
            fragment.showMessage("WebSocket Error: ${t.message}")
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Closed: $reason")
        fragment.activity?.runOnUiThread {
            fragment.showMessage("Disconnected: $reason")
        }
    }
}