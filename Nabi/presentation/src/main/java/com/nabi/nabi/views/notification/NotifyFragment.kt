package com.nabi.nabi.views.notification

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseFragment
import com.nabi.nabi.custom.CustomDecoration
import com.nabi.nabi.databinding.FragmentNotifyBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotifyFragment : BaseFragment<FragmentNotifyBinding>(R.layout.fragment_notify) {
    private lateinit var notificationAdapter: NotificationAdapter
    private val viewModel: NotificationViewModel by viewModels()

    override fun initView() {
        (requireActivity() as MainActivity).setStatusBarColor(R.color.white, false)

        viewModel.fetchData()
        setNotificationRv()
    }

    private fun setNotificationRv() {
        notificationAdapter = NotificationAdapter()

        binding.rvFcmNotify.apply {
            adapter = notificationAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
            addItemDecoration(
                CustomDecoration(
                    0.5f,
                    ContextCompat.getColor(requireContext(), R.color.gray2)
                )
            )
        }
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.notifyState.observe(this) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("알림 데이터 불러오기 실패: ${it.message}")
                }

                is UiState.Success -> {
                    notificationAdapter.setData(it.data)
                }
            }
        }
    }
}