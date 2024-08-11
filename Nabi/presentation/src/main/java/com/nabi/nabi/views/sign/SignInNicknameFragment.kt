package com.nabi.nabi.views.sign

import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.base.NabiApplication.Companion.nickname
import com.nabi.nabi.databinding.FragmentSignNicknameBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignInNicknameFragment(
    private val isSignUp: Boolean = true
): BaseFragment<FragmentSignNicknameBinding>(R.layout.fragment_sign_nickname) {
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
            if(binding.etNick.text.isNotEmpty()) {
                viewModel.setNickname(binding.etNick.text.toString())
            }
        }

        binding.ibClear.setOnClickListener {
            binding.etNick.text.clear()
        }

        binding.etNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val length = s.toString().length

                binding.ibClear.visibility = if(length != 0) View.VISIBLE else View.GONE

                binding.btnDone.apply {
                    if(length != 0){
                        setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.shape_radius_50))

                        setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    } else {
                        setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.shape_radius_50_with_stroke))
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.gray2))
                    }
                }
            }
        })
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
                    nickname = binding.etNick.text.toString()

                    if(isSignUp){
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}