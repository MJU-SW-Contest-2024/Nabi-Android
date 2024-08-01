package com.nabi.nabi.views.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.home.RecentFiveDiary
import com.nabi.nabi.databinding.ItemDiaryBinding

class HomeRvAdapter : RecyclerView.Adapter<HomeRvAdapter.ActivityViewHolder>() {
    private var dataList: List<String> = mutableListOf()

    inner class ActivityViewHolder(private val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: String) {
            binding.tvDiary.text = content
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRvAdapter.ActivityViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeRvAdapter.ActivityViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<RecentFiveDiary>) {
        dataList = newList.map { it.content }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(isFull: Boolean)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}