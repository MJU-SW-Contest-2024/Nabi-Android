package com.nabi.nabi.views.diary.search

import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.custom.CustomDecoration
import com.nabi.nabi.databinding.FragmentSearchDiaryBinding
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.OnRvItemClickListener
import com.nabi.nabi.views.diary.detail.DetailDiaryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDiaryFragment: BaseFragment<FragmentSearchDiaryBinding>(R.layout.fragment_search_diary) {
    private val viewModel: SearchDiaryViewModel by viewModels()
    private lateinit var searchDiaryAdapter: SearchDiaryAdapter
    private var isLoading = false

    override fun initView() {
        setEditTextFilter()
        setSearchDiaryAdapter()
    }

    override fun onResume() {
        super.onResume()

        searchDiaryAdapter.submitList(viewModel.diaryItems.value)
    }

    private fun setSearchDiaryAdapter(){
        searchDiaryAdapter = SearchDiaryAdapter().apply {
            setRvItemClickListener(object : OnRvItemClickListener<Int>{
                override fun onClick(item: Int) {
                    (requireActivity() as MainActivity).replaceFragment(DetailDiaryFragment(item, "SearchDiaryFragment"), true)
                }
            })
        }
        binding.rvSearchDiaryResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchDiaryResult.addItemDecoration(CustomDecoration(0.5f, ContextCompat.getColor(requireContext(), R.color.gray2)))
        binding.rvSearchDiaryResult.adapter = searchDiaryAdapter
    }

    private fun setEditTextFilter(){
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                val char = source[i]
                if (!char.isLetter() && !(char in 'ㄱ'..'ㅎ' || char in '가'..'힣' || char in 'ㅏ'..'ㅣ')) {
                    return@InputFilter ""
                }
            }
            null
        }

        val temp = binding.etSearchDiary.filters.toMutableList()
        temp.add(filter)
        binding.etSearchDiary.filters = temp.toTypedArray()
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.etSearchDiary.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                binding.ibSearchDiary.performClick()
                true
            } else {
                false
            }
        }

        binding.ibSearchDiary.setOnClickListener {
            if(binding.etSearchDiary.text.length >= 2) viewModel.fetchData(binding.etSearchDiary.text.toString())
            else showToast("2글자 이상 입력해주세요")
        }

        binding.rvSearchDiaryResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount ?: 0

                if (lastVisibleItemPos >= itemTotalCount - 6) {
                    if(!isLoading){
                        isLoading = true
                        viewModel.fetchData()
                    }
                }
            }
        })

    }

    override fun setObserver() {
        super.setObserver()

        viewModel.searchWord.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                searchDiaryAdapter.searchWord = binding.etSearchDiary.text.toString()
                searchDiaryAdapter.submitList(emptyList())
            }
        }

        viewModel.diaryState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("검색 실패: ${it.message}")
                }
                is UiState.Success -> {
                    isLoading = false

                    if(it.data.isEmpty()) searchDiaryAdapter.submitList(emptyList())
                    else {
                        val temp = searchDiaryAdapter.currentList.toMutableList()
                        temp.addAll(it.data)
                        searchDiaryAdapter.submitList(temp)
                    }
                }
            }
        }
    }
}