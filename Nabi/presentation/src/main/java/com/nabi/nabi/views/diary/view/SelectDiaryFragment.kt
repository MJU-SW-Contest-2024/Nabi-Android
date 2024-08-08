package com.nabi.nabi.views.diary.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.DialogNonDayDatePickerBinding
import com.nabi.nabi.databinding.FragmentSelectDiaryBinding
import com.nabi.nabi.extension.dialogResize
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.diary.search.SearchDiaryFragment
import com.nabi.nabi.views.diary.statistics.DiaryStatisticsFragment
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SelectDiaryFragment : BaseFragment<FragmentSelectDiaryBinding>(R.layout.fragment_select_diary) {
    private lateinit var calendarAdapter: SelectDiaryMonthCalendarStateAdapter

    private val minYear = 1950
    private val maxYear = Calendar.getInstance().get(Calendar.YEAR)

    override fun initView() {
        calendarAdapter = SelectDiaryMonthCalendarStateAdapter(requireActivity())
        binding.vpCalendarMonth.adapter = calendarAdapter
        binding.vpCalendarMonth.setCurrentItem(Int.MAX_VALUE / 2, false)
        binding.vpCalendarMonth.offscreenPageLimit = 1

        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(Date())
        binding.tvCurrentMonth.text = currentMonth
    }

    override fun onResume() {
        super.onResume()

        updateCurrentMonthText(binding.vpCalendarMonth.currentItem)
    }

    override fun initListener() {
        super.initListener()

        binding.vpCalendarMonth.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateCurrentMonthText(position)
            }
        })

        binding.btnMoveStatistics.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(DiaryStatisticsFragment(), true)
        }

        binding.ibSearch.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(SearchDiaryFragment(), true)
        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ibPreviousMonth.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
            binding.vpCalendarMonth.setCurrentItem(currentPos - 1, false)
        }

        binding.ibNextMonth.setOnClickListener {
            val currentPos = binding.vpCalendarMonth.currentItem
            binding.vpCalendarMonth.setCurrentItem(currentPos + 1, false)
        }

        binding.ivEmotionAnger.setOnClickListener { createEmotionTooltip("화나").showAlignTop(binding.ivEmotionAnger) }
        binding.ivEmotionHappiness.setOnClickListener { createEmotionTooltip("행복해").showAlignTop(binding.ivEmotionHappiness) }
        binding.ivEmotionBoredom.setOnClickListener { createEmotionTooltip("지루해").showAlignTop(binding.ivEmotionBoredom) }
        binding.ivEmotionSadness.setOnClickListener { createEmotionTooltip("슬퍼").showAlignTop(binding.ivEmotionSadness) }
        binding.ivEmotionAnxiety.setOnClickListener { createEmotionTooltip("불안해").showAlignTop(binding.ivEmotionAnxiety) }

        binding.tvCurrentMonth.setOnClickListener {
            showNumberPickerDialog()
        }
    }

    private fun updateCurrentMonthText(position: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(calendar.time)
        binding.tvCurrentMonth.text = currentMonth
    }

    private fun createEmotionTooltip(text: String): Balloon {
        return createBalloon(context = requireContext()) {
            setHeight(42)
            setWidth(BalloonSizeSpec.WRAP)

            setText(text)
            setTextSize(12f)
            setTextColorResource(R.color.white)
            setTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.pretendard_semi_bold)!!)

            setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
            setArrowSize(10)
            setArrowPosition(0.5f)

            setPaddingHorizontal(8)

            setCornerRadius(5f)
            setBackgroundColorResource(R.color.black)
            setBalloonAnimation(BalloonAnimation.FADE)

            setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)

            setLifecycleOwner(viewLifecycleOwner)
            build()
        }
    }

    private fun showNumberPickerDialog() {
        val currentCalendar = Calendar.getInstance()
        val displayedMonthYear = binding.tvCurrentMonth.text.toString()
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
        val displayedDate = dateFormat.parse(displayedMonthYear)
        currentCalendar.time = displayedDate!!

        val year = currentCalendar.get(Calendar.YEAR)
        val month = currentCalendar.get(Calendar.MONTH)

        val dialogBinding = DialogNonDayDatePickerBinding.inflate(LayoutInflater.from(requireContext()))

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
                binding.tvCurrentMonth.text = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(currentCalendar.time)
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