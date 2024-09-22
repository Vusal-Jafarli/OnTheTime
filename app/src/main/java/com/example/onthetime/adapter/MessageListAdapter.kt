package com.example.onthetime.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.model.Message
import com.example.onthetime.databinding.ItemMessageListBinding

class MessageListAdapter(private val onItemClick: (Message) -> Unit) :
    RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {

    private var messageList: List<Message> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messageList.size

    fun submitList(list: List<Message>) {
        messageList = list
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(private val binding: ItemMessageListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.userName.text = message.receiverId // User ID'yi gösteriyoruz, istersen User verisiyle değiştirebilirsin.
            binding.root.setOnClickListener { onItemClick(message) }
        }
    }
}
