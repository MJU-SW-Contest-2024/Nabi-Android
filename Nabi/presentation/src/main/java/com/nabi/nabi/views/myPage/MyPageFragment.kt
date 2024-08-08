package com.nabi.nabi.views.myPage

import android.annotation.SuppressLint
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.base.NabiApplication.Companion.consecutiveDay
import com.nabi.nabi.base.NabiApplication.Companion.nickname
import com.nabi.nabi.custom.CustomDialog
import com.nabi.nabi.databinding.FragmentMypageBinding

class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.tvNickname.text = "$nickname 님"
        binding.tvConsecutiveDay.text = "일기 작성 ${consecutiveDay}일 째"
    }

    override fun initListener() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnChangeNickname.setOnClickListener {

        }

        binding.btnLoadDiary.setOnClickListener {

        }

        binding.btnWithdraw.setOnClickListener {
            val withdrawDialog = CustomDialog.getInstance(CustomDialog.DialogType.UNLINK)

            withdrawDialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun onButton1Clicked() {

                }

                override fun onButton2Clicked() {

                }
            })

            withdrawDialog.show(parentFragmentManager, "WithdrawDialog")
        }
    }
}