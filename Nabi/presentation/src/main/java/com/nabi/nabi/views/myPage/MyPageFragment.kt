package com.nabi.nabi.views.myPage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.datastore.dataStore
import androidx.fragment.app.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.base.NabiApplication.Companion.consecutiveDay
import com.nabi.nabi.base.NabiApplication.Companion.nickname
import com.nabi.nabi.custom.CustomDialog
import com.nabi.nabi.databinding.FragmentMypageBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.sign.SignActivity
import com.nabi.nabi.views.sign.SignInNicknameFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private val myPageViewModel: MyPageViewModel by viewModels()

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
            (requireActivity() as MainActivity).replaceFragment(SignInNicknameFragment(), true)
        }

        binding.btnLoadDiary.setOnClickListener {

        }

        binding.btnWithdraw.setOnClickListener {
            val withdrawDialog = CustomDialog.getInstance(CustomDialog.DialogType.UNLINK, nickname)

            withdrawDialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun onButton1Clicked() {
                    myPageViewModel.withdraw()
                }

                override fun onButton2Clicked() {

                }
            })

            withdrawDialog.show(parentFragmentManager, "WithdrawDialog")
        }
    }

    override fun setObserver() {
        super.setObserver()
        myPageViewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 탈퇴 실패")
                }

                is UiState.Success -> {
                    myPageViewModel.clearData()
                }
            }
        }

        myPageViewModel.clearState.observe(viewLifecycleOwner)
        {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 정보 삭제 실패")
                }

                is UiState.Success -> {
                    requireActivity().apply {
                        startActivity(Intent(this, SignActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}