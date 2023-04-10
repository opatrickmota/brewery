package com.patrickmota.brewery.viewmodel.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.patrickmota.brewery.data.remote.model.BreweryResponse
import com.patrickmota.brewery.data.remote.repository.BreweryRepository
import com.patrickmota.brewery.viewmodel.ViewData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

class DetailViewModelTest {

    @RelaxedMockK
    private lateinit var repository: BreweryRepository

    private lateinit var viewModel: DetailViewModel

    @RelaxedMockK
    private lateinit var observer: Observer<ViewData<BreweryResponse>>

    private lateinit var capturesArguments: MutableList<ViewData<BreweryResponse>>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        MockKAnnotations.init(this)

        capturesArguments = mutableListOf()

        viewModel = DetailViewModel(repository, testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()

        unmockkAll()
    }

    @Test
    fun `check result and viewdata status when query returns data`() = runTest {
        viewModel.brewery.observeForever(observer)

        val mockBrewery = BreweryResponse(
            "123", "", "", "", "", "", "",
            "", "", "", "", "", "", "",
            "", ""
        )

        coEvery { repository.getBreweryById("") } returns mockBrewery

        viewModel.loadBrewery("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == ViewData.Status.LOADING)
        assert(capturesArguments[1].status == ViewData.Status.COMPLETE)
        assert(viewModel.brewery.value?.data?.id == "123")
    }

    @Test
    fun `check result and viewdata status when query returns error`() = runTest {
        viewModel.brewery.observeForever(observer)

        coEvery { repository.getBreweryById("") } throws Exception("Network error")

        viewModel.loadBrewery("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == ViewData.Status.LOADING)
        assert(capturesArguments[1].status == ViewData.Status.ERROR)
        assert(viewModel.brewery.value?.error?.message == "Network error")
    }
}