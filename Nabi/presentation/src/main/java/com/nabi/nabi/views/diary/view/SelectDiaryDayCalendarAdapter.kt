package com.nabi.nabi.views.diary.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.nabi.R
import com.nabi.nabi.databinding.ItemSelectDiaryDayBinding
import com.nabi.nabi.views.OnRvItemClickListener

class SelectDiaryDayCalendarAdapter() : ListAdapter<Pair<String, DiaryInfo?>, SelectDiaryDayCalendarAdapter.DateViewHolder>(diaryDiffUtil) {

    companion object {
        private val diaryDiffUtil = object : DiffUtil.ItemCallback<Pair<String, DiaryInfo?>>() {
            override fun areItemsTheSame(oldItem: Pair<String, DiaryInfo?>, newItem: Pair<String, DiaryInfo?>): Boolean =
                oldItem.second?.diaryId == newItem.second?.diaryId

            override fun areContentsTheSame(oldItem: Pair<String, DiaryInfo?>, newItem: Pair<String, DiaryInfo?>): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemSelectDiaryDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        when(getItem(position).first){
            "previous" -> holder.clear()
            "next" -> {}
            else -> holder.bind(getItem(position))
        }
    }

    inner class DateViewHolder(val binding: ItemSelectDiaryDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(diaryInfo: Pair<String, DiaryInfo?>) {
            binding.tvDay.text = diaryInfo.first
            binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            binding.root.isClickable = true
            binding.root.visibility = View.VISIBLE

            diaryInfo.second?.let {
                val resourceId = when(it.emotion){
                    "행복" -> R.drawable.img_happiness_empty
                    "우울" -> R.drawable.img_sadness_empty
                    "화남" -> R.drawable.img_anger_empty
                    "불안" -> R.drawable.img_anxiety_empty
                    "평온" -> R.drawable.img_boredom_empty
                    else -> R.drawable.img_boredom_empty_gray
                }
                binding.ivDiaryCheck.setImageResource(resourceId)
                binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            itemView.setOnClickListener {
                if(diaryInfo.second != null) rvItemClickListener.onClick(diaryInfo.second!!.diaryId)
            }
        }

        fun clear() {
            binding.tvDay.text = ""
            binding.root.isClickable = false
            binding.root.visibility = View.GONE
        }
    }

    private lateinit var rvItemClickListener: OnRvItemClickListener<Int>

    fun setRvItemClickListener(rvItemClickListener: OnRvItemClickListener<Int>){
        this.rvItemClickListener = rvItemClickListener
    }
}
