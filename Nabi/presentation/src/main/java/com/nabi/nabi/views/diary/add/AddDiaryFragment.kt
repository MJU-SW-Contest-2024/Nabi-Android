package com.nabi.nabi.views.diary.add

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.nabi.data.room.DiaryDatabase
import com.nabi.data.room.DiaryEntity
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.custom.EmotionLoadingDialog
import com.nabi.nabi.databinding.FragmentAddDiaryBinding
import com.nabi.nabi.utils.Constants
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.diary.detail.DetailDiaryFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddDiaryFragment(
    private val isEdit: Boolean,
    private val diaryId: Int? = null,
    private val content: String? = null,
    private var diaryDate: String,
) : BaseFragment<FragmentAddDiaryBinding>(R.layout.fragment_add_diary) {
    private val viewModel: AddDiaryViewModel by viewModels()
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private var isListening: Boolean = false
    private lateinit var emotionLoadingDialog: EmotionLoadingDialog
    private var currentDiaryId: Int? = null

    private lateinit var db: DiaryDatabase
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun initView() {
        db = DiaryDatabase.getInstance(requireContext())

        binding.tvDiaryDate.text = diaryDate
        if (isEdit) {
            binding.etDiary.setText(content)
            binding.btnSave.visibility = View.VISIBLE
        } else if (content != null) {
            binding.etDiary.setText(content)
        }

        binding.etDiary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > 0) {
                    binding.btnSave.visibility = View.VISIBLE
                } else {
                    binding.btnSave.visibility = View.GONE
                }
            }

        })

        // RecognizerIntent 생성
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "Nabi")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        }

        // SpeechRecognizer 초기화
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(recognitionListener)

        emotionLoadingDialog = EmotionLoadingDialog()
    }

    override fun initListener() {
        super.initListener()

        val inputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(diaryDate, inputFormatter)
        val diaryEntryDate = date.format(outputFormatter)

        binding.ibBack.setOnClickListener {
            val content = binding.etDiary.text.toString().trim()
            val diaryEntity = DiaryEntity(
                diaryTempDate = diaryEntryDate,
                diaryTempContent = content
            )

            if (content.isNotEmpty()) {
                coroutineScope.launch {
                    try {
                        val existingDiary = db.getDiaryDao().getDiaryByDate(diaryEntryDate)
                        if (existingDiary != null) {
                            db.getDiaryDao()
                                .updateTempDiary(diaryEntity.copy(id = existingDiary.id))
                        } else {
                            db.getDiaryDao().addTempDiary(diaryEntity)
                        }
                    } catch (e: Exception) {
                        showToast("임시 일기 저장 실패: ${e.message}")
                    }
                }
            }
            requireActivity().supportFragmentManager.popBackStack()

        }

        binding.ibMic.setOnClickListener {
            if (!(requireActivity() as BaseActivity<*>).checkPermissions(Constants.AUDIO_PERMISSIONS)) {
                (requireActivity() as BaseActivity<*>).requestPermissions(Constants.AUDIO_PERMISSIONS)
            } else {
                if (!isListening) {
                    startListening()
                } else {
                    stopListening()
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val content = binding.etDiary.text.toString().trim()

            if (content.isNotEmpty()) {
                if (isEdit) {
                    // 일기 수정일 때
                    emotionLoadingDialog.show(
                        requireActivity().supportFragmentManager, "EmotionLoadingDialog"
                    )
                    viewModel.updateDiary(diaryId!!, content, diaryEntryDate)
                } else {
                    // 일기 작성일 때
                    emotionLoadingDialog.show(
                        requireActivity().supportFragmentManager, "EmotionLoadingDialog"
                    )
                    viewModel.addDiary(content, diaryEntryDate)
                }
            } else {
                // content가 empty일 때
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
                    currentDiaryId = it.data.id
                    viewModel.getDiaryEmotion(currentDiaryId!!)
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 수정 실패")
                }

                is UiState.Success -> {
                    currentDiaryId = it.data.diaryId
                    viewModel.getDiaryEmotion(currentDiaryId!!)
                }
            }
        }

        viewModel.getEmotionState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기 감정분석 실패")
                }

                is UiState.Success -> {
                    val emotionState = it.data
                    viewModel.addDiaryEmotion(currentDiaryId!!, emotionState)
                }
            }
        }

        viewModel.addEmotionState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("일기에 감정 추가 실패")
                    emotionLoadingDialog.dismiss()
                }

                is UiState.Success -> {
                    emotionLoadingDialog.dismiss()
                    if (isEdit) {
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        (requireActivity() as MainActivity).replaceFragment(
                            DetailDiaryFragment(currentDiaryId!!),
                            false
                        )
                    }
                }
            }
        }
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            showToast("음성 인식 시작")
        }

        override fun onBeginningOfSpeech() {} // 말하기 시작했을 때 호출
        override fun onRmsChanged(rmsdB: Float) {} // 입력받는 소리의 크기를 알려줌
        override fun onBufferReceived(buffer: ByteArray) {} // 말을 시작하고 인식이 된 단어를 buffer에 담음

        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
            // 음성 인식이 끝나면 자동으로 중지
            isListening = false
            showToast("음성 인식 마침")
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
                else -> "알 수 없는 오류"
            }

            showToast(message)
        }

        // 인식 결과가 준비되면 호출
        @SuppressLint("SetTextI18n")
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null) {
                val resultText = matches.joinToString(separator = " ")
                binding.etDiary.setText("${binding.etDiary.text} $resultText")
            }
        }

        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}

        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }

    private fun startListening() {
        speechRecognizer.startListening(recognizerIntent)
        isListening = true
    }

    private fun stopListening() {
        speechRecognizer.stopListening()
        isListening = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.destroy()
    }
}