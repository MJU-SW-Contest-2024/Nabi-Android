package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.nabi.domain.model.diary.AddDiaryCallbackItem
import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.DialogNonDayDatePickerBinding
import com.nabi.nabi.databinding.FragmentSelectDateBinding
import com.nabi.nabi.extension.dialogResize
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddDiarySelectDateFragment :
    BaseFragment<FragmentSelectDateBinding>(R.layout.fragment_select_date) {
    private val sharedDateViewModel: SharedDateViewModel by activityViewModels()
    private val viewModel: AddDiarySelectDateViewModel by viewModels()
    private var diaryDates: Set<String> = emptySet()
    private var selectedDate: AddDiaryCallbackItem? = null
    private var tempDiary: DiaryDbEntity? = null
    private val minYear = 1950
    private val maxYear = Calendar.getInstance().get(Calendar.YEAR)
    private var tempDate: String? = ""

    private lateinit var calendarAdapter: AddDiaryMonthCalendarStateAdapter

    override fun onResume() {
        super.onResume()

        if (tempDate == "") {
            updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = dateFormat.parse(tempDate!!)
            val currentDate = SimpleDateFormat("yyyy년 M월 d일", Locale.ENGLISH).format(date!!)
            val currentYear = SimpleDateFormat("yyyy", Locale.ENGLISH).format(date)
            val currentMonth = SimpleDateFormat("MMMM", Locale.ENGLISH).format(date)
            binding.tvSelectDate.text = currentDate
            binding.tvSelectYear.text = currentYear
            binding.tvSelectMonth.text = currentMonth
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        calendarAdapter = AddDiaryMonthCalendarStateAdapter(requireActivity()).apply {
            setOnDateSelectedListener(object :
                AddDiaryMonthCalendarStateAdapter.OnDateSelectedListener {
                override fun onDateSelected(item: AddDiaryCallbackItem) {
                    selectedDate = item
                    updateSelectedDate(item.date)
                }
            })
        }

        binding.vpCalendarMonth.adapter = calendarAdapter
        binding.vpCalendarMonth.setCurrentItem(Int.MAX_VALUE / 2, false)
        binding.vpCalendarMonth.offscreenPageLimit = 1

        updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {

        binding.vpCalendarMonth.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                updateCurrentMonthText(position)
            }
        })

        binding.btnDone.setOnClickListener {
            if (selectedDate == null) {
                showToast("날짜를 선택해주세요")
            } else {
                if (!selectedDate!!.isClickable) {
                    showToast("이미 일기를 쓴 날이에요!")
                } else {
                    // 임시 일기 데이터 확인
                    viewModel.getTempDiary(selectedDate!!.date)
                    viewModel.getTempState.observe(viewLifecycleOwner) { state ->
                        when (state) {
                            is UiState.Loading -> {
                            }

                            is UiState.Success -> {
                                this.tempDiary = state.data

                                val fragment = AddDiaryFragment(
                                    false,
                                    null,
                                    tempDiary?.diaryTempContent,
                                    selectedDate!!.date,
                                )
                                this.tempDate = selectedDate!!.date
                                (requireActivity() as MainActivity).replaceFragment(fragment, true)
                            }

                            is UiState.Failure -> {
                                showToast("임시 저장 일기를 불러오는데 실패했습니다.")
                            }
                        }
                    }
                }
            }
        }

        binding.ivLeftMonth.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
            updateMonthYear(currentPos - 1)
            Calendar.getInstance().set(Calendar.DAY_OF_MONTH, 1)
            binding.vpCalendarMonth.setCurrentItem(currentPos - 1, false)

            // 날짜 선택 초기화
            sharedDateViewModel.clearSelectedDate()
            sharedDateViewModel.notifyMonthChanged()

            this.selectedDate = null
            updateSelectedDateToFirstDayOfMonth(currentPos - 1)
        }
        binding.ivRightMonth.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
            updateMonthYear(currentPos + 1)
            Calendar.getInstance().set(Calendar.DAY_OF_MONTH, 1)
            binding.vpCalendarMonth.setCurrentItem(currentPos + 1, false)

            sharedDateViewModel.clearSelectedDate()
            sharedDateViewModel.notifyMonthChanged()

            this.selectedDate = null
            updateSelectedDateToFirstDayOfMonth(currentPos + 1)
        }

        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ivSelectYear.setOnClickListener {
            showNumberPickerDialog()
        }
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
                }
            }
        }
    }

    private fun updateMonthYear(newPosition: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, newPosition - (Int.MAX_VALUE / 2))
        }

        val newYear = calendar.get(Calendar.YEAR)
        binding.tvSelectYear.text = newYear.toString()
        binding.tvSelectMonth.text = SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.time)
    }

    private fun updateCurrentMonthText(position: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }

        val currentDate = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN).format(calendar.time)
        val currentYear = SimpleDateFormat("yyyy", Locale.ENGLISH).format(calendar.time)
        val currentMonth = SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.time)
        binding.tvSelectDate.text = currentDate
        binding.tvSelectYear.text = currentYear
        binding.tvSelectMonth.text = currentMonth
    }

    private fun showNumberPickerDialog() {
        val currentCalendar = Calendar.getInstance()
        val displayedMonthYear = "${binding.tvSelectMonth.text} ${binding.tvSelectYear.text}"
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
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

                val totalDisplayedMonths = (year * 12 + month)
                val totalSelectedMonths = (selectedYear * 12 + selectedMonth)
                val differenceInMonths = totalSelectedMonths - totalDisplayedMonths

                val currentPosition = binding.vpCalendarMonth.currentItem
                val newPosition = currentPosition + differenceInMonths
                binding.vpCalendarMonth.setCurrentItem(newPosition, false)

                currentCalendar.set(selectedYear, selectedMonth, 1)
                binding.tvSelectYear.text =
                    SimpleDateFormat("yyyy", Locale.ENGLISH).format(currentCalendar.time)
                binding.tvSelectMonth.text =
                    SimpleDateFormat("MMMM", Locale.ENGLISH).format(currentCalendar.time)
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

    private fun updateSelectedDate(date: String) {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())
        val parsedDate = originalFormat.parse(date)
        val formattedDate = targetFormat.format(parsedDate!!)
        binding.tvSelectDate.text = formattedDate
    }

    private fun updateSelectedDateToFirstDayOfMonth(position: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val firstDayOfMonth =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        updateSelectedDate(firstDayOfMonth)
    }
}