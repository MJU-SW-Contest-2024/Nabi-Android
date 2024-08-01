package com.nabi.nabi.views

import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.databinding.ActivityMainBinding
import com.nabi.nabi.utils.Constants
import com.nabi.nabi.views.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun initView() {
        if(!checkPermissions(Constants.FCM_PERMISSIONS)){
            requestPermissions(Constants.FCM_PERMISSIONS)
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_main, HomeFragment())
        ft.commit()
    }
}