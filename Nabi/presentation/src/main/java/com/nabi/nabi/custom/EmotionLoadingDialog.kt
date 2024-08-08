package com.nabi.nabi.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nabi.nabi.R
import com.nabi.nabi.databinding.DialogAddDiaryDoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmotionLoadingDialog: DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog_fullscreen)
        isCancelable = false
    }

    private lateinit var binding: DialogAddDiaryDoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddDiaryDoneBinding.inflate(inflater, container, false)
        return binding.root
    }
}