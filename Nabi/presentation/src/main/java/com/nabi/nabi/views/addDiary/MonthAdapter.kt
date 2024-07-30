package com.nabi.nabi.views.addDiary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.databinding.ItemMonthBinding
import java.util.Calendar
import java.util.Date

class MonthAdapter(private var currentMonthOffset: Int) :
    RecyclerView.Adapter<MonthAdapter.MonthView>() {
    private var calendar = Calendar.getInstance()

    inner class MonthView(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthView {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthView(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MonthView, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, currentMonthOffset)

        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)

        val dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
        calendar.add(Calendar.DAY_OF_MONTH, 1 - calendar.get(Calendar.DAY_OF_WEEK))
        for (i in 0 until 6) {
            for (k in 0 until 7) {
                dayList[i * 7 + k] = calendar.time
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val dayListManager = GridLayoutManager(holder.binding.root.context, 7)
        val dayListAdapter = DayAdapter(currentMonth, dayList)

        holder.binding.itemMonthDayList.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun updateCurrentMonth(monthOffset: Int) {
        currentMonthOffset += monthOffset
        notifyDataSetChanged()
    }
}