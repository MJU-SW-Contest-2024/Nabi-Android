package com.nabi.nabi.views.diary.statistics

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.nabi.domain.model.emotion.EmotionStatistics
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentStatisticsDiaryBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.diary.emotion.EmotionSearchFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DiaryStatisticsFragment: BaseFragment<FragmentStatisticsDiaryBinding>(R.layout.fragment_statistics_diary) {
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private val viewmodel: DiaryStatisticsViewModel by viewModels()

    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            endDate.time = calendar.time
            calendar.add(Calendar.MONTH, -1)
            startDate.time = calendar.time
        }
    }

    override fun initView() {
        binding.tvEndDate.text = dateFormat.format(endDate.time)
        binding.tvStartDate.text = dateFormat.format(startDate.time)

        fetchData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("startDate", startDate.timeInMillis)
        outState.putLong("endDate", endDate.timeInMillis)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            startDate.timeInMillis = it.getLong("startDate")
            endDate.timeInMillis = it.getLong("endDate")
            binding.tvStartDate.text = dateFormat.format(startDate.time)
            binding.tvEndDate.text = dateFormat.format(endDate.time)
            fetchData()
        }
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvStartDate.setOnClickListener { showDatePickerDialog(true) }
        binding.tvEndDate.setOnClickListener { showDatePickerDialog(false) }

        binding.ivEmotionAnger.setOnClickListener { (requireActivity() as MainActivity).replaceFragment(EmotionSearchFragment("화남"), true)}
        binding.ivEmotionHappiness.setOnClickListener { (requireActivity() as MainActivity).replaceFragment(EmotionSearchFragment("행복"), true)}
        binding.ivEmotionBoredom.setOnClickListener { (requireActivity() as MainActivity).replaceFragment(EmotionSearchFragment("지루함"), true)}
        binding.ivEmotionSadness.setOnClickListener { (requireActivity() as MainActivity).replaceFragment(EmotionSearchFragment("우울"), true)}
        binding.ivEmotionAnxiety.setOnClickListener { (requireActivity() as MainActivity).replaceFragment(EmotionSearchFragment("불안"), true)}
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val initialCalendar = if (isStartDate) startDate.clone() as Calendar else endDate.clone() as Calendar

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val selectedDate = calendar.time

            if (isStartDate) {
                startDate.time = selectedDate
                binding.tvStartDate.text = dateFormat.format(selectedDate)
                if (startDate.after(endDate)) {
                    endDate.time = startDate.time
                    binding.tvEndDate.text = dateFormat.format(endDate.time)
                }
            } else {
                endDate.time = selectedDate
                binding.tvEndDate.text = dateFormat.format(selectedDate)
                if (endDate.before(startDate)) {
                    startDate.time = endDate.time
                    binding.tvStartDate.text = dateFormat.format(startDate.time)
                }
            }

            fetchData()
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            dateSetListener,
            initialCalendar.get(Calendar.YEAR),
            initialCalendar.get(Calendar.MONTH),
            initialCalendar.get(Calendar.DAY_OF_MONTH)
        )

        val maxDate = System.currentTimeMillis()
        val minDate = if (isStartDate) endDate.timeInMillis else startDate.timeInMillis

        if (minDate > maxDate) {
            datePickerDialog.datePicker.minDate = maxDate
            datePickerDialog.datePicker.maxDate = maxDate
        } else {
            if (isStartDate) {
                datePickerDialog.datePicker.minDate = 0
                datePickerDialog.datePicker.maxDate = endDate.timeInMillis
            } else {
                datePickerDialog.datePicker.minDate = startDate.timeInMillis
                datePickerDialog.datePicker.maxDate = maxDate
            }
        }

        datePickerDialog.show()
    }

    private fun fetchData() {
        viewmodel.fetchData(
            dateFormat.format(startDate.time).replace(".", "-"),
            dateFormat.format(endDate.time).replace(".", "-")
        )
    }

    private fun setupChart(p: EmotionStatistics) {
        binding.barChart.setValues(
            p.run { intArrayOf(angerCount, happinessCount, boringCount, depressionCount, anxietyCount) }
        )

        binding.tvEmotionAngerValue.text = p.angerCount.toString()
        binding.tvEmotionHappinessValue.text = p.happinessCount.toString()
        binding.tvEmotionBoredomValue.text = p.boringCount.toString()
        binding.tvEmotionSadnessValue.text = p.depressionCount.toString()
        binding.tvEmotionAnxietyValue.text = p.anxietyCount.toString()
    }

    override fun setObserver() {
        super.setObserver()

        viewmodel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("감정 통계 로드 실패")
                    setupChart(EmotionStatistics(0, 0, 0, 0, 0))
                }
                is UiState.Success -> {
                    setupChart(it.data)
                }
            }
        }
    }
}
