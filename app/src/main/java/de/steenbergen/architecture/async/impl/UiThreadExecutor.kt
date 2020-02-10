package de.steenbergen.architecture.async.impl

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

object UiThreadExecutor : Executor {
    private val mHandler: Handler =
        Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
        mHandler.post(command)
    }
}
