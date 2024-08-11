package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.nabi.R
import com.nabi.nabi.databinding.ItemDayBinding
import com.nabi.nabi.databinding.ItemSelectDiaryDayBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.OnRvItemClickListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddDiaryCalendarAdapter :
    ListAdapter<Pair<String, DiaryInfo?>, AddDiaryCalendarAdapter.DateViewHolder>(diaryDiffUtil) {
    private var selectedDate: String? = null

    companion object {
        private val diaryDiffUtil = object : DiffUtil.ItemCallback<Pair<String, DiaryInfo?>>() {
            override fun areItemsTheSame(
                oldItem: Pair<String, DiaryInfo?>,
                newItem: Pair<String, DiaryInfo?>
            ): Boolean =
                oldItem.second?.diaryId == newItem.second?.diaryId

            override fun areContentsTheSame(
                oldItem: Pair<String, DiaryInfo?>,
                newItem: Pair<String, DiaryInfo?>
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding =
            ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        when (getItem(position).first) {
            "previous" -> holder.clear()
            "next" -> {}
            else -> holder.bind(getItem(position))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedDate(date: String) {
        selectedDate = date
        notifyDataSetChanged()
    }

    inner class DateViewHolder(val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(diaryInfo: Pair<String, DiaryInfo?>) {
            val day = diaryInfo.first
            binding.root.isClickable = true
            binding.root.visibility = View.VISIBLE
            binding.ivSelectDate.alpha = 0.0f

            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dates = dateFormatter.parse(day)
            val selectedCalendar = Calendar.getInstance().apply {
                time = dates!!
            }
            val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH).toString()
            binding.tvDay.text = dayOfMonth
            val todayCalendar = Calendar.getInstance()

            if (selectedCalendar.after(todayCalendar)) {
                binding.tvDay.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray2))
                binding.root.isClickable = false
                binding.ivDiaryCheck.alpha = 0.0f
            } else {
                val isToday =
                    selectedCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                            selectedCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                            selectedCalendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(
                        Calendar.DAY_OF_MONTH
                    )

                val isSelectedDate = dayOfMonth == selectedDate
                if (diaryInfo.second?.content != null) {
                    // 작성한 일기가 있는 날짜에 .표시
                    if (isSelectedDate) {
                        // 작성한 일기가 있는 날짜 선택
                        binding.ivSelectDate.alpha = 1.0f
                        binding.ivDiaryCheck.backgroundTintList = ContextCompat.getColorStateList(
                            itemView.context, R.color.white
                        )
                    } else {
                        binding.ivSelectDate.alpha = 0.0f
                    }
                } else {
                    binding.ivDiaryCheck.backgroundTintList = ContextCompat.getColorStateList(
                        itemView.context, R.color.white
                    )

                    if (isSelectedDate) {
                        // 작성한 일기가 없는 날짜 선택
                        binding.ivSelectDate.alpha = 1.0f
                        binding.ivDiaryCheck.backgroundTintList = ContextCompat.getColorStateList(
                            itemView.context, R.color.primary
                        )
                    } else {
                        binding.ivSelectDate.alpha = 0.0f
                    }
                }

                if (isToday) {
                    binding.ivSelectDate.alpha = 1.0f
                    if (diaryInfo.second?.content == null) {
                        binding.ivDiaryCheck.backgroundTintList = ContextCompat.getColorStateList(
                            itemView.context, R.color.primary
                        )
                    }
                }

                itemView.setOnClickListener {
                    rvItemClickListener.onClick(dayOfMonth)
                }
            }
        }

        fun clear() {
            binding.tvDay.text = ""
            binding.root.isClickable = false
            binding.root.visibility = View.GONE
        }
    }

    private lateinit var rvItemClickListener: OnRvItemClickListener<String>

    fun setRvItemClickListener(rvItemClickListener: OnRvItemClickListener<String>) {
        this.rvItemClickListener = rvItemClickListener
    }
}