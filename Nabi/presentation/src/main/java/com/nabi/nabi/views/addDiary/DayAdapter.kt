package com.nabi.nabi.views.addDiary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.databinding.ItemDayBinding
import java.util.Calendar
import java.util.Date

class DayAdapter(private val currentMonth: Int, private val dayList: MutableList<Date>) :
    RecyclerView.Adapter<DayAdapter.DayView>() {
    private val ROW = 6

    inner class DayView(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        with(holder.binding) {
            itemDayLayout.setOnClickListener {
                Toast.makeText(holder.itemView.context, "${dayList[position]}", Toast.LENGTH_SHORT)
                    .show()
            }

            val calendar = Calendar.getInstance()
            calendar.time = dayList[position]
            val dayMonth = calendar.get(Calendar.MONTH) + 1
            tvDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            
            if (currentMonth != dayMonth) {
                // 다른 달의 날짜 안 보이게 표시
                tvDay.alpha = 0.0f
            } else {
                tvDay.alpha = 1.0f
            }
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}
