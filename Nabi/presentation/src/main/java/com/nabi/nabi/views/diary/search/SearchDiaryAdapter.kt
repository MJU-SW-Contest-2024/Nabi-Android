package com.nabi.nabi.views.diary.search

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.nabi.R
import com.nabi.nabi.databinding.ItemSearchDiaryBinding

class SearchDiaryAdapter : ListAdapter<SearchDiary, SearchDiaryAdapter.SearchResultViewHolder>(diaryDiffUtil) {
    var searchWord = ""

    companion object {
        private val diaryDiffUtil = object : DiffUtil.ItemCallback<SearchDiary>() {
            override fun areItemsTheSame(oldItem: SearchDiary, newItem: SearchDiary): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: SearchDiary, newItem: SearchDiary): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchResultViewHolder(val binding: ItemSearchDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchDiary) {
            binding.tvSearchDiaryDate.text = item.diaryEntryDate
            applyStyleAndColorToText(binding.tvContent, "... ${item.previewContent} ...", searchWord)

            if (bindingAdapterPosition == itemCount - 1) {
                binding.divider.visibility = View.GONE
            } else {
                binding.divider.visibility = View.VISIBLE
            }
        }

        private fun applyStyleAndColorToText(
            textView: TextView,
            fullText: String,
            targetWord: String,
        ) {
            val ssb = SpannableStringBuilder(fullText)
            val startIndex = fullText.indexOf(targetWord)
            val endIndex = startIndex + targetWord.length

            if (startIndex != -1) {

                ssb.apply {
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.red)),
                        startIndex,
                        endIndex,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        startIndex,
                        endIndex,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            textView.text = ssb
        }
    }
}
