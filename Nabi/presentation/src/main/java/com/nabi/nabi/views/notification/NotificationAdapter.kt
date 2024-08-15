package com.nabi.nabi.views.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.databinding.ItemFcmNotifyBinding

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ActivityViewHolder>() {
    private var notifyList: List<String> = mutableListOf()

    inner class ActivityViewHolder(val binding: ItemFcmNotifyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: String) {
            binding.tvFcmContent.text = content
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityViewHolder {
        val binding =
            ItemFcmNotifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(notifyList[position])
    }

    override fun getItemCount(): Int = notifyList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<String>) {
        notifyList = newList
        notifyDataSetChanged()
    }
}