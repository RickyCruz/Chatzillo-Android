package com.konztic.chatzillo.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.konztic.chatzillo.R
import com.konztic.chatzillo.models.Message
import com.konztic.chatzillo.utilities.inflate
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_right.view.*
import java.text.SimpleDateFormat

class ChatAdapter(val items: List<Message>, val userId: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_right
    private val layoutLeft = R.layout.fragment_chat_item_left

    // If we have more than one design for the adapter we have to override the method getItemViewType
    override fun getItemViewType(position: Int): Int {
        return if (items[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            MY_MESSAGE -> ViewHolderRight(parent.inflate(layoutRight))
            else -> ViewHolderLeft(parent.inflate(layoutLeft))
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) { // Get viewType
            MY_MESSAGE -> (holder as ViewHolderRight).bind((items[position]))
            GLOBAL_MESSAGE -> (holder as ViewHolderLeft).bind((items[position]))
        }
    }

    class ViewHolderRight(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(message: Message) = with(itemView) {
            textViewMessageRight.text = message.message
            textViewTimeRight.text = SimpleDateFormat("hh:mm").format(message.sentAt)

            if (message.profileImageURL.isEmpty()) {
                Glide.with(itemView)
                    .load(R.drawable.ic_person)
                    .override(64, 64)
                    .circleCrop()
                    .into(imageViewProfileRight)
            } else {
                Glide.with(itemView)
                    .load(message.profileImageURL)
                    .override(64, 64)
                    .circleCrop()
                    .into(imageViewProfileRight)
            }
        }
    }

    class ViewHolderLeft(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(message: Message) = with(itemView) {
            textViewMessageLeft.text = message.message
            textViewTimeLeft.text = SimpleDateFormat("hh:mm").format(message.sentAt)

            if (message.profileImageURL.isEmpty()) {
                Glide.with(itemView)
                    .load(R.drawable.ic_person)
                    .override(64, 64)
                    .circleCrop()
                    .into(imageViewProfileLeft)
            } else {
                Glide.with(itemView)
                    .load(message.profileImageURL)
                    .override(64, 64)
                    .circleCrop()
                    .into(imageViewProfileLeft)
            }
        }
    }
}