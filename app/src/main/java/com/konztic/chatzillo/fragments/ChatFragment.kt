package com.konztic.chatzillo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.konztic.chatzillo.R
import com.konztic.chatzillo.adapters.ChatAdapter
import com.konztic.chatzillo.models.Message
import com.konztic.chatzillo.utilities.toast
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {

    private lateinit var _view: View
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view =  inflater.inflate(R.layout.fragment_chat, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpRecyclerView()
        setUpChatBtn()

        return _view
    }

    private fun setUpChatBtn() {
        chatDBRef = store.collection("andy-chats")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)

        _view.recycler_view.setHasFixedSize(true)
        _view.recycler_view.layoutManager = layoutManager
        _view.recycler_view.itemAnimator = DefaultItemAnimator()
        _view.recycler_view.adapter = adapter
    }

    private fun setUpChatDB() {
        _view.button_send.setOnClickListener {
            val messageText = _view.edit_text_message.text.toString()
            if (messageText.isNotEmpty()) {

                val message = Message(currentUser.uid, messageText, currentUser.photoUrl.toString(), Date())

                saveMessage(message)

                _view.edit_text_message.text = null
            }
        }
    }

    private fun saveMessage(message: Message) {
        val newMessage = HashMap<String, Any>()

        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sentAt"] = message.sentAt

        chatDBRef.add(newMessage)
            .addOnCompleteListener {
                activity!!.toast("Message added!")
            }
            .addOnFailureListener {
                activity!!.toast("Message error, try again!")
            }
    }
}