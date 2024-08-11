package com.nabi.nabi.views.diary.add

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nabi.domain.model.diary.AddDiaryCallbackItem
import java.util.Calendar

class AddDiaryMonthCalendarStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private lateinit var onDateSelectedListener: OnDateSelectedListener
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        val fragment = AddDiaryMonthFragment.newInstance(calendar.time).apply {
            setOnDateSelectedListener(object : AddDiaryMonthFragment.OnDateSelectedListener {
                override fun onDateSelected(item: AddDiaryCallbackItem) {
                    onDateSelectedListener.onDateSelected(item)
                }
            })
        }
        return fragment
    }

    fun setOnDateSelectedListener(onDateSelectedListener: OnDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener
    }

    interface OnDateSelectedListener {
        fun onDateSelected(item: AddDiaryCallbackItem)
    }
}