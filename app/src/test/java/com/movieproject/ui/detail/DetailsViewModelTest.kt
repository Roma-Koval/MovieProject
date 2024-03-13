package com.movieproject.ui.detail

import app.cash.turbine.test
import com.movieproject.data.repository.MoviesRepository
import com.movieproject.data.repository.RepositoryResult
import com.movieproject.ui.MovieInfo
import com.movieproject.BaseViewModelTest
import com.movieproject.ui.main.UIState.Error
import com.movieproject.ui.main.UIState.Loading
import com.movieproject.ui.main.UIState.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailsViewModelTest : BaseViewModelTest() {

    private val repository = mockk<MoviesRepository>()

    private val viewModel = DetailsViewModel(repository)

    @Test
    fun `should return true if initial state is Loading`() {
        assertTrue(viewModel.getState().value == Loading)
    }

    @Test
    fun `getMovieInfoState should return success`() = runTest {
        val mockkMovieInfo = mockk<MovieInfo>()

        coEvery { repository.getMovieInfoData(any()) } returns RepositoryResult.Success(mockkMovieInfo)

        viewModel.getState().test {
            viewModel.loadMovieInfo(1)
            assertEquals(Loading, awaitItem())
            val expectedSuccess = awaitItem()
            assertTrue(expectedSuccess is Success)
            assertEquals((expectedSuccess as Success<MovieInfo>).data, mockkMovieInfo)
        }
    }

    @Test
    fun `getMovieInfoState should return error`() = runTest {
        val error = Throwable("test error")
        coEvery { repository.getMovieInfoData(any()) } returns RepositoryResult.Error(error)

        val movieId = 1
        viewModel.getState().test {
            viewModel.loadMovieInfo(movieId)

            assertTrue(awaitItem() is Loading)
            val expectedError = awaitItem()
            assertTrue(expectedError is Error)
            assertEquals((expectedError as Error).error, error)
            coVerify { repository.getMovieInfoData(movieId) }
        }
    }
}
