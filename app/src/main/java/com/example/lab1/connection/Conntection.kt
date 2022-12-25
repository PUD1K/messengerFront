package com.example.lab1

import android.os.Handler
import android.os.Looper
import com.example.lab1.activity.LoginActivity
import kotlinx.coroutines.Runnable
import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

// обработчик запросов
class Connection {
    interface SendDataListenerInterface {
        fun onError(e: IOException);
        fun onResult(value: String);
    }
    abstract class SendDataListener : SendDataListenerInterface {
        override fun onError(e: IOException) {}

        override fun onResult(value: String) {}
    }


    val client = OkHttpClient()
    lateinit var context: LoginActivity
    var mainHandler: Handler = Handler(Looper.getMainLooper())

    // params: user, contacts
    fun getData(url: String, listener: SendDataListener){
        Thread(Runnable{
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("error" + e)
                }

                override fun onResponse(call: Call, response: Response) {
                    var res = response.body?.string()
                    listener.onResult(res.toString())
                }
            })
        }).start()
    }

    // params: login, registration, contact
    fun sendData(url: String, body: String, listener: SendDataListener){
        Thread(Runnable (){
                var reqBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)

                val request = Request.Builder()
                    .url(url)
                    .post(reqBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        listener.onError(e)
                    }
                    override fun onResponse(call: Call, response: Response){
                        var res = response.body?.string()
                        listener.onResult(res.toString())
                    }
                })
        }).start()
    }

    fun getMessages(url: String, body: String, listener: SendDataListener){
        Thread(Runnable{
            var reqBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)

            val request = Request.Builder()
                .url(url)
                .post(reqBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    var res = response.body?.string()
                    listener.onResult(res.toString())
                }
            })
        }).start()
    }


    // params: contact
    fun deleteData(url: String, body: String, listener: SendDataListener){
        Thread(Runnable() {
            var reqBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)

            println("weee aaarreee heeerreee ")
            val request = Request.Builder()
                .url(url)
                .delete(reqBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener.onError(e);
                }

                override fun onResponse(call: Call, response: Response) {
                    var res = response.body?.string()
                    listener.onResult(res.toString());
                }
            })
        })
    }

}



