package com.nabi.nabi.views.home

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nabi.domain.model.home.RecentFiveDiary
import com.nabi.nabi.R
import com.nabi.nabi.databinding.ItemDiaryBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.views.OnRvItemClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeRvAdapter : RecyclerView.Adapter<HomeRvAdapter.ActivityViewHolder>() {
    var dataList: List<RecentFiveDiary> = mutableListOf()

    inner class ActivityViewHolder(val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isBookmarked = false

        fun bind(diary: RecentFiveDiary) {
            isBookmarked = diary.isBookmarked
            binding.ivBookmark.apply {
                setImageResource(
                    if (diary.isBookmarked) R.drawable.ic_heart_filled else R.drawable.ic_heart
                )
                setOnClickListener {
                    rvItemBookmarkClickListener.onClick(diary)
                }
            }
            binding.tvDiaryDate.text = formatDateString(diary.diaryEntryDate)
            binding.tvDiary.text = diary.content

            adjustText(binding.tvDiary, diary.diaryId)
        }

        fun isBookmarked(): Boolean {
            return isBookmarked
        }

        fun changeImageBookmark(isBookmarked: Boolean) {
            this.isBookmarked = isBookmarked
            binding.ivBookmark.setImageResource(if (isBookmarked) R.drawable.ic_heart_filled else R.drawable.ic_heart)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRvAdapter.ActivityViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeRvAdapter.ActivityViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<RecentFiveDiary>) {
        dataList = newList
        notifyDataSetChanged()
    }

    private lateinit var rvItemClickListener: OnRvItemClickListener<Int>

    fun setRvItemClickListener(rvItemClickListener: OnRvItemClickListener<Int>) {
        this.rvItemClickListener = rvItemClickListener
    }

    private lateinit var rvItemBookmarkClickListener: OnRvItemClickListener<RecentFiveDiary>

    fun setRvItemBookmarkClickListener(rvItemClickListener: OnRvItemClickListener<RecentFiveDiary>) {
        this.rvItemBookmarkClickListener = rvItemClickListener
    }

    private fun adjustText(tvDiary: TextView, diaryId: Int) {
        tvDiary.post {
            val layout = tvDiary.layout
            if (layout != null) {
                val lines = layout.lineCount
                if (lines > 3) {
                    val lastLineStart = layout.getLineStart(3)
                    val lastLineEnd = layout.getLineEnd(3)

                    val text = tvDiary.text.toString()
                    val truncatedText = text.substring(0, lastLineEnd).trimEnd()

                    val moreText = "...더보기"
                    val remainingLength = lastLineEnd - lastLineStart

                    val displayedText = if (remainingLength >= moreText.length) {
                        truncatedText.substring(0, lastLineEnd - moreText.length) + moreText
                    } else {
                        truncatedText.substring(0, lastLineEnd - (moreText.length - remainingLength)) + moreText
                    }

                    val builder = SpannableStringBuilder(displayedText)
                    val moreTextStart = builder.indexOf(moreText)
                    if (moreTextStart >= 0) {
                        val moreTextEnd = moreTextStart + moreText.length

                        builder.setSpan(object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                rvItemClickListener.onClick(diaryId)
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                ds.isUnderlineText = false
                                ds.color = tvDiary.context.getColor(R.color.gray2)
                            }
                        }, moreTextStart, moreTextEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    tvDiary.text = builder
                    tvDiary.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    }



    fun formatDateString(inputDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val targetFormat = SimpleDateFormat("yyyy. MM. dd", Locale.KOREAN)

        return try {
            val date: Date? = originalFormat.parse(inputDate)
            date?.let { targetFormat.format(it) } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}