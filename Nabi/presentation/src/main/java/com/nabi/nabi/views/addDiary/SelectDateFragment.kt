package com.nabi.nabi.views.addDiary

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSelectDateBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class SelectDateFragment : BaseFragment<FragmentSelectDateBinding>(R.layout.fragment_select_date) {
    private lateinit var monthAdapter: MonthAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private val calendar = Calendar.getInstance()

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ivLeftMonth.setOnClickListener {
            monthAdapter.updateCurrentMonth(-1)
            updateMonth(-1)
        }
        binding.ivRightMonth.setOnClickListener {
            monthAdapter.updateCurrentMonth(1)
            updateMonth(1)
        }

        setTodayDate()
        updateMonthDisplay()
    }

    private fun updateMonth(monthChange: Int) {
        calendar.add(Calendar.MONTH, monthChange)
        updateMonthDisplay()
    }

    private fun updateMonthDisplay() {
        // 월 영어로 표시
        val monthFormat =
            SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthFormat.format(calendar.time)

        binding.tvSelectMonth.text = monthName
    }

    private fun setTodayDate() {
        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        val formattedDate = dateFormat.format(calendar.time)

        binding.tvTodayDate.text = formattedDate
    }

    override fun initListener() {
        monthAdapter = MonthAdapter(0)
        monthListManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.apply {
            layoutManager = monthListManager
            adapter = monthAdapter
            scrollToPosition(Int.MAX_VALUE / 2)
        }
    }

}