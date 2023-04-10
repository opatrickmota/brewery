package com.patrickmota.brewery.viewmodel.rate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.patrickmota.brewery.data.local.model.RateModel
import com.patrickmota.brewery.data.local.repository.RateRepository
import com.patrickmota.brewery.viewmodel.ViewData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RateViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    private lateinit var repository: RateRepository

    private lateinit var viewModel: RateViewModel

    @RelaxedMockK
    private lateinit var observer: Observer<ViewData<RateModel>>

    private lateinit var capturesArguments: MutableList<ViewData<RateModel>>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        MockKAnnotations.init(this)

        capturesArguments = mutableListOf()

        viewModel = RateViewModel(repository, testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()

        unmockkAll()
    }

    @Test
    fun `check result and viewdata status when query returns data`() = runTest {
        viewModel.rating.observeForever(observer)

        val mockRate = RateModel("123", 1)

        coEvery { repository.getRateById("") } returns mockRate

        viewModel.getRateById("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == ViewData.Status.LOADING)
        assert(capturesArguments[1].status == ViewData.Status.COMPLETE)
        assert(viewModel.rating.value?.data?.id == "123")
    }

    @Test
    fun `check result and viewdata status when query returns error`() = runTest {
        viewModel.rating.observeForever(observer)

        coEvery { repository.getRateById("") } throws Exception("Network error")

        viewModel.getRateById("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == ViewData.Status.LOADING)
        assert(capturesArguments[1].status == ViewData.Status.ERROR)
        assert(viewModel.rating.value?.error?.message == "Network error")
    }

    @Test
    fun `should add rate`() = runTest {
        val mockkRateModel = RateModel("123", 1)

        viewModel.addRating(mockkRateModel)

        coVerify(exactly = 1) { repository.addRate(any()) }
    }
}