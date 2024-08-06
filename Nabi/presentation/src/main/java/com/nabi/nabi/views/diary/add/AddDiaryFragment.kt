package com.nabi.nabi.views.diary.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.ActivityMainBinding
import com.nabi.nabi.databinding.FragmentAddDiaryBinding
import com.nabi.nabi.utils.Constants
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddDiaryFragment(
    private val isEdit: Boolean,
    private val diaryId: Int? = null,
    private val content: String? = null,
    private val diaryDate: String,
) : BaseFragment<FragmentAddDiaryBinding>(R.layout.fragment_add_diary) {
    private val viewModel: AddDiaryViewModel by viewModels()
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun initView() {
        binding.tvDiaryDate.text = diaryDate
        if (isEdit) {
            binding.etDiary.setText(content)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // RecognizerIntent 생성
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "Nabi")    // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")         // 언어 설정

        binding.ibMic.setOnClickListener {
            if (!(requireActivity() as BaseActivity<*>).checkPermissions(Constants.AUDIO_PERMISSIONS)) {
                (requireActivity() as BaseActivity<*>).requestPermissions(Constants.AUDIO_PERMISSIONS)
            } else {
                // mic 버튼 눌렀을 때 로직
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
                speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정
                speechRecognizer.startListening(intent)                         // 듣기 시작
            }
        }

        binding.btnAddDiary.setOnClickListener {
            val content = binding.etDiary.text.toString().trim()

            if (content.isNotEmpty()) {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val date = LocalDate.parse(diaryDate, inputFormatter)
                val diaryEntryDate = date.format(outputFormatter)
                LoggerUtils.d(diaryEntryDate)
                if (isEdit) {
                    viewModel.updateDiary(diaryId!!, content, diaryEntryDate)
                } else {
                    viewModel.addDiary(content, diaryEntryDate)
                }
            } else {
                showToast("일기 내용을 입력해주세요")
            }
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.addState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 추가 실패: ${it.message}")
                }

                is UiState.Success -> {
                    showToast("일기 추가 성공")
                    binding.etDiary.text.clear()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 수정 실패")
                }

                is UiState.Success -> {}
            }
        }
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(requireContext(), "음성인식 시작", Toast.LENGTH_SHORT).show()
        }

        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
        }

        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}

        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}

        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
        }

        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
        }

        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
//            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            for (i in matches!!.indices) binding.etDiary.text = matches[i]
        }

        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}

        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }
}