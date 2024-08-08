package com.nabi.domain.utils

object EmotionStateUtils {

    fun parseEmotionState(emotionState: String): String {
        // ' ' 사이의 감정 텍스트만 뽑아서 return
        val regex = """'([^']*)'""".toRegex()
        val matchResult = regex.find(emotionState)
        return matchResult?.groups?.get(1)?.value ?: ""
    }
}