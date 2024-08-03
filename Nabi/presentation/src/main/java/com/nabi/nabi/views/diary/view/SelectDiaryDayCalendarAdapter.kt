package com.nabi.nabi.views.diary.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nabi.data.utils.LoggerUtils
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.nabi.R
import com.nabi.nabi.databinding.ItemSelectDiaryDayBinding

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
            binding.root.isClickable = true
            binding.root.visibility = View.VISIBLE

            diaryInfo.second?.let {
                val resourceId = when(it.emotion){
                    "행복" -> R.drawable.img_happiness
                    "우울" -> R.drawable.img_sadness
                    "화남" -> R.drawable.img_anger
                    "불안" -> R.drawable.img_anxiety
                    "지루" -> R.drawable.img_boredom
                    else -> R.drawable.shape_radius_50_with_stroke // todo 감정 데이터 Null 경우 임시로 처리
                }
                binding.ivDiaryCheck.setImageResource(resourceId)
                if(it.emotion.isNullOrEmpty()) binding.ivDiaryCheck.alpha = 0.2f // todo 감정 데이터 Null 경우 임시로 처리
            }
        }

        fun clear() {
            binding.tvDay.text = ""
            binding.root.isClickable = false
            binding.root.visibility = View.GONE
        }
    }
}
