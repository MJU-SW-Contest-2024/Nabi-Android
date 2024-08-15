package com.nabi.nabi.views.diary.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.databinding.DialogAddDiaryDoneBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.diary.detail.DetailDiaryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmotionLoadingDialog(private val isEdit: Boolean, private val diaryId: Int) : DialogFragment() {
    private val viewModel: EmotionLoadingViewModel by viewModels()
    private lateinit var emotionLoadingDialog: EmotionLoadingDialog
    private var currentToast: Toast? = null
    private lateinit var binding: DialogAddDiaryDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog_fullscreen)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddDiaryDoneBinding.inflate(inflater, container, false)
        emotionLoadingDialog = EmotionLoadingDialog(isEdit, diaryId)

        setObserver()
        viewModel.getDiaryEmotion(diaryId)
        return binding.root
    }

    fun setObserver() {
        viewModel.getEmotionState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 감정분석 실패")
                    (requireActivity() as MainActivity).replaceFragment(
                        DetailDiaryFragment(diaryId, "EmotionLoadingDialog"),
                        false
                    )
                    dismiss()
                }

                is UiState.Success -> {
                    val emotionState = it.data
                    viewModel.addDiaryEmotion(diaryId, emotionState)
                }
            }
        }

        viewModel.addEmotionState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기에 감정 추가 실패")
                    requireActivity().supportFragmentManager.popBackStack()
                }

                is UiState.Success -> {
                    if (isEdit) {
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        (requireActivity() as MainActivity).replaceFragment(
                            DetailDiaryFragment(diaryId, "EmotionLoadingDialog"),
                            false
                        )
                    }
                    dismiss()
                }
            }
        }
    }

    private fun showToast(msg: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}