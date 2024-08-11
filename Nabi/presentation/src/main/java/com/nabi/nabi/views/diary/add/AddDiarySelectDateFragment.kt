package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.domain.model.diary.DiarySelectInfo
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.DialogNonDayDatePickerBinding
import com.nabi.nabi.databinding.FragmentSelectDateBinding
import com.nabi.nabi.extension.dialogResize
import com.nabi.nabi.utils.Constants.dateDashFormat
import com.nabi.nabi.utils.Constants.dateEnglishOnlyMonthFormat
import com.nabi.nabi.utils.Constants.dateEnglishOnlyYearFormat
import com.nabi.nabi.utils.Constants.dateKoreanFormat
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddDiarySelectDateFragment :
    BaseFragment<FragmentSelectDateBinding>(R.layout.fragment_select_date) {

    private val viewModel: AddDiarySelectDateViewModel by viewModels()
    private val sharedViewModel: SharedDateViewModel by activityViewModels()

    private lateinit var calendarAdapter: AddDiaryMonthCalendarStateAdapter

    private var diaryDates: Set<String> = emptySet()
    private lateinit var selectedDate: DiarySelectInfo
    private var tempDiary: DiaryDbEntity? = null
    private var tempDate: String? = ""
    private val minYear = 1950
    private val maxYear = Calendar.getInstance().get(Calendar.YEAR)

    override fun onResume() {
        super.onResume()
        updateDisplayedDate()
    }

    override fun initView() {
        (requireActivity() as MainActivity).setStatusBarColor(R.color.primary, true)

        sharedViewModel.selectedDate.value?.let {
            if (it.isEmpty()) {
                sharedViewModel.changeSelectedDate(dateDashFormat.format(Calendar.getInstance().time))
            }
        }

        selectedDate = DiarySelectInfo(false, sharedViewModel.selectedDate.value!!, true)
        LoggerUtils.d(selectedDate.toString())

        calendarAdapter = AddDiaryMonthCalendarStateAdapter(requireActivity()).apply {
            setOnDateSelectedListener(object :
                AddDiaryMonthCalendarStateAdapter.OnDateSelectedListener {
                override fun onDateSelected(item: DiarySelectInfo) {
                    selectedDate = item
                    updateSelectedDate(item.diaryEntryDate)
                }
            })
        }

        binding.apply {
            vpCalendarMonth.adapter = calendarAdapter
            vpCalendarMonth.setCurrentItem(Int.MAX_VALUE / 2, false)
            vpCalendarMonth.offscreenPageLimit = 1
        }

        updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.apply {
            btnDone.setOnClickListener { onDoneButtonClick() }
            ivLeftMonth.setOnClickListener { updateCalendarMonth(-1) }
            ivRightMonth.setOnClickListener { updateCalendarMonth(1) }
            ivBack.setOnClickListener { onBackButtonClick() }
            ivSelectYear.setOnClickListener { showNumberPickerDialog() }
        }
    }

    override fun setObserver() {
        super.setObserver()
        viewModel.diaryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> LoggerUtils.e("diary 조회 실패: $state")
                is UiState.Success -> {
                    diaryDates = state.data.map { it.diaryEntryDate }.toSet()
                }

                else -> Unit
            }
        }
    }

    private fun updateDisplayedDate() {
        if (tempDate.isNullOrEmpty()) {
            updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
        } else {
            val date = dateDashFormat.parse(tempDate!!)
            binding.apply {
                tvSelectDate.text = dateKoreanFormat.format(date!!)
                tvSelectYear.text = dateEnglishOnlyYearFormat.format(date)
                tvSelectMonth.text = dateEnglishOnlyMonthFormat.format(date)
            }
        }
    }

    private fun onDoneButtonClick() {
        if (sharedViewModel.selectedDate.value.isNullOrEmpty()) {
            showToast("날짜를 선택해주세요")
        } else if (selectedDate.existDiary) {
            showToast("이미 일기를 쓴 날이에요!")
        } else {
            viewModel.getTempDiary(selectedDate.diaryEntryDate)
            viewModel.getTempState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Success -> {
                        tempDiary = state.data
                        tempDate = selectedDate.diaryEntryDate
                        navigateToAddDiaryFragment()
                    }

                    is UiState.Failure -> showToast("임시 저장 일기를 불러오는데 실패했습니다.")
                    else -> Unit
                }
            }
        }
    }

    private fun navigateToAddDiaryFragment() {
        val fragment = AddDiaryFragment(
            false,
            null,
            tempDiary?.diaryTempContent,
            selectedDate.diaryEntryDate
        )
        (requireActivity() as MainActivity).replaceFragment(fragment, true)
    }

    private fun updateCalendarMonth(monthDifference: Int) {
        val currentPos = binding.vpCalendarMonth.currentItem
        binding.vpCalendarMonth.setCurrentItem(currentPos + monthDifference, false)
        updateMonthYear(currentPos + monthDifference)
    }

    private fun onBackButtonClick() {
        sharedViewModel.clearData()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun showNumberPickerDialog() {
        val currentCalendar = Calendar.getInstance()
        val displayedMonthYear = "${binding.tvSelectMonth.text} ${binding.tvSelectYear.text}"
        val displayedDate = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).parse(displayedMonthYear)
        currentCalendar.time = displayedDate!!

        val dialogBinding =
            DialogNonDayDatePickerBinding.inflate(LayoutInflater.from(requireContext()))

        dialogBinding.apply {
            npYear.minValue = minYear
            npYear.maxValue = maxYear
            npYear.value = currentCalendar.get(Calendar.YEAR)

            npMonth.minValue = 1
            npMonth.maxValue = 12
            npMonth.value = currentCalendar.get(Calendar.MONTH) + 1
        }

        AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setView(dialogBinding.root)
            .setPositiveButton("확인") { _, _ ->
                updateCalendarPositionByPicker(
                    currentCalendar,
                    dialogBinding.npYear.value,
                    dialogBinding.npMonth.value - 1
                )
            }
            .setNegativeButton("취소", null)
            .create()
            .apply {
                show()
                context.dialogResize(this, 0.8f, 0.3f)
                getButton(AlertDialog.BUTTON_POSITIVE).setTextAppearance(R.style.DialogButtonStyle)
                getButton(AlertDialog.BUTTON_NEGATIVE).setTextAppearance(R.style.DialogButtonStyle)
            }
    }

    private fun updateCalendarPositionByPicker(
        calendar: Calendar,
        selectedYear: Int,
        selectedMonth: Int
    ) {
        val totalDisplayedMonths = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH)
        val totalSelectedMonths = selectedYear * 12 + selectedMonth
        val differenceInMonths = totalSelectedMonths - totalDisplayedMonths

        val currentPosition = binding.vpCalendarMonth.currentItem
        binding.vpCalendarMonth.setCurrentItem(currentPosition + differenceInMonths, false)

        calendar.set(selectedYear, selectedMonth, 1)
        binding.tvSelectYear.text = dateEnglishOnlyYearFormat.format(calendar.time)
        binding.tvSelectMonth.text = dateEnglishOnlyMonthFormat.format(calendar.time)
    }

    private fun updateCurrentMonthText(position: Int) {
        val calendar = getCalendarForPosition(position)
        binding.apply {
            tvSelectDate.text = dateKoreanFormat.format(calendar.time)
            tvSelectYear.text = dateEnglishOnlyYearFormat.format(calendar.time)
            tvSelectMonth.text = dateEnglishOnlyMonthFormat.format(calendar.time)
        }
    }

    private fun getCalendarForPosition(position: Int): Calendar {
        return Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
    }

    private fun updateSelectedDate(date: String) {
        val parsedDate = dateDashFormat.parse(date)
        binding.tvSelectDate.text = dateKoreanFormat.format(parsedDate!!)
    }

    private fun updateMonthYear(newPosition: Int) {
        val calendar = getCalendarForPosition(newPosition)
        binding.tvSelectYear.text = calendar.get(Calendar.YEAR).toString()
        binding.tvSelectMonth.text = dateEnglishOnlyMonthFormat.format(calendar.time)
    }
}
