package com.example.lab1.activity

import SocketHandler
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.*
import com.example.lab1.static_data.Mapper

class MainMenuActivity : AppCompatActivity() {
    private lateinit var numbersList: RecyclerView;
    private lateinit var contactsAdapter: ContactsAdapter
    companion object{
        const val name = "user_name"
    }

    val mSocket = SocketHandler.getSocket()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.setTitle("Contacts");
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue)))

        val context = this
        val db = DataBaseHandler(context)

        val btn_add_contact = findViewById<ImageButton>(R.id.btn_add_contact)


        val ClassConnect = Connection()
        val ClassListener: Connection.SendDataListener = object : Connection.SendDataListener() {
            override fun onResult(value: String) {
                var response = value

                var users_list = Mapper.map_to_conctacts(response)
                Log.d("users_list", users_list.toString())

                runOnUiThread(Runnable {
                        // use adapter
                        numbersList = findViewById<RecyclerView>(R.id.rv_contacts)
                        numbersList.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                        contactsAdapter = ContactsAdapter(users_list.size, users_list, context)
                        Log.d("12345", contactsAdapter.usersList.toString())
                        numbersList.setAdapter(contactsAdapter)
                })
            }
//                android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        }

//        val user_id = db.selectUserId(user_data_static.name)
        ClassConnect.getData("${ip_static.ip}/contacts/${user_data_static.id}", ClassListener)

        btn_add_contact.setOnClickListener{
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
            finish()
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
        else{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        finish()

        return true
    }
}