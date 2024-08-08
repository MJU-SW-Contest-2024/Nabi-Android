package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.DialogNonDayDatePickerBinding
import com.nabi.nabi.databinding.FragmentSelectDateBinding
import com.nabi.nabi.extension.dialogResize
import com.nabi.nabi.utils.LoggerUtils
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
    private val viewModel: AddDiarySelectDateViewModel by viewModels()
    private val calendar = Calendar.getInstance()
    private var diaryDates: Set<String> = emptySet()
    private var tempDataExists: Boolean = false
    private var tempData: Pair<String, String>? = null
    private val minYear = 1950
    private val maxYear = Calendar.getInstance().get(Calendar.YEAR)

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
            val originalFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
            val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val originalDateStr = binding.tvSelectDate.text.toString()
            val date: Date? = originalFormat.parse(originalDateStr)
            val selectedDate = date?.let { targetFormat.format(it) }

            // 임시 저장된 일기 확인
            viewModel.checkTempData(selectedDate!!)
            if (tempDataExists && tempData != null) {
                val (date, content) = tempData!!
                LoggerUtils.d("$tempData, $tempDataExists")
                LoggerUtils.d("$date, $content")
                (requireActivity() as MainActivity).replaceFragment(
                    AddDiaryFragment(
                        true,
                        null,
                        content,
                        date,
                    ), true
                )
            }

            if (diaryDates.contains(selectedDate)) {
                showToast("이미 일기를 쓴 날이에요!")
            } else {
                (requireActivity() as MainActivity).replaceFragment(
                    AddDiaryFragment(
                        false,
                        null,
                        null,
                        originalDateStr,
                    ), true
                )
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

        binding.ivSelectYear.setOnClickListener {
            showNumberPickerDialog()
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
        viewModel.tempData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    tempDataExists = true
                    tempData = state.data
                }

                is UiState.Failure -> {
                    tempDataExists = false
                    tempData = null
                }
            }
        }
    }

    private fun showNumberPickerDialog() {
        val currentCalendar = Calendar.getInstance()
        val displayedMonthYear = binding.tvSelectYear.text.toString()
        val dateFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val displayedDate = dateFormat.parse(displayedMonthYear)
        currentCalendar.time = displayedDate!!

        val year = currentCalendar.get(Calendar.YEAR)
        val month = currentCalendar.get(Calendar.MONTH)

        val dialogBinding =
            DialogNonDayDatePickerBinding.inflate(LayoutInflater.from(requireContext()))

        dialogBinding.npYear.minValue = minYear
        dialogBinding.npYear.maxValue = maxYear
        dialogBinding.npYear.value = year

        dialogBinding.npMonth.minValue = 1
        dialogBinding.npMonth.maxValue = 12
        dialogBinding.npMonth.value = month + 1

        val builder = AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setView(dialogBinding.root)
            .setPositiveButton("확인") { _, _ ->
                val selectedYear = dialogBinding.npYear.value
                val selectedMonth = dialogBinding.npMonth.value - 1

//                val totalDisplayedMonths = (year * 12 + month)
//                val totalSelectedMonths = (selectedYear * 12 + selectedMonth)
//                val differenceInMonths = totalSelectedMonths - totalDisplayedMonths

//                val currentPosition = binding.vpCalendarMonth.currentItem
//                val newPosition = currentPosition + differenceInMonths
//                binding.vpCalendarMonth.setCurrentItem(newPosition, false)

                currentCalendar.set(selectedYear, selectedMonth, 1)
                updateMonthDisplay()
                viewModel.checkMonthDiary(selectedYear, selectedMonth + 1)
                binding.tvSelectDate.text =
                    SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN).format(currentCalendar.time)
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()

        builder.show()

        val positiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = builder.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton?.setTextAppearance(R.style.DialogButtonStyle)
        negativeButton?.setTextAppearance(R.style.DialogButtonStyle)

        builder.context.dialogResize(builder, 0.8f, 0.3f)
    }
}