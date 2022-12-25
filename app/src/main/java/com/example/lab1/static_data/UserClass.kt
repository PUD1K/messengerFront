package com.example.lab1

import android.service.autofill.UserData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
class User{
    var id: Int = 0
    var name: String = ""
    var pass: String = ""
//    var age: Int = 0
    var online: Boolean = false
    var number: String = ""
    var createdAt: String = ""
    var updatedAt: String = ""
    var message: String = ""

    constructor(name:String = "", pass: String = "", number: String = "", id: Int = 0, createdAt: String = "", updatedAt: String = "", message: String = "", online: Boolean = false){
        this.name = name
        this.pass = pass
//        this.age = age
        this.number = number
        this.id = id
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.online = online
    }


    override fun toString(): String {
        return "User [id: ${this.id}, name: ${this.name}, pass: ${this.pass}, number: ${this.number}, createdAt: ${this.createdAt}, updatedAt: ${this.updatedAt}, message: ${this.message}]"
    }
}

