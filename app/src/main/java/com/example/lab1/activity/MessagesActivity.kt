package com.example.lab1.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.*
import com.example.lab1.static_data.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val mSocket = SocketHandler.getSocket()

class MessagesActivity : AppCompatActivity() {
    private lateinit var messagesList: RecyclerView;
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        getSupportActionBar()?.setTitle(contact_data_static.name);
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue)))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btn_send_message = findViewById<ImageButton>(R.id.btn_send)
        val etv_message = findViewById<EditText>(R.id.etvMessage)
        var textOnline = findViewById<TextView>(R.id.text_online)

        if(contact_data_static.online){
            textOnline.visibility = View.VISIBLE
        }
        else{
            textOnline.visibility = View.GONE
        }

        var messages_list: MutableList<Message> = arrayListOf()

//        SocketHandler.setSocket()

//        mSocket.connect()
//        var room = user_data_static.number+contact_data_static.number
        var room = "room2"
        mSocket.emit("join_chat", room)


        val context = this

        val ClassConnect = Connection()
        val ClassListener1: Connection.SendDataListener = object : Connection.SendDataListener() {
            override fun onResult(value: String) {
                runOnUiThread(Runnable{
                var response = value
                messages_list += Mapper.map_to_messages(response)
                messages_list.sortBy { it.updatedAt }

                messagesList = findViewById<RecyclerView>(R.id.rv_messages)
                messagesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)

                messagesAdapter = MessagesAdapter(messages_list.size, messages_list, context)
                messagesList.setAdapter(messagesAdapter)
                })
            }
        }


        // получаем мои сообщения
        var obj =
            MessageDataForGet(user_data_static.number, contact_data_static.number)
        var body = Json.encodeToString(obj)

        ClassConnect.getMessages("${ip_static.ip}/messages/get", body,ClassListener1)

        // получаем сообщения контакта
        obj = MessageDataForGet(contact_data_static.number, user_data_static.number)
        body = Json.encodeToString(obj)

        //
        ClassConnect.getMessages("${ip_static.ip}/messages/get", body,ClassListener1)

        mSocket.on("message"){args->
            println(args[0])
            runOnUiThread(Runnable {
                messages_list += Mapper.map_to_messages("[${args[0]}]")
                messages_list.sortBy { it.updatedAt }
                messagesAdapter = MessagesAdapter(messages_list.size, messages_list, context)
                messagesList.setAdapter(messagesAdapter)
            })
        }

        btn_send_message.setOnClickListener(){
            val msg_obj =
                MessageDataForAdd(user_data_static.number, contact_data_static.number, etv_message.text.toString(), room)
            body = Json.encodeToString(msg_obj)
            // ClassConnect.sendData("${ip_static.ip}/messages/add", body,ClassListener2)

//            mSocket.emit("send_message", user_data_static.number, contact_data_static.number, etv_message.text.toString(), room)

            mSocket.emit("send_message", body, room)

            etv_message.setText("")

        }

        @Override
        fun onTaskRemoved(rootIntent: Intent) {
            mSocket.emit("offline", user_data_static.number)
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
        mSocket.emit("left_chat", "room2")
        finish()


        return true
    }
}