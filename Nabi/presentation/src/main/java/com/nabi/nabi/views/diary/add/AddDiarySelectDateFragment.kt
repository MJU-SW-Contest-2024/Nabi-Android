package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.data.room.DiaryDatabase
import com.nabi.data.room.DiaryEntity
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
import kotlinx.coroutines.launch
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
    private var selectedDate: String? = null
    private val minYear = 1950
    private val maxYear = Calendar.getInstance().get(Calendar.YEAR)
    private lateinit var db: DiaryDatabase
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onResume() {
        super.onResume()

        viewModel.selectedDate.value?.let { selectedDateStr ->
            val nowMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date = dateFormat.parse(selectedDateStr) ?: return

            val calendar = Calendar.getInstance().apply {
                time = date
            }
            val koreanDateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
            binding.tvSelectDate.text = koreanDateFormat.format(calendar.time)

            val yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
            val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
            val selectedYear = yearFormat.format(calendar.time)
            val selectedMonth = monthFormat.format(calendar.time)
            val nowYear = yearFormat.format(Calendar.getInstance().time)
            val monthNumber = calendar.get(Calendar.MONTH) + 1

            binding.tvSelectYear.text = selectedYear
            binding.tvSelectMonth.text = selectedMonth

            val nowMonths = (nowYear.toInt() * 12 + nowMonth)
            val selectedMonths = (selectedYear.toInt() * 12 + monthNumber)
            val differenceMonths = selectedMonths - nowMonths
            monthAdapter.updateCurrentMonth(differenceMonths)

            viewModel.checkMonthDiary(selectedYear.toInt(), monthNumber)
        } ?: run {
            setTodayDate()
            updateMonthDisplay()
        }
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//
//        arguments?.let {
//            val nowMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
//            selectedDate = it.getString("SELECTED_DATE")
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
//            val date = dateFormat.parse(selectedDate!!) ?: return
//
//            val calendar = Calendar.getInstance().apply {
//                time = date
//            }
//
//            val koreanDateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
//            binding.tvSelectDate.text = koreanDateFormat.format(calendar.time)
//
//            val yearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
//            val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
//            val selectedYear = yearFormat.format(calendar.time)
//            val selectedMonth = monthFormat.format(calendar.time)
//            val nowYear = yearFormat.format(Calendar.getInstance().time)
//            val monthNumber = calendar.get(Calendar.MONTH) + 1
//
//            binding.tvSelectYear.text = selectedYear
//            binding.tvSelectMonth.text = selectedMonth
//
//            val nowMonths = (nowYear.toInt() * 12 + nowMonth)
//            val selectedMonths = (selectedYear.toInt() * 12 + monthNumber)
//            val differenceMonths = selectedMonths - nowMonths
//            monthAdapter.updateCurrentMonth(differenceMonths)
//
//            viewModel.checkMonthDiary(selectedYear.toInt(), monthNumber)
//        }
//    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        db = DiaryDatabase.getInstance(requireContext())

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
            selectedDate = date?.let { targetFormat.format(it) }
            arguments = Bundle().apply {
                putString("SELECTED_DATE", selectedDate)
            }
            viewModel.selectedDate.value = selectedDate


            var existingDiary: DiaryEntity?

            if (diaryDates.contains(selectedDate)) {
                showToast("이미 일기를 쓴 날이에요!")
            } else {
                // 임시 저장된 일기 확인
                coroutineScope.launch {
                    try {
                        existingDiary = db.getDiaryDao().getDiaryByDate(selectedDate!!)


                        if (existingDiary == null) {
                            val fragment = AddDiaryFragment(
                                false,
                                null,
                                null,
                                originalDateStr,
                            )
                            (requireActivity() as MainActivity).replaceFragment(fragment, true)
                        } else {
                            val fragment = AddDiaryFragment(
                                false,
                                null,
                                existingDiary!!.diaryTempContent,
                                originalDateStr,
                            )
                            (requireActivity() as MainActivity).replaceFragment(fragment, true)
                        }
                    } catch (e: Exception) {
                        LoggerUtils.e("임시 일기 확인 실패: ${e.message}")
                    }
                }
            }
        }
    }

    private fun updateMonth(monthChange: Int) {
        if (selectedDate != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date = dateFormat.parse(selectedDate!!)
            calendar.time = date!!
        }

        calendar.add(Calendar.MONTH, monthChange)
        viewModel.checkMonthDiary(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
        updateMonthDisplay()

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(calendar.time)

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

        val dateKoreanFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val formattedDate = dateKoreanFormat.format(calendar.time)

        binding.tvSelectDate.text = formattedDate
        selectedDate = dateFormat.format(calendar.time)
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
            showNumberPickerDialog(selectedDate!!)
        }
    }

    override fun onDateSelected(date: Date) {
        val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        val formattedDate = dateFormat.format(date)
        binding.tvSelectDate.text = formattedDate

        monthAdapter.updateSelectedDate(date)
        selectedDate = formattedDate
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

    private fun showNumberPickerDialog(selectedDate: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val date = dateFormat.parse(selectedDate)
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = date!!
        val year = currentCalendar.get(Calendar.YEAR)
        val month = currentCalendar.get(Calendar.MONTH) + 1

        val dialogBinding =
            DialogNonDayDatePickerBinding.inflate(LayoutInflater.from(requireContext()))

        dialogBinding.npYear.minValue = minYear
        dialogBinding.npYear.maxValue = maxYear
        dialogBinding.npYear.value = year

        dialogBinding.npMonth.minValue = 1
        dialogBinding.npMonth.maxValue = 12
        dialogBinding.npMonth.value = month

        val builder = AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setView(dialogBinding.root)
            .setPositiveButton("확인") { _, _ ->
                val selectedYear = dialogBinding.npYear.value
                val selectedMonth = dialogBinding.npMonth.value - 1

                val nowMonths = (year * 12 + month)
                val selectedMonths = (selectedYear * 12 + selectedMonth)
                val differenceMonths = selectedMonths - nowMonths
                monthAdapter.updateCurrentMonth(differenceMonths + 1)

                currentCalendar.set(selectedYear, selectedMonth, 1)
                viewModel.checkMonthDiary(selectedYear, selectedMonth + 1)

                binding.tvSelectYear.text =
                    SimpleDateFormat("yyyy", Locale.ENGLISH).format(currentCalendar.time)
                binding.tvSelectMonth.text =
                    SimpleDateFormat("MMMM", Locale.ENGLISH).format(currentCalendar.time)
                binding.tvSelectDate.text =
                    SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN).format(currentCalendar.time)

                this.selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(currentCalendar.time)
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