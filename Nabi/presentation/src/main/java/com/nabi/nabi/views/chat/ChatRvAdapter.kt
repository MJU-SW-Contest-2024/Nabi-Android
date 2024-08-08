package com.nabi.nabi.views.chat

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.chat.ChatItem
import com.nabi.nabi.databinding.ItemChatDateBinding
import com.nabi.nabi.databinding.ItemChatMyBinding
import com.nabi.nabi.databinding.ItemChatOtherBinding
import com.nabi.nabi.views.OnRvItemClickListener

class ChatRvAdapter(
    private val retryClickListener: OnRvItemClickListener<Int>
): ListAdapter<ChatItem, RecyclerView.ViewHolder>(chatDiffCallback) {

    companion object {
        private const val VIEW_TYPE_DATE = 0
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2

        private val chatDiffCallback = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var lastVisibleOtherChatViewHolder: OtherChatViewHolder? = null

    fun getLastChatId(): Int {
        return if (itemCount > 0) {
            getItem(itemCount - 1).chatId
        } else {
            0
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item.isDateHeader -> VIEW_TYPE_DATE
            item.isMine -> VIEW_TYPE_MY_MESSAGE
            else -> VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_DATE -> DateViewHolder.from(parent)
            VIEW_TYPE_MY_MESSAGE -> MyChatViewHolder.from(parent)
            VIEW_TYPE_OTHER_MESSAGE -> OtherChatViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> holder.bind(getItem(position))
            is MyChatViewHolder -> holder.bind(getItem(position))
            is OtherChatViewHolder -> {
                holder.bind(getItem(position), retryClickListener)
                manageOtherChatViewHolderVisibility(holder, position)
            }
        }
    }

    private fun manageOtherChatViewHolderVisibility(holder: OtherChatViewHolder, position: Int) {
        if (position == itemCount - 1) {
            lastVisibleOtherChatViewHolder?.binding?.ibRefresh?.visibility = View.GONE
            holder.binding.ibRefresh.visibility = View.VISIBLE
            lastVisibleOtherChatViewHolder = holder
        } else {
            holder.binding.ibRefresh.visibility = View.GONE
        }
    }

    class DateViewHolder private constructor(private val binding: ItemChatDateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) {
            binding.dateText.text = item.date
        }

        companion object {
            fun from(parent: ViewGroup): DateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemChatDateBinding.inflate(layoutInflater, parent, false)
                return DateViewHolder(view)
            }
        }
    }

    class MyChatViewHolder private constructor(private val binding: ItemChatMyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) {
            binding.messageText.text = item.content
            binding.tvTime.text = item.time
            setMaxWidth(binding.messageText, itemView.context)
        }

        private fun setMaxWidth(textView: TextView, context: Context) {
            val displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val maxWidth = (screenWidth * 0.7).toInt()
            textView.maxWidth = maxWidth
        }

        companion object {
            fun from(parent: ViewGroup): MyChatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemChatMyBinding.inflate(layoutInflater, parent, false)
                return MyChatViewHolder(view)
            }
        }
    }

    class OtherChatViewHolder private constructor(val binding: ItemChatOtherBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem, retryClickListener: OnRvItemClickListener<Int>) {
            binding.messageText.text = item.content
            binding.tvTime.text = item.time
            binding.ibRefresh.visibility = if (item.showRefreshIcon) View.VISIBLE else View.GONE
            setMaxWidth(binding.messageText, itemView.context)

            binding.ibRefresh.setOnClickListener {
                retryClickListener.onClick(bindingAdapterPosition)
            }
        }

        private fun setMaxWidth(textView: TextView, context: Context) {
            val displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val maxWidth = (screenWidth * 0.6).toInt()
            textView.maxWidth = maxWidth
        }

        companion object {
            fun from(parent: ViewGroup): OtherChatViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemChatOtherBinding.inflate(layoutInflater, parent, false)
                return OtherChatViewHolder(view)
            }
        }
    }
}

