package com.nabi.nabi.views.diary.detail

import android.view.View
import androidx.fragment.app.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentDetailDiaryBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.diary.add.AddDiaryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailDiaryFragment(
    private val diaryId: Int
) : BaseFragment<FragmentDetailDiaryBinding>(R.layout.fragment_detail_diary) {
    private val viewModel: DetailDiaryViewModel by viewModels()

    override fun initView() {
        viewModel.fetchData(diaryId)
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnEdit.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(
                AddDiaryFragment(
                    true,
                    diaryId,
                    binding.tvDiaryContent.text.toString(),
                    binding.tvDiaryDate.text.toString()
                ), true
            )
        }

        binding.ibBookmark.setOnClickListener {
            if (viewModel.isBookmarked.value!!) viewModel.deleteBookmark(diaryId)
            else viewModel.addBookmark(diaryId)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.addState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("북마크 추가 실패")
                }

                is UiState.Success -> {}
            }
        }

        viewModel.deleteState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("북마크 삭제 실패")
                }

                is UiState.Success -> {}
            }
        }

        viewModel.isBookmarked.observe(viewLifecycleOwner) {
            binding.ibBookmark.setImageResource(
                if (it) R.drawable.ic_heart_filled else R.drawable.ic_heart
            )
        }


        viewModel.diaryState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {}
                is UiState.Success -> {
                    binding.tvDiaryDate.text = formatDate(it.data.diaryEntryDate)
                    binding.tvDiaryContent.text = it.data.content

                    val resourceId = when (it.data.emotion) {
                        "행복" -> R.drawable.img_happiness
                        "우울" -> R.drawable.img_sadness
                        "화남" -> R.drawable.img_anger
                        "불안" -> R.drawable.img_anxiety
                        "지루" -> R.drawable.img_boredom
                        else -> R.drawable.shape_circle
                    }
                    binding.ivEmotion.setImageResource(resourceId)
                    if (resourceId == R.drawable.shape_circle) binding.ivEmotion.visibility =
                        View.GONE

                    binding.ibBookmark.setImageResource(
                        if (it.data.isBookmarked) R.drawable.ic_heart_filled else R.drawable.ic_heart
                    )
                }
            }
        }
    }

    private fun formatDate(inputDate: String): String {
        val parts = inputDate.split("-")
        val year = parts[0]
        val month = parts[1].toInt()
        val day = parts[2].toInt()

        return "${year}년 ${month}월 ${day}일"
    }
}