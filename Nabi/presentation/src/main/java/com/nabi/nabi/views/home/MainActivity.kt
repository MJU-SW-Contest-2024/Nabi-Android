package com.nabi.nabi.views.home

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.databinding.ActivityMainBinding
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var mainRvAdapter: MainRvAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun initView() {
        viewModel.fetchData()

        setDiaryRv()
        initListener()
        setObserver()
    }

    private fun setDiaryRv() {
        mainRvAdapter = MainRvAdapter()

        binding.rvDiary.apply {
            adapter = mainRvAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
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