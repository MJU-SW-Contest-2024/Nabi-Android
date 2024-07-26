package com.nabi.nabi.view.sign

import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nabi.data.utils.LoggerUtils
import com.nabi.nabi.MainActivity
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSignNicknameBinding
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignInNicknameFragment: BaseFragment<FragmentSignNicknameBinding>(R.layout.fragment_sign_nickname) {
    private val viewModel: SignNicknameViewModel by viewModels()

    override fun initView() {
        binding.etNick.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps: Pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
            if (source == "" || ps.matcher(source).matches()) return@InputFilter source
            ""
        }, InputFilter.LengthFilter(14))
    }

    override fun initListener() {
        super.initListener()

        binding.btnDone.setOnClickListener {
            viewModel.setNickname(binding.etNick.text.toString())
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.nickState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("로그인 실패: ${it.message}")
                }
                is UiState.Success -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }
}