package com.nabi.nabi.views.chat

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.chat.ChatItem
import com.nabi.domain.utils.DateTimeUtils
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentChatBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.OnRvItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ChatFragment: BaseFragment<FragmentChatBinding>(R.layout.fragment_chat), OnRvItemClickListener<Int> {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter : ChatRvAdapter
    private var isLoading = false
    private var isRetrying = false

    override fun initView() {
        (requireActivity() as MainActivity).setStatusBarColor(R.color.white, false)

        setRvAdapter()
        viewModel.embedDiary()
        viewModel.fetchChatHistory()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount ?: 0

                if (lastVisibleItemPos >= itemTotalCount - 20) {
                    if(!isLoading){
                        isLoading = true
                        viewModel.fetchChatHistory()
                    }
                }
            }
        })

        binding.ibSend.setOnClickListener {
            if(binding.etSendChat.text.isNotEmpty()) {
                val dateTime = getCurrentKoreanTime()

                val processedItems = processChatItems(listOf(ChatItem(
                    chatId = chatAdapter.getLastChatId() + 1,
                    content = binding.etSendChat.text.toString(),
                    date = dateTime.first,
                    time = dateTime.second,
                    originalDateTime = dateTime.first,
                    isMine = true
                )))

                submitCustom(processedItems)

                viewModel.sendQuestion(binding.etSendChat.text.toString())
                binding.etSendChat.text.clear()
            } else {
                showToast("작성된 내용이 없어요")
            }
        }
    }

    private fun setRvAdapter(){
        chatAdapter = ChatRvAdapter(this)
        binding.rvChat.adapter = chatAdapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
    }

    override fun onClick(item: Int) {
        if(!isRetrying) {
            isRetrying = true
            viewModel.retryChatRes(item)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.embedState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    LoggerUtils.e(it.message)
                }
                is UiState.Success -> {
                    LoggerUtils.i("임베딩 완료")
                }
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    isLoading = false
                }
                is UiState.Success -> {
                    isLoading = false

                    if(it.data.isNotEmpty()){
                        submitCustom(processChatItems(it.data))
                    }
                }
            }
        }

        viewModel.resState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("앗, 답변을 준비하는데 실패했어요.\n다시 한번 시도해 볼까요?")
                    LoggerUtils.e(it.message)
                }
                is UiState.Success -> {
                    viewModel.fetchChatHistory(true)
                }
            }
        }

        viewModel.retryState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("앗, 답변을 재준비하는데 실패했어요.\n다시 한번 시도해 볼까요?")
                    LoggerUtils.e(it.message)
                    isRetrying = false
                }
                is UiState.Success -> {
                    val updatedList = chatAdapter.currentList.toMutableList()
                    updatedList.removeAt(it.data)
                    chatAdapter.submitList(updatedList)

                    viewModel.fetchChatHistory(true)
                    isRetrying = false
                }
            }
        }
    }

    private fun processChatItems(dataList: List<ChatItem>): MutableList<ChatItem> {
        val processedList = mutableListOf<ChatItem>()
        var lastDate: String? = null
        var lastOtherMessageIndex = -1

        dataList.forEachIndexed { index, item ->
            if (item.date != lastDate) {
                processedList.add(ChatItem(chatId = item.chatId, date = item.date, time = "", originalDateTime = "${item.chatId*(index+1)*-1}" , content = "", isDateHeader = true, isMine = false))
                lastDate = item.date
            }

            val showRefreshIcon = !item.isMine && index > lastOtherMessageIndex
            if (!item.isMine) {
                lastOtherMessageIndex = index
            }

            processedList.add(item.copy(showRefreshIcon = showRefreshIcon))
        }

        return processedList
    }

    private fun getCurrentKoreanTime(): Pair<String, String> {
        val currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        return DateTimeUtils.convertDateTime(currentDateTime.format(formatter))
    }

    private fun submitCustom(list: MutableList<ChatItem>) {
        val currentList = chatAdapter.currentList.toMutableList()

        if (currentList.isNotEmpty() && currentList[currentList.size - 1].isMine) {
            currentList.removeAt(currentList.size - 1)
        }

        list.addAll(currentList)
        list.sortWith(compareBy<ChatItem> { it.date }.thenByDescending { it.isDateHeader }.thenBy { it.chatId })

        val dateHeaders = mutableSetOf<String>()
        val originalDateTimes = mutableSetOf<String>()
        val filteredList = mutableListOf<ChatItem>()

        for (item in list) {
            if (item.isDateHeader) {
                if (!dateHeaders.contains(item.date)) {
                    dateHeaders.add(item.date)
                    filteredList.add(item)
                }
            } else {
                if (!originalDateTimes.contains(item.originalDateTime)) {
                    originalDateTimes.add(item.originalDateTime)
                    filteredList.add(item)
                }
            }
        }

        chatAdapter.submitList(filteredList.sortedWith(compareBy<ChatItem> { it.date }.thenByDescending { it.isDateHeader }.thenBy { it.chatId })){
            binding.rvChat.scrollToPosition(filteredList.size - 1)
        }
    }
}