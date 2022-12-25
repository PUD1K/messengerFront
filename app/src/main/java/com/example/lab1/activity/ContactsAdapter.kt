package com.example.lab1.activity

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.*
import com.example.lab1.static_data.Contact
import com.example.lab1.static_data.ContactForSend
import com.example.lab1.static_data.Mapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ContactsAdapter: RecyclerView.Adapter<ContactsAdapter.NumberViewHolder> {

    companion object {
        var viewHolderCount: Int = -1
    }

    var usersList: MutableList<Contact> = ArrayList()
    private var numberItems: Int = -1
    var context: MainMenuActivity
    var context1 = this

    constructor(numberOfItems: Int, usersList: MutableList<Contact>, context: MainMenuActivity){
        numberItems = numberOfItems
        viewHolderCount = 0
        this.usersList = usersList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val context = parent.getContext()
        val layoutIdForThisItem = R.layout.item_list_contacts

        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(layoutIdForThisItem, parent, false)

        val viewHolder = NumberViewHolder(view)
//        viewHolder.viewHolderItemIndex.setText("ViewHolder index " + viewHolderCount)

//        viewHolderCount++

        return viewHolder
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.contact = usersList[position]
        holder.context = context
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    class NumberViewHolder: RecyclerView.ViewHolder {

        var listItemNumberView: TextView
        var viewHolderItemIndex: TextView
        var listItemNumberViewContact: TextView
        lateinit var context: MainMenuActivity
        var contact = Contact()

        var response = "1"
        val ClassConnect = Connection()
        val ClassListener: Connection.SendDataListener = object : Connection.SendDataListener() {
            override fun onResult(value: String) {
                response = value
                var users_list = Mapper.map_to_conctacts(response)
                Log.d("users_list", users_list.toString())
            }
        }

        constructor(itemView: View): super(itemView){
            listItemNumberView = itemView.findViewById(R.id.tv_number_item)
            viewHolderItemIndex = itemView.findViewById(R.id.tv_view_holder_number)
            listItemNumberViewContact = itemView.findViewById(R.id.tv_number_item_contact)

            itemView.setOnClickListener{
                contact_data_static.name = contact.name
                contact_data_static.number = contact.contact_number
                contact_data_static.online = contact.online

                val thirtyIntent = Intent(context, MessagesActivity::class.java)
                context.startActivity(thirtyIntent)
                context.finish()
            }

            itemView.findViewById<ImageButton>(R.id.btn_remove_contact).setOnClickListener{
                var obj = ContactForSend(user_data_static.id.toString(), contact.contact_number)
                var body = Json.encodeToString(obj)
                ClassConnect.deleteData("${ip_static.ip}/contacts/remove", body, ClassListener)

                itemView.visibility = View.GONE
            }
        }


        fun bind(listIndex: Int) {
//            listItemNumberView.setText(listIndex.toString())
            listItemNumberView.setText("${contact.name}")
            listItemNumberViewContact.setText("${contact.contact_number}")
            var v_online = itemView.findViewById<LinearLayout>(R.id.online_layout)
            var v_offline = itemView.findViewById<LinearLayout>(R.id.offline_layout)

            if(contact.online) {
                v_online.visibility = View.VISIBLE
                v_offline.visibility = View.GONE
            }
            else{
                v_online.visibility = View.GONE
                v_offline.visibility = View.VISIBLE
            }
        }
    }
}