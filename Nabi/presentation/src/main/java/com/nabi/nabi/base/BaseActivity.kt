package com.nabi.nabi.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.nabi.nabi.utils.LoggerUtils

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    AppCompatActivity() {
    lateinit var binding: T
    private var currentToast: Toast? = null
    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionResult(permissions)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()
        super.onCreate(savedInstanceState)
        LoggerUtils.d("onCreate: $localClassName")

        binding = DataBindingUtil.setContentView(this, layoutId)
        initView()
        initListener()
        setObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        LoggerUtils.d("onDestroy: $localClassName")
    }

    protected fun showToast(msg: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    protected open fun beforeSetContentView() {}
    protected abstract fun initView()
    protected open fun initListener() {}
    protected open fun setObserver() {}


    // Permission
    fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }

        return true
    }

    private fun setPermissionLauncher() {
//        permissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//                handlePermissionResult(permissions)
//            }
    }

    private fun handlePermissionResult(
        permissions: Map<String, Boolean>,
    ) {
        val permissionArray = permissions.keys.toTypedArray()
        val grantResults =
            permissions.values.map { if (it) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED }
                .toIntArray()

        val deniedPermissions = permissionArray.filterIndexed { index, _ ->
            grantResults[index] != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }

    protected open fun onPermissionsGranted() {}
    protected open fun onPermissionsDenied() {}

    protected fun showDeniedPermissionDialog(msg: String) {
        LoggerUtils.e(msg)
        showToast(msg)
//        CustomDialog.getInstance(CustomDialog.DialogType.DENIED_PERMISSION, null).apply {
//            setButtonClickListener(object : CustomDialog.OnButtonClickListener {
//                override fun onButton1Clicked() {}
//                override fun onButton2Clicked() {
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                        data = Uri.fromParts("package", requireActivity().packageName, null)
//                    }
//                    requireActivity().startActivity(intent)
//                    dismiss()
//                }
//            })
//        }.show(requireActivity().supportFragmentManager, "")
    }

    fun requestPermissions(permissions: Array<String>) {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            val showRationale = permissionsToRequest.any {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }
            if (showRationale) {
                permissionLauncher.launch(permissions)
            } else {
                permissionLauncher.launch(permissionsToRequest)
            }
        }
    }
}
