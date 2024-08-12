package com.nabi.nabi.views.diary.add

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nabi.domain.model.diary.DiarySelectInfo
import java.util.Calendar

class AddDiaryMonthCalendarStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        return AddDiaryMonthFragment.newInstance(calendar.time)
    }
}