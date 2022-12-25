package com.example.lab1

object user_data_static{
    var id: Int = 0
    var name: String = ""
    var pass: String = ""
    var number: String = ""
    var createdAt: String = ""
    var updatedAt: String = ""
}

object ip_static{
    val ip: String = "http://192.168.0.22:5000/api/v1"
    val ip_without_api: String = "http://192.168.0.22:5000"
}

object contact_data_static{
    var id: String =  ""
    var number: String =  ""
    var name: String =  ""
    var online: Boolean = false
}