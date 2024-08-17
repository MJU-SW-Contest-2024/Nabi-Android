package com.nabi.nabi.views.sign

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.nabi.data.service.KakaoAuthService
import com.nabi.data.service.NaverAuthService
import com.nabi.domain.enums.AuthProvider
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSignProviderBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInProviderFragment :
    BaseFragment<FragmentSignProviderBinding>(R.layout.fragment_sign_provider) {
    private val signViewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var naverAuthService: NaverAuthService

    override fun initView() {
        (requireActivity() as SignActivity).setStatusBarColor(R.color.white, false)

        signViewModel.getRecentAuthProvider()
    }

    override fun initListener() {
        super.initListener()

        binding.btnKakao.setOnClickListener {
            kakaoAuthService.signInKakao(signViewModel::signIn)
        }
        binding.btnRecentKakao.setOnClickListener {
            kakaoAuthService.signInKakao(signViewModel::signIn)
        }
        binding.btnNaver.setOnClickListener {
            showToast("해당 기능은 추후 제공될 예정입니다\n조금만 기다려 주세요!")
        }
        binding.btnRecentNaver.setOnClickListener { showToast("해당 기능은 추후 제공될 예정입니다\n조금만 기다려 주세요!") }
        binding.btnGoogle.setOnClickListener { showToast("해당 기능은 추후 제공될 예정입니다\n조금만 기다려 주세요!") }
        binding.btnRecentGoogle.setOnClickListener { showToast("해당 기능은 추후 제공될 예정입니다\n조금만 기다려 주세요!") }
    }

    override fun setObserver() {
        super.setObserver()

        signViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("로그인 실패: ${it.message}")
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    signViewModel.saveSignInInfo(it.data)
                }
            }
        }

        signViewModel.saveState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("로그인 정보 저장 실패: ${it.message}")
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    if (it.data) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

        signViewModel.recentState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    LoggerUtils.e("최근에 사용한 프로바이더 조회 실패: ${it.message}")
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    when (it.data) {
                        AuthProvider.NAVER -> {
                            binding.groupRecentNaver.visibility = View.VISIBLE
                            binding.groupRecentKakao.visibility = View.GONE
                            binding.groupRecentGoogle.visibility = View.GONE
                        }

                        AuthProvider.KAKAO -> {
                            binding.groupRecentNaver.visibility = View.GONE
                            binding.groupRecentKakao.visibility = View.VISIBLE
                            binding.groupRecentGoogle.visibility = View.GONE
                        }

                        AuthProvider.GOOGLE -> {
                            binding.groupRecentNaver.visibility = View.GONE
                            binding.groupRecentKakao.visibility = View.GONE
                            binding.groupRecentGoogle.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}