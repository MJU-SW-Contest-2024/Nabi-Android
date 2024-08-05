package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSelectDateBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddDiarySelectDateFragment :
    BaseFragment<FragmentSelectDateBinding>(R.layout.fragment_select_date),
    AddDiaryMonthAdapter.OnDateSelectedListener {
    private lateinit var monthAdapter: AddDiaryMonthAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private val viewModel: AddDiaryViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private var diaryDates: Set<String> = emptySet()

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ivLeftMonth.setOnClickListener {
            updateMonth(-1)
            monthAdapter.updateCurrentMonth(-1)
        }
        binding.ivRightMonth.setOnClickListener {
            updateMonth(1)
            monthAdapter.updateCurrentMonth(1)
        }

        binding.btnDone.setOnClickListener {
            val selectedDate = changeDateFormat(binding.tvSelectDate.text.toString())

            if (diaryDates.contains(selectedDate)) {
                showToast("이미 일기를 쓴 날이에요!")
            } else {
                viewModel.selectDate(selectedDate)
                (requireActivity() as MainActivity).replaceFragment(AddDiaryFragment(), true)
            }
        }

        setTodayDate()
        updateMonthDisplay()
    }

    private fun updateMonth(monthChange: Int) {
        calendar.add(Calendar.MONTH, monthChange)
        viewModel.checkMonthDiary(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
        updateMonthDisplay()
    }

    private fun updateMonthDisplay() {
        // 월 영어로 표시
        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthFormat.format(calendar.time)
        // 연도 표시
        val yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val year = yearFormat.format(calendar.time)

        binding.tvSelectMonth.text = monthName
        binding.tvSelectYear.text = year
    }

    private fun setTodayDate() {
        val calendar = Calendar.getInstance()
        viewModel.checkMonthDiary(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)

        val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        val formattedDate = dateFormat.format(calendar.time)

        binding.tvSelectDate.text = formattedDate
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        val today = Calendar.getInstance().time
        monthAdapter = AddDiaryMonthAdapter(0, today)
        monthAdapter.setOnDateSelectedListener(this)


        monthListManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.apply {
            layoutManager = monthListManager
            adapter = monthAdapter
            scrollToPosition(Int.MAX_VALUE / 2)

            // 좌우 스크롤 막기
            setOnTouchListener { _, _ -> true }
        }
    }

    override fun onDateSelected(date: Date) {
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        val formattedDate = dateFormat.format(date)
        binding.tvSelectDate.text = formattedDate

        monthAdapter.updateSelectedDate(date)
    }

    override fun setObserver() {
        super.setObserver()
        viewModel.diaryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> LoggerUtils.e("diary 조회 실패: $state")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val dates = state.data.map { it.diaryEntryDate }
                    diaryDates = dates.toSet()
                    monthAdapter.updateDiaryDates(dates)
                }
            }
        }
    }

    private fun changeDateFormat(selectedDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val date = inputFormat.parse(selectedDate)
        return outputFormat.format(date!!)
    }
}