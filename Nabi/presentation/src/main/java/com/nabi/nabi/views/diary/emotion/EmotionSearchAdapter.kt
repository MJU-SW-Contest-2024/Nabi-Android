package com.nabi.nabi.views.diary.emotion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.nabi.databinding.ItemSearchDiaryBinding
import com.nabi.nabi.views.OnRvItemClickListener

class EmotionSearchAdapter : ListAdapter<SearchDiary, EmotionSearchAdapter.SearchResultViewHolder>(diaryDiffUtil) {

    companion object {
        private val diaryDiffUtil = object : DiffUtil.ItemCallback<SearchDiary>() {
            override fun areItemsTheSame(oldItem: SearchDiary, newItem: SearchDiary): Boolean =
                oldItem.diaryId == newItem.diaryId

            override fun areContentsTheSame(oldItem: SearchDiary, newItem: SearchDiary): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchResultViewHolder(val binding: ItemSearchDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchDiary) {
            binding.tvSearchDiaryDate.text = item.diaryEntryDate
            binding.tvContent.text = item.previewContent

            itemView.setOnClickListener {
                rvItemClickListener.onClick(item.diaryId)
            }
        }
    }

    private lateinit var rvItemClickListener: OnRvItemClickListener<Int>

    fun setRvItemClickListener(rvItemClickListener: OnRvItemClickListener<Int>){
        this.rvItemClickListener = rvItemClickListener
    }

    override fun submitList(list: List<SearchDiary>?) {
        list?.let {
            val filteredList = it.distinctBy { diary -> diary.diaryId }
            super.submitList(filteredList)
        } ?: super.submitList(null)
    }
}
