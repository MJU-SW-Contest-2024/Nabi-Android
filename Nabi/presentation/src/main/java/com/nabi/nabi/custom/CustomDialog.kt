package com.nabi.nabi.custom

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.nabi.nabi.R
import com.nabi.nabi.databinding.CustomDialogBinding
import com.nabi.nabi.extension.dialogResize
import com.nabi.nabi.utils.LoggerUtils

@SuppressLint("SetTextI18n")
class CustomDialog private constructor(
    private val dialogType: DialogType,
    private val item: String? = null
) : DialogFragment() {

    enum class DialogType {
        UNLINK,
        DELETE_DIARY
    }

    companion object {
        @Volatile
        private var instance: CustomDialog? = null

        fun getInstance(dialogType: DialogType, item: String? = null): CustomDialog {
            synchronized(this) {
                instance?.let {
                    try {
                        it.dismiss()
                    } catch (e: Exception) {
                        LoggerUtils.e("Error dismissing dialog: ${e.message}")
                    }
                }
                instance = CustomDialog(dialogType, item)
                return instance!!
            }
        }
    }

    interface OnButtonClickListener {
        fun onButton1Clicked()
        fun onButton2Clicked()
    }

    private var buttonClickListener: OnButtonClickListener? = null

    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    private var _binding: CustomDialogBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.let { requireContext().dialogResize(it, 0.7f) }
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomDialogBinding.inflate(inflater, container, false)

        when (dialogType) {
            DialogType.UNLINK -> setUnlink()
            DialogType.DELETE_DIARY -> setDeleteDiary()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    private fun setUnlink(){
        binding.tvDialogTitle.text = "회원 탈퇴"
        binding.tvDialogContent.text = "$item, 정말 탈퇴할고양? \uD83E\uDD7A"
        binding.tvDialogMsg.text = "회원탈퇴 이후 모든 자료는 삭제되며, 같은 아이디로 재가입 시에도\n일기와 대화 내용은 복원되지 않습니다."
        binding.tvDialogMsg.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        binding.btnDialog1.text = "예"
        binding.btnDialog2.text = "아니오"

        binding.btnDialog1.setOnClickListener {
            buttonClickListener?.onButton1Clicked()
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
            dismiss()
        }
    }

    private fun  setDeleteDiary(){
        binding.tvDialogTitle.text = "일기 삭제"
        binding.tvDialogContent.text = "삭제 후에는 복원이 불가능합니다.\n정말 삭제하시겠습니까?"
        binding.tvDialogMsg.visibility = View.GONE

        binding.btnDialog1.text = "예"
        binding.btnDialog2.text = "아니오"

        binding.btnDialog1.setOnClickListener {
            buttonClickListener?.onButton1Clicked()
            dismiss()
        }

        binding.btnDialog2.setOnClickListener {
            buttonClickListener?.onButton2Clicked()
            dismiss()
        }
    }

}