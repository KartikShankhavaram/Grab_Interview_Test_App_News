package com.kartik.grabinterviewtestapp_news.data

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Executor {
    fun IOThread(t: () -> Unit) {
        val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
        IO_EXECUTOR.execute(t)
    }
}