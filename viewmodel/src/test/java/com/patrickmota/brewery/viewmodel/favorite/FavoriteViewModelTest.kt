package com.patrickmota.brewery.viewmodel.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.patrickmota.brewery.core.data.models.BreweryModel
import com.patrickmota.brewery.core.domain.repositories.FavoritesRepository
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

class FavoriteViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @RelaxedMockK
    private lateinit var repository: FavoritesRepository

    private lateinit var viewModel: FavoriteViewModel

    @RelaxedMockK
    private lateinit var observer: Observer<com.patrickmota.brewery.viewmodel.ViewData<BreweryModel>>
    private lateinit var capturesArguments: MutableList<com.patrickmota.brewery.viewmodel.ViewData<BreweryModel>>

    @RelaxedMockK
    private lateinit var observerList: Observer<com.patrickmota.brewery.viewmodel.ViewData<List<BreweryModel>>>
    private lateinit var capturesArgumentsList: MutableList<com.patrickmota.brewery.viewmodel.ViewData<List<BreweryModel>>>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        MockKAnnotations.init(this)

        capturesArguments = mutableListOf()
        capturesArgumentsList = mutableListOf()

        viewModel =
            FavoriteViewModel(repository, testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()

        unmockkAll()
    }

    @Test
    fun `check result and viewdata status when query returns brewery`() = runTest {
        viewModel.brewery.observeForever(observer)

        val mockkBreweryModel = BreweryModel(
            "123", "", "", "", "", "", "",
            "", "", "", "", "", "", "",
            "", ""
        )

        coEvery { repository.getBreweryById("") } returns mockkBreweryModel

        viewModel.loadFavoriteById("")

        verify { observer.onChanged(capture(capturesArguments)) }
        assert(capturesArguments[0].status == com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING)
        assert(capturesArguments[1].status == com.patrickmota.brewery.viewmodel.ViewData.Status.COMPLETE)
        assert(viewModel.brewery.value?.data?.id == "123")
    }

    @Test
    fun `check result and viewdata status when query returns error in search of the brewery`() =
        runTest {
            viewModel.brewery.observeForever(observer)

            coEvery { repository.getBreweryById("") } throws Exception("Network error")

            viewModel.loadFavoriteById("")

            verify { observer.onChanged(capture(capturesArguments)) }
            assert(capturesArguments[0].status == com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING)
            assert(capturesArguments[1].status == com.patrickmota.brewery.viewmodel.ViewData.Status.ERROR)
            assert(viewModel.brewery.value?.error?.message == "Network error")
        }

    @Test
    fun `check result and viewdata status when query returns breweries`() = runTest {
        viewModel.breweries.observeForever(observerList)

        val mockkBreweries = listOf(BreweryModel(
            "123", "", "", "", "", "", "",
            "", "", "", "", "", "", "",
            "", ""
        ))

        coEvery { repository.getBreweries() } returns mockkBreweries

        viewModel.loadFavoriteBreweries()

        verify { observerList.onChanged(capture(capturesArgumentsList)) }
        assert(capturesArgumentsList[0].status == com.patrickmota.brewery.viewmodel.ViewData.Status.LOADING)
        assert(capturesArgumentsList[1].status == com.patrickmota.brewery.viewmodel.ViewData.Status.COMPLETE)
        assert(viewModel.breweries.value?.data?.first()?.id == "123")
    }

    @Test
    fun `should add favorite brewery`() = runTest {
        val mockkBreweryModel = BreweryModel(
            "123", "", "", "", "", "", "",
            "", "", "", "", "", "", "",
            "", ""
        )

        viewModel.addFavorite(mockkBreweryModel)

        coVerify(exactly = 1) { repository.addFavorite(any()) }
    }

    @Test
    fun `should delete favorite brewery`() = runTest {
        val mockkBreweryModel = BreweryModel(
            "123", "", "", "", "", "", "",
            "", "", "", "", "", "", "",
            "", ""
        )

        viewModel.deleteFavorite(mockkBreweryModel)

        coVerify(exactly = 1) { repository.deleteBrewery(any()) }
    }
}