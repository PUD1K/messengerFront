package com.example.lab1.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Window
import android.webkit.WebSettings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lab1.*
import com.example.lab1.static_data.Mapper
import com.example.lab1.static_data.UserDataForLogin
import com.github.nkzawa.engineio.client.transports.WebSocket
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)
        getSupportActionBar()?.setTitle("Login");
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue)))


        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        mSocket.connect()

        val btn_log = findViewById<Button>(R.id.btn_log)
        val etv_name = findViewById<EditText>(R.id.etvName)
        val etv_pass = findViewById<EditText>(R.id.etvPass)
        val cbox_pass = findViewById<CheckBox>(R.id.etvVisiblePass)

        val etv_reg = findViewById<TextView>(R.id.etv_register)

        val context = this

        var response = "1"
        val ClassConnect = Connection()
        val ClassListener: Connection.SendDataListener = object : Connection.SendDataListener() {
            override fun onResult(value: String) {
                response = value

                var result = Mapper.map_to_user(response)
                println("result " + result)
                println(result.message.length)
                if(result.message.length == 0){
                    user_data_static.name = result.name
                    user_data_static.number = result.number
                    user_data_static.id = result.id

                    mSocket.emit("login", user_data_static.number)

                    val thirtyIntent = Intent(context, MainMenuActivity::class.java)
                    startActivity(thirtyIntent)
                    finish()
                }
                else{
                    runOnUiThread(Runnable {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }

        mSocket.on("login"){ args ->
            println(args[0])
        }

        etv_reg.setOnClickListener{
            val secondIntent = Intent(this, RegisterActivity::class.java)
            startActivity(secondIntent)
        }

        cbox_pass.setOnCheckedChangeListener{
            button, isChecked ->
            if(!isChecked)
            {
                etv_pass.setTransformationMethod(PasswordTransformationMethod.getInstance())
                etv_pass.setSelection(etv_pass.text.length)
            }
            else{
                etv_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                etv_pass.setSelection(etv_pass.text.length)
            }

        }

        btn_log.setOnClickListener {
            if (etv_name.text.toString().length > 0 &&
                etv_pass.text.toString().length > 0) {

                val obj =
                    UserDataForLogin(etv_name.text.toString(), etv_pass.text.toString())
                var body = Json.encodeToString(obj)
                ClassConnect.sendData("${ip_static.ip}/user/login", body, ClassListener)
            }
            else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
