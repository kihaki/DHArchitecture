package de.steenbergen.architecture

import de.steenbergen.architecture.async.impl.coroutines.CoroutinesEngine
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CoroutinesEngineTest {

    private lateinit var workDispatcher: TestCoroutineDispatcher
    private lateinit var callbackDispatcher: TestCoroutineDispatcher
    private lateinit var testScope: TestCoroutineScope
    private lateinit var engine: CoroutinesEngine

    @Before
    fun setUp() {
        workDispatcher = TestCoroutineDispatcher()
        callbackDispatcher = TestCoroutineDispatcher()
        testScope = TestCoroutineScope()
        engine =
            CoroutinesEngine(
                scope = testScope,
                workDispatcher = workDispatcher,
                callbackDispatcher = callbackDispatcher
            )
    }

    @After
    fun cleanUp() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun doAsyncDoesWorkOnWorkerDispatcherAndReportsOnCallbackDispatcher() {
        workDispatcher.pauseDispatcher()
        callbackDispatcher.pauseDispatcher()

        var thrownError: Throwable? = null
        var successValue: String? = null
        val work = engine.doAsync(
            input = "input",
            operation = { input ->
                Thread.sleep(50)
                input
            },
            onError = { error -> thrownError = error },
            onSuccess = { result -> successValue = result }
        )

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)

        workDispatcher.resumeDispatcher()

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)

        callbackDispatcher.resumeDispatcher()

        Assert.assertEquals("input", successValue)
        Assert.assertEquals(null, thrownError)
    }

    @Test
    fun doAsyncDoesWorkOnWorkerDispatcherAndReportsOnCallbackDispatcher2() {
        engine =
            CoroutinesEngine(
                scope = testScope,
                workDispatcher = newSingleThreadContext("Worker"),
                callbackDispatcher = newSingleThreadContext("Callback")
            )

        var workerThread: String? = null
        var callbackThread: String? = null
        var thrownError: Throwable? = null
        var successValue: String? = null
        engine.doAsync(
            input = "input",
            operation = { input ->
                workerThread = Thread.currentThread().name.substringBefore(" @")
                input
            },
            onError = { error -> thrownError = error },
            onSuccess = { result ->
                callbackThread = Thread.currentThread().name.substringBefore(" @")
                successValue = result
            }
        )

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)

        Thread.sleep(50)

        Assert.assertEquals("input", successValue)
        Assert.assertEquals(null, thrownError)
        Assert.assertEquals("Worker", workerThread)
        Assert.assertEquals("Callback", callbackThread)
    }

    @Test
    fun doAsyncCanBeCancelled() {
        workDispatcher.pauseDispatcher()

        var thrownError: Throwable? = null
        var successValue: String? = null
        val work = engine.doAsync(
            input = "input",
            operation = { input ->
                Thread.sleep(50)
                input
            },
            onError = { error -> thrownError = error },
            onSuccess = { result ->
                successValue = result
            }
        )

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)
        work.close()

        workDispatcher.resumeDispatcher()

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)
    }

    @Test
    fun doAsyncCanBeCancelled2() {
        engine =
            CoroutinesEngine(
                scope = testScope,
                workDispatcher = newSingleThreadContext("Worker"),
                callbackDispatcher = newSingleThreadContext("Callback")
            )

        var thrownError: Throwable? = null
        var successValue: String? = null
        val work = engine.doAsync(
            input = "input",
            operation = { input ->
                Thread.sleep(50)
                input
            },
            onError = { error -> thrownError = error },
            onSuccess = { result ->
                successValue = result
            }
        )

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)
        Thread.sleep(20)
        work.close()

        Thread.sleep(100)

        Assert.assertEquals(null, successValue)
        Assert.assertEquals(null, thrownError)
    }

    @Test
    fun doAsyncWillCreateANewAsyncWorkerEveryTime() {
        val work1 = engine.doAsync(
            input = "input1",
            operation = { input ->
                input
            },
            onError = { },
            onSuccess = { }
        )
        val work2 = engine.doAsync(
            input = "input2",
            operation = { input ->
                Thread.sleep(50)
                input
            },
            onError = { },
            onSuccess = { }
        )

        Assert.assertNotEquals(work1, work2)
    }
}
