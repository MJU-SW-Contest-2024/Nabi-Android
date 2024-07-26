package com.nabi.nabi.view.sign

import androidx.fragment.app.activityViewModels
import com.nabi.data.service.KakaoAuthService
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSignProviderBinding
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInProviderFragment: BaseFragment<FragmentSignProviderBinding>(R.layout.fragment_sign_provider) {
    private val signViewModel: SignViewModel by activityViewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    override fun initView() {

    }

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
                    val ft = requireActivity().supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fl_sign, SignInNicknameFragment())
                    ft.commit()
                }
            }
        }
    }
}