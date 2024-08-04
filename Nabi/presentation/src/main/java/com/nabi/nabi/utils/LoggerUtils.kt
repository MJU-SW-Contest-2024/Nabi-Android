package com.nabi.nabi.utils

import com.nabi.nabi.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

object LoggerUtils {

    init {
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }
    }

    private const val TAG = "LOGGER"

    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.t(TAG).d(message)
        }
    }

    fun i(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.t(TAG).i(message)
        }
    }

    fun w(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.t(TAG).w(message)
        }
    }

    fun e(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.t(TAG).e(message)
        }
    }

    fun json(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.t(TAG).json(message)
        }
    }

    fun xml(message: String) {
        if (BuildConfig.DEBUG) {
            Logger.t(TAG).xml(message)
        }
    }
}