package com.nabi.nabi.views.diary.view

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nabi.data.utils.LoggerUtils
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSelectDiaryMonthBinding
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SelectDiaryMonthFragment: BaseFragment<FragmentSelectDiaryMonthBinding>(R.layout.fragment_select_diary_month) {
    private val viewModel: SelectDiaryViewModel by viewModels()
    private lateinit var dayAdapter: SelectDiaryDayCalendarAdapter
    private val dateMonthFormat = SimpleDateFormat("M", Locale.getDefault())
    private val dateYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private lateinit var date: Date

    companion object {
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): SelectDiaryMonthFragment {
            val fragment = SelectDiaryMonthFragment()
            val args = Bundle()
            args.putLong(ARG_DATE, date.time)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {
        date = arguments?.getLong(ARG_DATE)?.let { Date(it) } ?: Date()

        val daysInMonth = getDaysInMonth(date)
        dayAdapter = SelectDiaryDayCalendarAdapter()
        dayAdapter.submitList(matchDiaryEntriesWithDays(List<DiaryInfo?>(daysInMonth.size){ null }, daysInMonth))

        binding.rvCalendarDays.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarDays.adapter = dayAdapter

        viewModel.fetchData(month = dateMonthFormat.format(date.time).toInt(), year = dateYearFormat.format(date.time).toInt())
    }

    private fun getDaysInMonth(date: Date): List<String> {
        val calendar = Calendar.getInstance().apply { time = date }
        val daysInMonth = mutableListOf<String>()

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        for (i in 0 until firstDayOfWeek) {
            daysInMonth.add("previous")
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            daysInMonth.add(i.toString())
        }

        return daysInMonth
    }

    private fun matchDiaryEntriesWithDays(diaryInfos: List<DiaryInfo?>, days: List<String>): List<Pair<String, DiaryInfo?>> {
        val result = mutableListOf<Pair<String, DiaryInfo?>>()
        val datePattern = """\d{4}-\d{2}-(\d{2})""".toRegex()

        for (day in days) {
            if (day == "previous" || day == "next") {
                result.add(day to null)
            } else {
                val matchedDiaryInfo = diaryInfos.find { diaryInfo ->
                    val entryDate = diaryInfo?.diaryEntryDate
                    entryDate?.let { datePattern.find(it)?.groupValues?.get(1) == day } ?: false
                }
                result.add(day to matchedDiaryInfo)
            }
        }

        return result
    }


    override fun setObserver() {
        super.setObserver()

        viewModel.diaryState.observe(viewLifecycleOwner){ state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    LoggerUtils.e(state.message)
                }
                is UiState.Success -> {
                    dayAdapter.submitList(matchDiaryEntriesWithDays(state.data, getDaysInMonth(date)))
                }
            }
        }
    }
}