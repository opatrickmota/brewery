package com.patrickmota.brewery.viewmodel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.core.domain.repositories.BreweryRepository
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

class HomeViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    private lateinit var repository: BreweryRepository

    private lateinit var viewModel: HomeViewModel

    @RelaxedMockK
    private lateinit var observer: Observer<com.patrickmota.brewery.viewmodel.ViewData<List<BreweryResponse>>>

    private lateinit var capturesArguments: MutableList<com.patrickmota.brewery.viewmodel.ViewData<List<BreweryResponse>>>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        MockKAnnotations.init(this)

        capturesArguments = mutableListOf()

        viewModel = HomeViewModel(repository, testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()

        unmockkAll()
    }

    @Test
    fun `check result and viewdata status when query returns data`() = runTest {
        viewModel.breweries.observeForever(observer)

        val mockBreweries = listOf(BreweryResponse(
            "123", "", "", "", "", "", "",
            "", "", "", "", "", "", "",
            "", ""
        ))

        coEvery { repository.getBreweries("") } returns mockBreweries

        viewModel.loadBreweries("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING)
        assert(capturesArguments[1].status == com.patrickmota.brewery.viewmodel.ViewData.Status.COMPLETE)
        assert(viewModel.breweries.value?.data?.first()?.id == "123")
    }

    @Test
    fun `check result and viewdata status when query returns error`() = runTest {
        viewModel.breweries.observeForever(observer)

        coEvery { repository.getBreweries("") } throws Exception("Network error")

        viewModel.loadBreweries("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING)
        assert(capturesArguments[1].status == com.patrickmota.brewery.viewmodel.ViewData.Status.ERROR)
        assert(viewModel.breweries.value?.error?.message == "Network error")
    }
}