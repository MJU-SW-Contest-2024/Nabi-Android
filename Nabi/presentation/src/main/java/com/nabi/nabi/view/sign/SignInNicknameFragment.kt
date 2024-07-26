package com.nabi.nabi.view.sign

import androidx.fragment.app.activityViewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSignNicknameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInNicknameFragment: BaseFragment<FragmentSignNicknameBinding>(R.layout.fragment_sign_nickname) {
    private val signViewModel: SignViewModel by activityViewModels()

    override fun initView() {

    }
}