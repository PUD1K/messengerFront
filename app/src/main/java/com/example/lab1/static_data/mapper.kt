package com.example.lab1.static_data

import com.example.lab1.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object Mapper {
    val format = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun map_to_user(value: String): User {
        println("result" + value)
        return format.decodeFromString(value)
    }

    fun map_to_conctacts(value: String): MutableList<Contact> {
        println("result" + value)
        return format.decodeFromString(value)
    }

    fun map_to_messages(value: String): MutableList<Message>{
        println("result" + value)
        return format.decodeFromString(value)
    }

    fun map_to_message(value: String): msg{
        println("result" + value)
        return format.decodeFromString(value)
    }

    fun map_to_date(value: String): String{
        var date = ""
        for(i in 0..9)
            date+= value.get(i)
        date += " "
        for(i in 11..15)
            date+= value.get(i)
        date += " AM"
        return date
    }

    interface Datas
}


@Serializable
@SerialName("user")
data class userData(val user: User = User(), val message: String = "") : Mapper.Datas {
    override fun toString(): String {
        return "User [id: ${this.user.id}, name: ${this.user.name}, pass: ${this.user.pass}, number: ${this.user.number}, createdAt: ${this.user.createdAt}, updatedAt: ${this.user.updatedAt}]"
    }
}

@Serializable
class Contact{
    var contact_number: String = ""
    var name: String = ""
    var online: Boolean = false

    constructor(name:String = "", contact_number: String = "", online: Boolean = false){
        this.contact_number = contact_number
        this.name = name
        this.online = online
    }
}

@Serializable
data class msg(
    val message: String
)
@Serializable
data class UserDataForLogin(val number: String, val password: String)

@Serializable
data class UserDataForRegistration(val name: String, val number: String, val password: String)


@Serializable
data class MessageDataForGet(val request_number: String, val response_number: String)

@Serializable
data class MessageDataForAdd(val request_number: String, val response_number: String, val message: String, val room: String)

@Serializable
class ContactForSend(val id: String, val number: String)

@Serializable
class UserNumber(val number: String)

@Serializable
public class Message{
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

