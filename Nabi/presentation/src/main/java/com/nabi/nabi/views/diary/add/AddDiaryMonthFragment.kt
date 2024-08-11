package com.nabi.nabi.views.diary.add

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nabi.domain.model.diary.AddDiaryCallbackItem
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentAddDiaryMonthBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.OnRvItemClickListener
import com.nabi.nabi.views.diary.detail.DetailDiaryFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddDiaryMonthFragment(private val callback: ((Throwable?) -> AddDiaryCallbackItem)? = null) :
    BaseFragment<FragmentAddDiaryMonthBinding>(R.layout.fragment_add_diary_month) {
    private val viewModel: AddDiarySelectDateViewModel by viewModels()
    private val sharedDateViewModel: SharedDateViewModel by activityViewModels()
    private lateinit var dayAdapter: AddDiaryCalendarAdapter
    private val dateMonthFormat = SimpleDateFormat("M", Locale.getDefault())
    private val dateYearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private lateinit var date: Date
    private var onDateSelectedListener: OnDateSelectedListener? = null
    private var diaryDates: Set<String> = emptySet()
    private var selectedDate: String? = null
    private var isClickable = true

    companion object {
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): AddDiaryMonthFragment {
            val fragment = AddDiaryMonthFragment()
            val args = Bundle()
            args.putLong(ARG_DATE, date.time)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView() {
        date = arguments?.getLong(ARG_DATE)?.let { Date(it) } ?: Date()

        sharedDateViewModel.clearSelectedDate()
        // 날짜 선택 옵저버
        sharedDateViewModel.selectedDate.observe(viewLifecycleOwner) {
            dayAdapter.setSelectedDate("")
        }

        sharedDateViewModel.monthChanged.observe(viewLifecycleOwner) { monthChanged ->
            if (monthChanged) {
                dayAdapter.notifyDataSetChanged()
                sharedDateViewModel.resetMonthChangedFlag()
            }
        }

        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val daysInMonth = getDaysInMonth(date)

        dayAdapter = AddDiaryCalendarAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<String> {
                override fun onClick(item: String) {

                    val day = item.padStart(2, '0')
                    val selectedDateStr = String.format("%d-%02d-%s", year, month, day)
                    setSelectedDate(item)
                    isClickable = !diaryDates.contains(selectedDateStr)

                    onDateSelectedListener?.onDateSelected(
                        AddDiaryCallbackItem(
                            selectedDateStr, isClickable
                        )
                    )
                    selectedDate = selectedDateStr
                }
            })
        }
        dayAdapter.submitList(
            matchDiaryEntriesWithDays(
                List<DiaryInfo?>(daysInMonth.size) { null },
                daysInMonth, year, month
            )
        )

        binding.rvCalendarDays.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvCalendarDays.adapter = dayAdapter
        binding.rvCalendarDays.itemAnimator = null

        viewModel.checkMonthDiary(
            month = dateMonthFormat.format(date.time).toInt(),
            year = dateYearFormat.format(date.time).toInt()
        )
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

    private fun matchDiaryEntriesWithDays(
        diaryInfos: List<DiaryInfo?>,
        days: List<String>,
        year: Int,
        month: Int
    ): List<Pair<String, DiaryInfo?>> {
        val result = mutableListOf<Pair<String, DiaryInfo?>>()
        val datePattern = """\d{4}-\d{2}-(\d{2})""".toRegex()

        for (day in days) {
            if (day == "previous" || day == "next") {
                result.add(day to null)
            } else {
                val dayWithLeadingZero = day.padStart(2, '0')  // day 앞에 0을 붙여 두 자리로 맞춤
                val dateString = String.format("%04d-%02d-%s", year, month, dayWithLeadingZero)

                val matchedDiaryInfo = diaryInfos.find { diaryInfo ->
                    val entryDate = diaryInfo?.diaryEntryDate
                    entryDate?.let { datePattern.find(it)?.groupValues?.get(1) == dayWithLeadingZero }
                        ?: false
                }

                result.add(dateString to matchedDiaryInfo)
            }
        }
        return result
    }


    override fun setObserver() {
        super.setObserver()

        viewModel.diaryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    LoggerUtils.e(state.message)
                }

                is UiState.Success -> {
                    dayAdapter.submitList(
                        matchDiaryEntriesWithDays(
                            state.data,
                            getDaysInMonth(date),
                            year = dateYearFormat.format(date.time).toInt(),
                            month = dateMonthFormat.format(date.time).toInt()
                        )
                    )
                    val dates = state.data.map { it.diaryEntryDate }
                    diaryDates = dates.toSet()
                    viewModel.setDiaryDates(diaryDates)
                }
            }
        }
    }

    fun setOnDateSelectedListener(onDateSelectedListener: OnDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener
    }

    interface OnDateSelectedListener {
        fun onDateSelected(item: AddDiaryCallbackItem)
    }

}