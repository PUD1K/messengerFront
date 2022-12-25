package com.example.lab1.activity

import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1.*
import com.example.lab1.static_data.Contact
import com.example.lab1.static_data.ContactForSend
import com.example.lab1.static_data.Mapper
import com.example.lab1.static_data.Mapper.map_to_date
import com.example.lab1.static_data.Message
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessagesAdapter: RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    companion object {
        var viewHolderCount: Int = -1
    }

    var messagesList: MutableList<Message> = ArrayList()
    private var numberItems: Int = -1
    var context: MessagesActivity
    var context1 = this

    constructor(numberOfItems: Int, messageList: MutableList<Message>, context: MessagesActivity){
        numberItems = numberOfItems
        viewHolderCount = 0
        this.messagesList = messageList
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val context = parent.getContext()
        val layoutIdForThisItem = R.layout.item_list_messages

        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(layoutIdForThisItem, parent, false)

        val viewHolder = MessageViewHolder(view)
//        viewHolder.viewHolderItemIndex.setText("ViewHolder index " + viewHolderCount)

//        viewHolderCount++

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.message = messagesList[position]
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    class MessageViewHolder: RecyclerView.ViewHolder {

        var listItemNumberView: TextView
        var viewHolderItemIndex: TextView
        lateinit var context: MessagesActivity
        var message = Message()

        var response = "1"

        constructor(itemView: View): super(itemView){
            listItemNumberView = itemView.findViewById(R.id.tv_message)
            viewHolderItemIndex = itemView.findViewById(R.id.tv_message_date)
        }


        fun bind(listIndex: Int) {
            listItemNumberView.setText("${message.message}")
            viewHolderItemIndex.setText("${map_to_date(message.createdAt)}")
            println("message " + message.message)

            val paramsRight = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.RIGHT
            }

            val paramsLeft = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                gravity = Gravity.LEFT
            }

            if(message.request_number == contact_data_static.number){
                itemView.findViewById<TextView>(R.id.tv_message).setBackgroundResource(R.drawable.shape_message_contact)
                itemView.findViewById<LinearLayout>(R.id.ll_message).layoutParams = paramsLeft
                itemView.findViewById<TextView>(R.id.tv_message_date).gravity = Gravity.START
            }
            else{
                itemView.findViewById<TextView>(R.id.tv_message).setBackgroundResource(R.drawable.shape_message_me)
                itemView.findViewById<LinearLayout>(R.id.ll_message).layoutParams = paramsRight
                itemView.findViewById<TextView>(R.id.tv_message_date).gravity = Gravity.END
            }
        }
    }
}