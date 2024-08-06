package com.nabi.nabi.views.sign

import android.content.Intent
import androidx.fragment.app.viewModels
import com.nabi.data.service.KakaoAuthService
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSignProviderBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInProviderFragment: BaseFragment<FragmentSignProviderBinding>(R.layout.fragment_sign_provider) {
    private val signViewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    override fun initView() {}

    override fun initListener() {
        super.initListener()

        binding.btnKakao.setOnClickListener {
            kakaoAuthService.signInKakao(signViewModel::signIn)
        }
        binding.btnNaver.setOnClickListener {  }
        binding.btnGoogle.setOnClickListener {  }
    }

    override fun setObserver() {
        super.setObserver()

        signViewModel.loginState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("로그인 실패: ${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    if(it.data){
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        val ft = requireActivity().supportFragmentManager.beginTransaction()
                        ft.replace(R.id.fl_sign, SignInNicknameFragment())
                        ft.commit()
                    }
                }
            }
        }
    }
}