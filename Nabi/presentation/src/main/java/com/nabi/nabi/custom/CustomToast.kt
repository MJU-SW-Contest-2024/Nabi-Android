package com.nabi.nabi.custom

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.nabi.nabi.databinding.CustomToastBinding

object CustomToast {
    fun makeToast(context: Context?, msg: String): Toast {
        val binding = CustomToastBinding.inflate(LayoutInflater.from(context))

        binding.tvMsg.text = msg

        return Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
}