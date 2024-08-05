package com.nabi.nabi.views.diary.add

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentAddDiaryBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddDiaryFragment : BaseFragment<FragmentAddDiaryBinding>(R.layout.fragment_add_diary) {
    private val viewModel: AddDiaryViewModel by activityViewModels()
    override fun initView() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            binding.tvDiaryDate.text = date.toString()
        }
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(AddDiarySelectDateFragment(), false)
        }

        binding.ibMic.setOnClickListener {
            // mic 버튼 눌렀을 때 로직
        }

        binding.btnAddDiary.setOnClickListener {
            val content = binding.etDiary.text.toString().trim()
            val selectedDate = viewModel.selectedDate.value.orEmpty()

            if (content.isNotEmpty()) {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val date = LocalDate.parse(selectedDate, inputFormatter)
                val diaryEntryDate = date.format(outputFormatter)
                LoggerUtils.d(diaryEntryDate)
                viewModel.addDiary(content, diaryEntryDate)
            } else {
                showToast("일기 내용을 입력해주세요")
            }
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.addState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 추가 실패: ${it.message}")
                }

                is UiState.Success -> {
                    showToast("일기 추가 성공")
                    binding.etDiary.text.clear()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 수정 실패")
                }

                is UiState.Success -> {}
            }
        }
    }
}