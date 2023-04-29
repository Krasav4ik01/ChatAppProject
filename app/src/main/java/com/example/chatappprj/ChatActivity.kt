package com.example.chatappprj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sentButton: FloatingActionButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth


    var receiverRoom: String? = null
    var senderRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        dbRef = FirebaseDatabase.getInstance().getReference()

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid


        supportActionBar?.title = name

        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageField)
        sentButton = findViewById(R.id.btnSend)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter


        dbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)

                        messageList.add(message!!)

                }
                messageAdapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
            }

        })

        sentButton.setOnClickListener{
            val message = messageBox.text.toString()
            val messageObjects = Message(message, senderUid)
            dbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObjects).addOnSuccessListener {
                dbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObjects)
            }
            messageBox.setText("")
        }

    }
}