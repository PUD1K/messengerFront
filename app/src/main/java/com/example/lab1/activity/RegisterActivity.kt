package com.example.lab1.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.lab1.*
import com.example.lab1.static_data.Mapper
import com.example.lab1.static_data.UserDataForLogin
import com.example.lab1.static_data.UserDataForRegistration
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_register)
        getSupportActionBar()?.setTitle("Register");
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val btn_reg = findViewById<Button>(R.id.btn_reg)
        val etv_name = findViewById<EditText>(R.id.etvName)
        val etv_pass = findViewById<EditText>(R.id.etvPass)
        val etv_age = findViewById<EditText>(R.id.etvAge)
        val etv_number = findViewById<EditText>(R.id.etvNumber)
        val cbox_pass = findViewById<CheckBox>(R.id.etvVisiblePass)

        val context = this

        val ClassConnect = Connection()
        val ClassListener: Connection.SendDataListener = object : Connection.SendDataListener() {
            override fun onResult(value: String) {
                var response = value

                var result = Mapper.map_to_message(response)
                println(result.message.length)
                if(result.message == "Пользователь зарегистрирован")
                    startActivity(Intent(context, LoginActivity::class.java))
                else
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
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

        btn_reg.setOnClickListener {
            if (etv_name.text.toString().length > 0 &&
                etv_pass.text.toString().length > 0 &&
                etv_number.text.toString().length > 0
            ) {
                val obj =
                    UserDataForRegistration(etv_name.text.toString(), etv_number.text.toString(), etv_pass.text.toString())
                var body = Json.encodeToString(obj)
                ClassConnect.sendData("${ip_static.ip}/user/registration", body, ClassListener)

            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
         if(item.itemId == android.R.id.home){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
             finish()
         }
        return true
    }
}