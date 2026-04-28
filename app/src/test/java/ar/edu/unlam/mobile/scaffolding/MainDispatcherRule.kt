package ar.edu.unlam.mobile.scaffolding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit [TestWatcher] that replaces [Dispatchers.Main] with a [TestDispatcher].
 *
 * ViewModels use [androidx.lifecycle.viewModelScope], which runs on [Dispatchers.Main].
 * In a pure JVM test there is no Android main thread, so any ViewModel coroutine would
 * crash immediately unless we provide a test-friendly dispatcher.
 *
 * By using [StandardTestDispatcher] we get full control over virtual time:
 *   - Coroutines are queued and don't start until we call [runTest] / [advanceTimeBy].
 *   - [kotlinx.coroutines.delay] uses virtual time, so tests finish instantly.
 *
 * Usage:
 * ```
 * @get:Rule val mainDispatcherRule = MainDispatcherRule()
 *
 * @Test
 * fun example() = runTest(mainDispatcherRule.testDispatcher) {
 *     advanceTimeBy(2_001)
 *     ...
 * }
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
