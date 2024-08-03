package com.nabi.nabi.views.diary.search

import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.databinding.FragmentSearchDiaryBinding
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDiaryFragment: BaseFragment<FragmentSearchDiaryBinding>(R.layout.fragment_search_diary) {
    private val viewModel: SearchDiaryViewModel by viewModels()
    private lateinit var searchDiaryAdapter: SearchDiaryAdapter

    override fun initView() {
        setEditTextFilter()
        setSearchDiaryAdapter()
    }

    private fun setSearchDiaryAdapter(){
        searchDiaryAdapter = SearchDiaryAdapter()
        binding.rvSearchDiaryResult.layoutManager = LinearLayoutManager(requireContext())
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
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.diaryState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("검색 실패: ${it.message}")
                }
                is UiState.Success -> {
                    searchDiaryAdapter.searchWord = binding.etSearchDiary.text.toString()
                    searchDiaryAdapter.submitList(it.data)
                }
            }
        }
    }
}