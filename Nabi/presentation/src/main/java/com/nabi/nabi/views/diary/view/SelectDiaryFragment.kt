package com.nabi.nabi.views.diary.view

import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSelectDiaryBinding
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.diary.search.SearchDiaryFragment
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
class SelectDiaryFragment: BaseFragment<FragmentSelectDiaryBinding>(R.layout.fragment_select_diary) {
    private lateinit var calendarAdapter: SelectDiaryMonthCalendarStateAdapter

    override fun initView() {
        calendarAdapter = SelectDiaryMonthCalendarStateAdapter(requireActivity())
        binding.vpCalendarMonth.adapter = calendarAdapter
        binding.vpCalendarMonth.setCurrentItem(Int.MAX_VALUE / 2, false)
        binding.vpCalendarMonth.offscreenPageLimit = 1

        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(Date())
        binding.tvCurrentMonth.text = currentMonth
    }

    override fun initListener() {
        super.initListener()

        binding.vpCalendarMonth.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateCurrentMonthText(position)
            }
        })

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
    }

    private fun updateCurrentMonthText(position: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, position - (Int.MAX_VALUE / 2))
        }
        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(calendar.time)
        binding.tvCurrentMonth.text = currentMonth
    }

    private fun createEmotionTooltip(text: String): Balloon{
        val balloon = createBalloon(context = requireContext()){
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


        return balloon
    }

}