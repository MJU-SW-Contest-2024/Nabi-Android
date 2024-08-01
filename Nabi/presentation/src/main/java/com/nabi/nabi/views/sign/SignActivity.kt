package com.nabi.nabi.views.sign

import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.databinding.ActivitySignBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignActivity: BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {

    override fun initView() {
        val isLoginSuccess = intent.getBooleanExtra("isLoginSuccess", false)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_sign, if(isLoginSuccess) SignInNicknameFragment() else SignInProviderFragment())
        ft.commit()
    }
}