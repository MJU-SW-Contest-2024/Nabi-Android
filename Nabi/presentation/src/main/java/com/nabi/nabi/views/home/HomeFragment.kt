package com.nabi.nabi.views.home

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.domain.model.home.RecentFiveDiary
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.base.NabiApplication.Companion.consecutiveDay
import com.nabi.nabi.base.NabiApplication.Companion.nickname
import com.nabi.nabi.databinding.FragmentHomeBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.OnRvItemClickListener
import com.nabi.nabi.views.chat.ChatFragment
import com.nabi.nabi.views.diary.add.AddDiarySelectDateFragment
import com.nabi.nabi.views.diary.detail.DetailDiaryFragment
import com.nabi.nabi.views.diary.view.SelectDiaryFragment
import com.nabi.nabi.views.myPage.MyPageFragment
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var homeRvAdapter: HomeRvAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun initView() {
        (requireActivity() as MainActivity).setStatusBarColor(R.color.white, false)

        binding.tvNickname.text = "$nickname 님"
        viewModel.registerFcmToken()
        viewModel.fetchData()
        setDiaryRv()
    }

    private fun setDiaryRv() {
        homeRvAdapter = HomeRvAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<Int> {
                override fun onClick(item: Int) {
                    (requireActivity() as MainActivity).replaceFragment(DetailDiaryFragment(item), true)
                }
            })
            setRvItemBookmarkClickListener(object : OnRvItemClickListener<RecentFiveDiary> {
                override fun onClick(item: RecentFiveDiary) {
                    if (getItemBinding(item.diaryId)?.isBookmarked() == true)
                        viewModel.deleteBookmark(item.diaryId)
                    else viewModel.addBookmark(item.diaryId)
                }
            })
        }

        binding.rvDiary.apply {
            adapter = homeRvAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.ibNotification.setOnClickListener {

        }

        binding.ibMypage.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.fl_main, MyPageFragment())
            ft.addToBackStack("myPage")
            ft.commit()
        }

        binding.btnAddDiary.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.fl_main, AddDiarySelectDateFragment())
            ft.addToBackStack("home")
            ft.commit()
        }

        binding.ivDetailDiary.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.fl_main, SelectDiaryFragment())
            ft.addToBackStack(null)
            ft.commit()
        }

        binding.btnTalk.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(ChatFragment(), true)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.homeState.observe(this) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("메인페이지 데이터 불러오기 실패: ${it.message}")
                }

                is UiState.Success -> {
                    val diaryList = it.data.recentFiveDiaries
                    homeRvAdapter.setData(diaryList)
                    consecutiveDay = it.data.consecutiveWritingDays
                    binding.tvDiaryDay.text = "일기 작성 ${consecutiveDay}일 째"
                    nickname = it.data.nickname
                    binding.tvNickname.text = "${it.data.nickname} 님"
                }
            }
        }

        viewModel.addState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("북마크 추가 실패")
                }

                is UiState.Success -> {
                    setRecentDiaryBookmark(it.data, true)
                }
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("북마크 삭제 실패")
                }

                is UiState.Success -> {
                    setRecentDiaryBookmark(it.data, false)
                }
            }
        }
    }

    private fun setRecentDiaryBookmark(diaryId: Int, isBookmarked: Boolean) {
        getItemBinding(diaryId)?.changeImageBookmark(isBookmarked)
    }

    private fun getItemBinding(diaryId: Int): HomeRvAdapter.ActivityViewHolder? {
        val adapterPosition = homeRvAdapter.dataList.indexOfFirst { it.diaryId == diaryId }
        return if (adapterPosition != -1) {
            val viewHolder = binding.rvDiary.findViewHolderForAdapterPosition(adapterPosition)
            (viewHolder as HomeRvAdapter.ActivityViewHolder)
        } else {
            null
        }
    }
}