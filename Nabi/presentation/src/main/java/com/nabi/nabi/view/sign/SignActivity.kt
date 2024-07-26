package com.nabi.nabi.view.sign

import androidx.activity.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.databinding.ActivitySignBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignActivity: BaseActivity<ActivitySignBinding>(R.layout.activity_sign) {
    private val signViewModel: SignViewModel by viewModels()

    override fun initView() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_sign, SignInProviderFragment())
        ft.commit()
    }
}