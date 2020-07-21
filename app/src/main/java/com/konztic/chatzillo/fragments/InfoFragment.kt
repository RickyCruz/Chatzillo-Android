package com.konztic.chatzillo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.konztic.chatzillo.R
import kotlinx.android.synthetic.main.fragment_chat_item_right.view.*
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {
    private lateinit var _view: View

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDBRef: CollectionReference

    private var chatSubscription: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view =  inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDB()
        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        return _view
    }

    private fun setUpChatDB() {
        chatDBRef = store.collection("andy-chats")
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        _view.text_view_info_email.text = currentUser.email
        _view.text_view_info_name.text = currentUser.displayName?.let { currentUser.displayName } ?: run { getString(R.string.info_no_name) }

        currentUser.photoUrl?.let {
            Glide.with(this).load(currentUser.photoUrl).override(256, 256)
                .circleCrop().into(_view.image_view_info_avatar)
        } ?: run {
            Glide.with(this).load(R.drawable.ic_person).override(128, 128)
                .circleCrop().into(_view.image_view_info_avatar)
        }
    }
}