package com.nabi.nabi.views.myPage

import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentMypageBinding

class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    override fun initView() {
        
    }

    override fun initListener() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}