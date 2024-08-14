package com.nabi.nabi.views.diary.add

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nabi.domain.model.diary.DiarySelectInfo
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentAddDiaryMonthBinding
import com.nabi.nabi.utils.Constants.dateEnglishOnlyYearFormat
import com.nabi.nabi.utils.Constants.dateNumberOnlyMonthFormat
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.OnRvItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddDiaryMonthFragment :
    BaseFragment<FragmentAddDiaryMonthBinding>(R.layout.fragment_add_diary_month) {

    private val viewModel: AddDiarySelectDateViewModel by viewModels()
    private val sharedDateViewModel: SharedDateViewModel by activityViewModels()
    private lateinit var dayAdapter: AddDiaryCalendarAdapter

    private lateinit var date: Date
    private var diaryDates: Set<String> = emptySet()
    private var isClickable = true

    companion object {
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): AddDiaryMonthFragment {
            return AddDiaryMonthFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_DATE, date.time)
                }
            }
        }
    }

    override fun initView() {
        date = arguments?.getLong(ARG_DATE)?.let { Date(it) } ?: Date()

        setupRecyclerView()
        loadDiaryData()
    }

    private fun setupRecyclerView() {
        val diaryDays = getDaysInMonth(date)

        dayAdapter = AddDiaryCalendarAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<DiarySelectInfo> {
                override fun onClick(item: DiarySelectInfo) {
                    sharedDateViewModel.changeSelectedDate(item.diaryEntryDate)
                    isClickable = !diaryDates.contains(item.diaryEntryDate)
                    sharedDateViewModel.changeDateInfo(item)
                }
            })
        }

        binding.rvCalendarDays.apply {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = dayAdapter
            itemAnimator = null
        }
        dayAdapter.setList(
            matchDiaryEntriesWithDays(
                List<DiarySelectInfo?>(diaryDays.size) { null },
                diaryDays,
                year = dateEnglishOnlyYearFormat.format(date.time).toInt(),
                month = dateNumberOnlyMonthFormat.format(date.time).toInt()
            )
        )
    }

    private fun loadDiaryData() {
        viewModel.checkMonthDiary(
            month = dateNumberOnlyMonthFormat.format(date.time).toInt(),
            year = dateEnglishOnlyYearFormat.format(date.time).toInt()
        )
    }

    private fun getDaysInMonth(date: Date): List<String> {
        val calendar = Calendar.getInstance().apply { time = date }
        val daysInMonth = mutableListOf<String>()

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        repeat(firstDayOfWeek) {
            daysInMonth.add("previous")
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        daysInMonth.addAll((1..maxDay).map { it.toString() })

        return daysInMonth
    }

    private fun matchDiaryEntriesWithDays(
        diaryInfos: List<DiarySelectInfo?>,
        days: List<String>,
        year: Int,
        month: Int
    ): List<Pair<String, DiarySelectInfo?>> {
        val result = mutableListOf<Pair<String, DiarySelectInfo?>>()
        val datePattern = """\d{4}-\d{2}-(\d{2})""".toRegex()

        for (day in days) {
            if (day == "previous" || day == "next") {
                result.add(day to null)
            } else {
                val dayWithLeadingZero = day.padStart(2, '0')  // day 앞에 0을 붙여 두 자리로 맞춤
                val dateString = String.format("%04d-%02d-%s", year, month, dayWithLeadingZero)

                var matchedDiaryInfo = diaryInfos.find { diaryInfo ->
                    val entryDate = diaryInfo?.diaryEntryDate
                    entryDate?.let { datePattern.find(it)?.groupValues?.get(1) == dayWithLeadingZero }
                        ?: false
                }
                if (matchedDiaryInfo == null) {
                    matchedDiaryInfo = DiarySelectInfo(false, dateString, false)
                } else {
                    matchedDiaryInfo.existDiary = true
                }

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val todayDate = dateFormat.format(Calendar.getInstance().time)

                if (sharedDateViewModel.date.value != null) {
                    if (sharedDateViewModel.date.value!!.diaryEntryDate == dateString) {
                        matchedDiaryInfo.isSelected = true
                    }
                } else {
                    if (todayDate == dateString) {
                        matchedDiaryInfo.isSelected = true
                    }
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
                is UiState.Failure -> LoggerUtils.e(state.message)
                is UiState.Success -> handleSuccessState(state.data)
            }
        }

        sharedDateViewModel.selectedDate.observe(viewLifecycleOwner) { selectedDate ->
            updateSelectedDateInAdapter(selectedDate)
        }
    }

    private fun handleSuccessState(diaryEntries: List<DiarySelectInfo>) {
        val daysInMonth = getDaysInMonth(date)
        dayAdapter.setList(
            matchDiaryEntriesWithDays(
                diaryEntries,
                daysInMonth,
                year = dateEnglishOnlyYearFormat.format(date.time).toInt(),
                month = dateNumberOnlyMonthFormat.format(date.time).toInt()
            ).toMutableList()
        )
        diaryDates = diaryEntries.map { it.diaryEntryDate }.toSet()
        viewModel.setDiaryDates(diaryDates)
    }

    private fun updateSelectedDateInAdapter(selectedDate: String) {
        val updatedList = dayAdapter.currentList.toMutableList()
        updatedList.forEach { it.second?.isSelected = false }
        updatedList.find { it.first == selectedDate }?.second?.isSelected = true
        dayAdapter.setList(updatedList)
    }
}