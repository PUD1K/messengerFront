package com.example.lab1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
//import com.example.lab1.static_data.Message

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "Users"
val COL_NAME = "name"
val COL_PASS = "pass"
val COL_AGE = "age"
val COL_NUMBER = "number"
val COL_IMAGE = "IMAGE"
val COL_ID = "id"
val COL_MY = "my_id"
val COL_FRIEND = "friend_id"
val COL_RECIPIENT = "recipient_id"
val TALBE_NAME_MESSAGES = "message"

class DataBaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 2) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUser = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name VARCHAR(256), pass VARCHAR(256), age INTEGER, number VARCHAR(15))";

        db?.execSQL(createTableUser)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val createTableContacts = "CREATE TABLE IF NOT EXISTS Contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, contact_number VARCHAR(15), " +
                "user_id INTEGER, FOREIGN KEY(user_id) REFERENCES Users(id))";

        val createTableMessages = "CREATE TABLE IF NOT EXISTS Messages (id INTEGER PRIMARY KEY AUTOINCREMENT, request_number VARCHAR(15), " +
                "response_number VARCHAR(15), message TEXT, date TIMESTAMP)";
        db?.execSQL(createTableContacts)
        db?.execSQL(createTableMessages)
    }


    fun insertData(user: User){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, user.name)
        cv.put(COL_PASS, user.pass)
        cv.put(COL_NUMBER, user.number)
//        val alterTable = "ALTER TABLE ${TABLE_NAME} ADD COLUMN number VARCHAR(15)"
//        db?.execSQL(alterTable)
        // проверка на существование юзера в базе
        var check = db.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE name = '${user.name}' or number ='${user.number}'", null)
        if(!check.moveToFirst()) {
            var result = db.insert(TABLE_NAME, null, cv)
            if (result == -1.toLong())
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, "Success registration!", Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(context, "User with this name or number already exist.", Toast.LENGTH_SHORT).show()
    }

    fun insertImage(username: String, image: ByteArray){
        val db = this.writableDatabase
        var result = db.rawQuery("UPDATE ${TABLE_NAME} SET(IMAGE = ${image}) WHERE name = '${username}'", null)
        if (result.equals(-1))
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Image successful update.", Toast.LENGTH_SHORT).show()
    }

    fun selectData(user: User): Boolean{
        val db = this.readableDatabase
        var result = db.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE (name = '${user.name}' or number = '${user.name}') and pass = '${user.pass}'", null)
        if (result.moveToFirst()){
            return true
        }
            Toast.makeText(context, "Wrong password or name.", Toast.LENGTH_SHORT).show()
            return false
        result.close()
    }

    //  get userId by login
    fun selectUserId(login: String): Int{
        var db = this.readableDatabase
        var result = db.rawQuery("SELECT * FROM Users WHERE name = '${login}' or number = '${login}'", null)
        if(result.moveToFirst()){
            return result.getInt(0);
        }
        return -1
    }

    // get userName by login
    fun selectUserName(login: String): String{
        var db = this.readableDatabase
        var result = db.rawQuery("SELECT * FROM Users WHERE name = ${login}", null)
        if(result.moveToFirst()){
            return result.getString(1);
        }
        return ""
    }

    //**************[Блок контактов]**************************************8
    // добавить в контакты
    fun addContact(user_id: Int, contact_number: String): Int{
        var db = this.writableDatabase
        val cv = ContentValues()
        cv.put("user_id", user_id)
        cv.put("contact_number", contact_number)
        var first_check = db.rawQuery("SELECT * FROM Users WHERE number = ${contact_number}", null)
        // если пользователь с таким номером найден
        if(first_check.moveToFirst()) {
            var check = db.rawQuery(
                "SELECT * FROM Contacts WHERE user_id = ${user_id} and contact_number = ${contact_number}",
                null
            )
            // если этого пользователя нет в контактах, добавляем
            if (!check.moveToFirst()) {
                db.insert("Contacts", null, cv)
                return 1
            }
            return 0
        }
        return -1
    }


    //Получение списка типа юзер, которые находятся в контактах у пользователя
    fun getContacts(id: Int): MutableList<User>{
        var db = this.readableDatabase
        var list: MutableList<User> = ArrayList()
        var result = db.rawQuery("SELECT Users.id, Users.name, Users.number FROM Contacts " +
                                     "INNER JOIN Users " +
                                     "ON Users.number = Contacts.contact_number " +
                                     "WHERE user_id = ${id}",null)
        if(result.moveToFirst()){
            while(!result.isAfterLast()){
                Log.d("usr_n", id.toString())
                val user = User(name = result.getString(result.getColumnIndexOrThrow("name")),
                    number = result.getString(result.getColumnIndexOrThrow("number")), id = result.getInt(result.getColumnIndexOrThrow("id")))
                list.add(user)
                result.moveToNext()
            }
        }
        return list
        result.close()
    }

    fun deleteContact(id: Int, contact_number: String): Boolean{
        var db = this.readableDatabase
        var result = db.rawQuery("SELECT * FROM Contacts WHERE user_id = '${id}' and contact_number = '${contact_number}'", null)
        Log.d("deleteQuery", "id = ${id} contact_number = ${contact_number}")
        if(result.moveToFirst()){
            Log.d("deleteQuery", "SUCCESS!")
//            var delete = db.rawQuery("DELETE FROM Contacts WHERE user_id = '${id}' and contact_number = '${contact_number}'", null)
            db.execSQL("DELETE FROM Contacts WHERE user_id = '${id}' and contact_number = '${contact_number}'")
            return true
        }
        return false
    }
    fun clearDatabase(){
        var db = this.writableDatabase
        db.execSQL("DELETE FROM Users WHERE number IS NULL")
    }
    // ********************[Конец блока контактов]***********************
    //  Отправка сообщений
    fun insertMessage(requestNumber: String, responseNumber: String, message:String): Boolean {
        var db = this.writableDatabase
        val cv = ContentValues()
        cv.put("request_number", requestNumber)
        cv.put("response_number", responseNumber)
        cv.put("message", message)
        try{
            db.insert("Messages", null, cv)
            return true
        }
        catch(e: Exception){
            return false
        }
    }

    // Получение сообщений - выходные данные: список<Message>
//    fun getMessages(requestNumber: String, responseNumber: String): MutableList<Message>{
//        var list: MutableList<Message> = ArrayList()
//        val db = this.readableDatabase
//        var result = db.rawQuery("SELECT * FROM Messages WHERE " +
//                "request_number = '${requestNumber}' and response_number = '${responseNumber}'" +
//                "OR request_number = '${responseNumber} and response_number = '${requestNumber}", null)
//
//        if(result.moveToFirst()){
//            while(!result.isAfterLast()){
//                var message = Message(result.getString(result.getColumnIndexOrThrow("request_number")),
//                    result.getString(result.getColumnIndexOrThrow("response_number")), result.getString(result.getColumnIndexOrThrow("message")))
//                list.add(message)
//                result.moveToNext()
//            }
//        }
//        return list
//        result.close()
//    }
}

