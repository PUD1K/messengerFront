package com.example.lab1.static_data

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
public class mess{
    var id: Int = 0
    var request_number: String = ""
    var response_number: String = ""
    var message: String = ""
    var createdAt: String = ""
    var updatedAt: String = ""

    constructor(id:Int = 0, request_number:String = "", response_number: String = "", message: String = "", createdAt: String = "", updatedAt: String = ""){
        this.id = id
        this.request_number = request_number
        this.response_number = response_number
        this.message = message
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }
}