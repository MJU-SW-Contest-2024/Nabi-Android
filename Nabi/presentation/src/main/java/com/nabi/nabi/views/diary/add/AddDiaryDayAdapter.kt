package com.nabi.nabi.views.diary.add

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.R
import com.nabi.nabi.databinding.ItemDayBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class AddDiaryDayAdapter(
    private val currentMonth: Int,
    private val dayList: MutableList<Date>,
    private val onDateSelected: (Date) -> Unit,
) :
    RecyclerView.Adapter<AddDiaryDayAdapter.DayView>() {
    private val ROW = 6
    private var diaryDates: Set<String> = emptySet()
    private var selectedDate: Date? = null

    inner class DayView(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        with(holder.binding) {
            itemDayLayout.setOnClickListener {
                val selectedDate = dayList[position]
                onDateSelected(selectedDate)
            }

            val calendar = Calendar.getInstance()
            calendar.time = dayList[position]
            val dayMonth = calendar.get(Calendar.MONTH) + 1
            tvDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

            val today = Calendar.getInstance()
            if (currentMonth != dayMonth) {
                // 다른 달의 날짜 안 보이게 표시
                ivDiaryCheck.alpha = 0.0f
                ivSelectDate.alpha = 0.0f
                tvDay.alpha = 0.0f
            } else {
                tvDay.alpha = 1.0f

                // 해당 일(day)이 diaryDates에 포함되어 있는지 확인
                val dateFormat = SimpleDateFormat("dd", Locale.KOREAN)
                val formattedDay = dateFormat.format(calendar.time)
                val isDateInDiary = diaryDates.contains(formattedDay)
                val isSelectedDate = isSameDate(dayList[position], selectedDate)

                // 선택된 날짜에 일기가 존재하면
                ivDiaryCheck.alpha = if (isDateInDiary) 1.0f else 0.0f
                ivDiaryCheck.background.setColorFilter(
                    if (isSelectedDate && isDateInDiary) Color.WHITE else Color.TRANSPARENT,
                    PorterDuff.Mode.SRC_ATOP
                )

                // 선택된 날짜 칠하기
                ivSelectDate.alpha = if (isSelectedDate) 1.0f else 0.0f

                itemDayLayout.setOnClickListener {
                    onDateSelected(calendar.time)
                }

                // 오늘 이후 날짜는 회색으로 처리
                if (calendar.after(today)) {
                    val gray2 = ContextCompat.getColor(holder.itemView.context, R.color.gray2)
                    tvDay.setTextColor(gray2)
                } else {
                    tvDay.setTextColor(Color.BLACK)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }

    fun setDiaryDates(diaryDates: Set<String>) {
        this.diaryDates = diaryDates
        notifyDataSetChanged()
    }

    fun setSelectedDate(date: Date?) {
        selectedDate = date
        notifyDataSetChanged()
    }

    private fun isSameDate(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}
