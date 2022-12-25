package com.example.lab1.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lab1.*
import com.example.lab1.static_data.ContactForSend
import com.example.lab1.static_data.Mapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AddContactActivity : AppCompatActivity() {
    companion object{
        const val name = "user_name"
    }

    val mSocket = SocketHandler.getSocket()

    var user_name_for_menu = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_add_contact)
        getSupportActionBar()?.setTitle("Add contact");
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var context = this

        var response = "1"
        val ClassConnect = Connection()
        val ClassListener: Connection.SendDataListener = object : Connection.SendDataListener() {
            override fun onResult(value: String) {
                response = value

                var result = Mapper.map_to_user(response)
                if(result.message == "Пользователь успешно добавлен"){
                    val thirtyIntent = Intent(context, MainMenuActivity::class.java)
                    startActivity(thirtyIntent)
                    finish()
                }
                else{
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        val btn_add = findViewById<Button>(R.id.btn_add)
        val etv_contact = findViewById<EditText>(R.id.etv_Number)

        btn_add.setOnClickListener{
            val user_id = user_data_static.id.toString()
            if(user_id != null) {
                println(user_id)
                var obj = ContactForSend(user_id, etv_contact.text.toString())
                var body = Json.encodeToString(obj)
                ClassConnect.sendData("${ip_static.ip}/contacts/add", body, ClassListener)
            }
            else{
                Toast.makeText(this, "Some any problems, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        if(item.toString() == "Logout"){
            startActivity(Intent(this, LoginActivity::class.java))
            mSocket.emit("offline", user_data_static.number)
        }
        else if(item.itemId == android.R.id.home){
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        finish()

        return true
    }
}