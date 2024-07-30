package com.nabi.nabi.views.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentHomeBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.addDiary.SelectDateFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var mainRvAdapter: MainRvAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun initView() {
        viewModel.fetchData()
        setDiaryRv()
    }

    private fun setDiaryRv() {
        mainRvAdapter = MainRvAdapter()

        binding.rvDiary.apply {
            adapter = mainRvAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnAddDiary.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.fl_main, SelectDateFragment())
            ft.addToBackStack("home")
            ft.commit()
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.homeState.observe(this) { it ->
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("메인페이지 데이터 불러오기 실패: ${it.message}")
                }

                is UiState.Success -> {
                    val diaryList = it.data.recentFiveDiaries
                    mainRvAdapter.setData(diaryList)
                    binding.tvNickname.text = "${it.data.nickname} 님"
                    binding.tvDiaryDay.text = "일기 작성 ${it.data.consecutiveWritingDays}일 째"
                }
            }
        }
    }
}