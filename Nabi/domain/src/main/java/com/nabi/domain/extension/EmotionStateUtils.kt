package com.nabi.domain.extension

fun String.parseEmotionState(): String {
    // ' ' 사이의 감정 텍스트만 뽑아서 return
    val regex = """'([^']*)'""".toRegex()
    val matchResult = regex.find(this)
    return matchResult?.groups?.get(1)?.value ?: ""
}