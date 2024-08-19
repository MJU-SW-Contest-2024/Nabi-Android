package com.nabi.nabi.views.diary.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.nabi.nabi.R
import com.nabi.nabi.databinding.DialogUpdateEmotionBinding
import com.nabi.nabi.extension.dialogResize
import com.nabi.nabi.utils.LoggerUtils
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowOrientationRules
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class UpdateEmotionDialog(
    private val emotion: String
) : DialogFragment() {
    private var mBinding: DialogUpdateEmotionBinding? = null
    private val binding: DialogUpdateEmotionBinding get() = mBinding!!

    private lateinit var toggleButtons: List<ToggleButton>
    private var lastCheckedButton: ToggleButton? = null

    private fun createEmotionTooltip(emotion: String): Balloon {
        return createBalloon(context = requireContext()) {
            setHeight(36)
            setWidth(BalloonSizeSpec.WRAP)
            setText(emotion)
            setTextSize(10f)
            setTextColorResource(R.color.white)
            setTextTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.pretendard_semi_bold
                )!!
            )
            setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
            setArrowSize(10)
            setArrowPosition(0.5f)
            setArrowOrientationRules(ArrowOrientationRules.ALIGN_FIXED)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setPaddingHorizontal(8)
            setCornerRadius(5f)
            setBackgroundColorResource(R.color.black)
            setBalloonAnimation(BalloonAnimation.FADE)
            setDismissWhenTouchOutside(false) // 벌룬 영역 밖을 터치해도 사라지지 않도록 설정
            setDismissWhenClicked(true) // 벌룬을 클릭했을 때만 사라지도록 설정
            setLifecycleOwner(viewLifecycleOwner)
            build()
        }
    }

    interface OnDoneButtonClickListener {
        fun onClick(emotion: String)
    }

    private var buttonClickListener: OnDoneButtonClickListener? = null

    fun setButtonClickListener(buttonClickListener: OnDoneButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.let { requireContext().dialogResize(it, 0.7f) }
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogUpdateEmotionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButtons = listOf(
            binding.tbAnger,
            binding.tbHappiness,
            binding.tbBoredom,
            binding.tbSadness,
            binding.tbAnxiety
        )

        var activeBalloon: Balloon? = null

        val initialToggleButton = when (emotion) {
            "화남" -> binding.tbAnger
            "행복" -> binding.tbHappiness
            "평온" -> binding.tbBoredom
            "우울" -> binding.tbSadness
            "불안" -> binding.tbAnxiety
            else -> null
        }

        initialToggleButton?.let { toggleButton ->
            toggleButton.isChecked = true
            lastCheckedButton = toggleButton
            val initialMessage = when (toggleButton) {
                binding.tbAnger -> "화나"
                binding.tbHappiness -> "행복해"
                binding.tbBoredom -> "평온해"
                binding.tbSadness -> "슬퍼"
                binding.tbAnxiety -> "불안해"
                else -> ""
            }

            view.post {
                activeBalloon = createEmotionTooltip(initialMessage).apply {
                    showAlignTop(toggleButton)
                }
            }
        }

        toggleButtons.forEach { toggleButton ->
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val msg = when (toggleButton) {
                        binding.tbAnger -> "화나"
                        binding.tbHappiness -> "행복해"
                        binding.tbBoredom -> "평온해"
                        binding.tbSadness -> "슬퍼"
                        binding.tbAnxiety -> "불안해"
                        else -> ""
                    }

                    activeBalloon?.dismiss()

                    activeBalloon = createEmotionTooltip(msg).apply {
                        showAlignTop(toggleButton)
                    }

                    toggleButtons.filter { it != toggleButton }.forEach {
                        it.isChecked = false
                    }

                    lastCheckedButton = toggleButton
                } else {
                    if (toggleButtons.none { it.isChecked }) {
                        toggleButton.isChecked = true
                    } else {
                        activeBalloon?.dismiss()
                    }
                }
            }
        }

        binding.btnDone.setOnClickListener {
            val emotionString = when (lastCheckedButton) {
                binding.tbAnger -> "화남"
                binding.tbHappiness -> "행복"
                binding.tbBoredom -> "평온"
                binding.tbSadness -> "우울"
                binding.tbAnxiety -> "불안"
                else -> ""
            }
            LoggerUtils.i(emotionString)
            buttonClickListener?.onClick(emotionString)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}
