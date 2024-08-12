package com.nabi.nabi.views.diary.emotion

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.custom.CustomDecoration
import com.nabi.nabi.databinding.FragmentEmotionSearchBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.OnRvItemClickListener
import com.nabi.nabi.views.diary.detail.DetailDiaryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmotionSearchFragment(
    private val emotion: String
) : BaseFragment<FragmentEmotionSearchBinding>(R.layout.fragment_emotion_search) {
    private val viewModel: EmotionSearchViewModel by viewModels()
    private lateinit var emotionSearchAdapter: EmotionSearchAdapter
    private var isLoading = false

    override fun initView() {
        setSearchDiaryAdapter()
        setCurrentEmotion()
        viewModel.fetchData(emotion)
    }

    override fun onResume() {
        super.onResume()

        emotionSearchAdapter.submitList(viewModel.diaryItems.value)
    }

    private fun setSearchDiaryAdapter() {
        emotionSearchAdapter = EmotionSearchAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<Int> {
                override fun onClick(item: Int) {
                    (requireActivity() as MainActivity).replaceFragment(
                        DetailDiaryFragment(item),
                        true
                    )
                }
            })
        }
        binding.rvEmotionDiaryResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmotionDiaryResult.addItemDecoration(
            CustomDecoration(
                0.5f,
                ContextCompat.getColor(requireContext(), R.color.gray2)
            )
        )
        binding.rvEmotionDiaryResult.adapter = emotionSearchAdapter
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvEmotionDiaryResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount ?: 0

                if (lastVisibleItemPos >= itemTotalCount - 6) {
                    if (!isLoading) {
                        isLoading = true
                        viewModel.fetchData(viewModel.searchEmotion.value ?: emotion)
                    }
                }
            }
        })

        // 행복, 우울, 화남, 불안, 지루함
        binding.ivEmotionAnger.setOnClickListener {
            viewModel.fetchData("화남", true)
            setCurrentEmotion()
        }
        binding.ivEmotionHappiness.setOnClickListener {
            viewModel.fetchData("행복", true)
            setCurrentEmotion()
        }
        binding.ivEmotionBoredom.setOnClickListener {
            viewModel.fetchData("지루함", true)
            setCurrentEmotion()
        }
        binding.ivEmotionSadness.setOnClickListener {
            viewModel.fetchData("우울", true)
            setCurrentEmotion()
        }
        binding.ivEmotionAnxiety.setOnClickListener {
            viewModel.fetchData("불안", true)
            setCurrentEmotion()
        }
    }

    private fun setCurrentEmotion() {
        val resourceIds = mutableListOf<Int>()
        val condition =
            if (viewModel.searchEmotion.value.isNullOrEmpty()) emotion else viewModel.searchEmotion.value

        var titleText = ""

        when (condition) {
            "화남" -> {
                titleText = "화난 감정의 일기들"
                resourceIds.addAll(
                    listOf(
                        R.drawable.img_anger,
                        R.drawable.img_happiness_gray,
                        R.drawable.img_boredom_gray,
                        R.drawable.img_sadness_gray,
                        R.drawable.img_anxiety_gray
                    )
                )
            }

            "행복" -> {
                titleText = "행복한 감정의 일기들"
                resourceIds.addAll(
                    listOf(
                        R.drawable.img_anger_gray,
                        R.drawable.img_happiness,
                        R.drawable.img_boredom_gray,
                        R.drawable.img_sadness_gray,
                        R.drawable.img_anxiety_gray
                    )
                )
            }

            "지루함" -> {
                titleText = "지루한 감정의 일기들"
                resourceIds.addAll(
                    listOf(
                        R.drawable.img_anger_gray,
                        R.drawable.img_happiness_gray,
                        R.drawable.img_boredom,
                        R.drawable.img_sadness_gray,
                        R.drawable.img_anxiety_gray
                    )
                )
            }

            "우울" -> {
                titleText = "우울한 감정의 일기들"
                resourceIds.addAll(
                    listOf(
                        R.drawable.img_anger_gray,
                        R.drawable.img_happiness_gray,
                        R.drawable.img_boredom_gray,
                        R.drawable.img_sadness,
                        R.drawable.img_anxiety_gray
                    )
                )
            }

            "불안" -> {
                titleText = "불안한 감정의 일기들"
                resourceIds.addAll(
                    listOf(
                        R.drawable.img_anger_gray,
                        R.drawable.img_happiness_gray,
                        R.drawable.img_boredom_gray,
                        R.drawable.img_sadness_gray,
                        R.drawable.img_anxiety
                    )
                )
            }
        }

        resourceIds.run {
            binding.ivEmotionAnger.setImageResource(this[0])
            binding.ivEmotionHappiness.setImageResource(this[1])
            binding.ivEmotionBoredom.setImageResource(this[2])
            binding.ivEmotionSadness.setImageResource(this[3])
            binding.ivEmotionAnxiety.setImageResource(this[4])
        }

        binding.tvTitle.text = titleText
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.searchEmotion.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                emotionSearchAdapter.submitList(emptyList())
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("검색 실패: ${it.message}")
                }

                is UiState.Success -> {
                    isLoading = false

                    if (it.data.isEmpty()) emotionSearchAdapter.submitList(emptyList())
                    else {
                        val temp = emotionSearchAdapter.currentList.toMutableList()
                        temp.addAll(it.data)
                        emotionSearchAdapter.submitList(temp)
                    }
                }
            }
        }
    }
}