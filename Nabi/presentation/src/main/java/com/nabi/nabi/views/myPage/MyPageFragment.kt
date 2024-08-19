package com.nabi.nabi.views.myPage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.base.NabiApplication.Companion.consecutiveDay
import com.nabi.nabi.base.NabiApplication.Companion.nickname
import com.nabi.nabi.custom.CustomDialog
import com.nabi.nabi.databinding.FragmentMypageBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.sign.SignActivity
import com.nabi.nabi.views.sign.SignInNicknameFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private val myPageViewModel: MyPageViewModel by viewModels()

    private lateinit var pdfPickerLauncher: ActivityResultLauncher<Intent>

    private val requiredGalleryPermissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        (requireActivity() as MainActivity).setStatusBarColor(R.color.white, false)

        binding.tvNickname.text = "$nickname 님"
        binding.tvConsecutiveDay.text = "일기 작성 ${consecutiveDay}일 째"

        pdfPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    LoggerUtils.d(it.toString())
                    readPdfFile(it)
                }
            }
        }
    }

    override fun initListener() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnChangeNickname.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(SignInNicknameFragment(false), true)
        }

        binding.btnLoadDiary.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "application/pdf"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            pdfPickerLauncher.launch(intent)
        }

        binding.btnAppGuide.setOnClickListener {
            val url = "https://humane-bucket-09b.notion.site/92da63ef98a64d219100e299c4614908"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.btnWithdraw.setOnClickListener {
            val withdrawDialog = CustomDialog.getInstance(CustomDialog.DialogType.UNLINK, nickname)

            withdrawDialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                override fun onButton1Clicked() {
                    myPageViewModel.withdraw()
                }

                override fun onButton2Clicked() {

                }
            })

            withdrawDialog.show(parentFragmentManager, "WithdrawDialog")
        }
    }

    override fun setObserver() {
        super.setObserver()

        myPageViewModel.loadState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 가져오기 실패")
                }

                is UiState.Success -> {
                    showToast("일기 가져오기 성공!")
                }
            }
        }

        myPageViewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 탈퇴 실패")
                }

                is UiState.Success -> {
                    myPageViewModel.clearData()
                }
            }
        }

        myPageViewModel.clearState.observe(viewLifecycleOwner)
        {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 정보 삭제 실패")
                }

                is UiState.Success -> {
                    requireActivity().apply {
                        startActivity(Intent(this, SignActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun readPdfFile(uri: Uri) {
        val realPath = getRealPathFromPdfUri(requireContext(), uri)
        if (realPath != null) {
            LoggerUtils.d("Real Path: $realPath")
            myPageViewModel.loadDiary(realPath)
        } else {
            LoggerUtils.e("Failed to get real path from URI")
            showToast("PDF 파일 경로를 가져오는데 실패했습니다.")
        }
    }

    private fun getRealPathFromPdfUri(context: Context, uri: Uri): String? {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val displayName = if (displayNameIndex != -1) {
                    cursor.getString(displayNameIndex)
                } else {
                    // 파일 이름을 가져올 수 없는 경우 임의의 이름 생성
                    "temp_pdf_${System.currentTimeMillis()}.pdf"
                }

                // 임시 파일 생성
                val file = File(context.cacheDir, displayName)

                try {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                    // 임시 파일의 절대 경로 반환
                    return file.absolutePath
                } catch (e: IOException) {
                    LoggerUtils.e("Error copying file: ${e.message}")
                }
            }
        }
        return null
    }
}