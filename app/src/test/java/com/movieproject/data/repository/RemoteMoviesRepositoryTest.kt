package com.movieproject.data.repository

import com.movieproject.data.MovieService
import com.movieproject.data.model.MovieInfoModel
import com.movieproject.data.repository.RepositoryResult.Success
import com.movieproject.ui.MovieInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteMoviesRepositoryTest {

    private val movieService = mockk<MovieService>()
    private val remoteMoviesRepository = RemoteMoviesRepository(movieService)

    @Test
    fun `getMovieInfoData should return success`() = runTest {
        val movieInfoModel = mockk<MovieInfoModel>(relaxed = true)

        coEvery { movieService.getMovieInfo(any()) } returns movieInfoModel

        val movieId = 1
        val expected = remoteMoviesRepository.getMovieInfoData(movieId)
        assertTrue(expected is Success)
        coVerify { movieService.getMovieInfo(movieId) }
    }

    @Test
    fun `getMovieInfoData should return error`() = runTest {
        val testError = Throwable("test error")

        coEvery { movieService.getMovieInfo(any()) } throws testError

        val movieId = 1
        val errorExpected = remoteMoviesRepository.getMovieInfoData(movieId)
        assertTrue(errorExpected is RepositoryResult.Error)
        assertEquals((errorExpected as RepositoryResult.Error).error, testError)
        coVerify { movieService.getMovieInfo(movieId) }
    }

    @Test
    fun `check mapping from MovieInfoModel to MovieInfo`() = runTest {
        val movieInfoModel = MovieInfoModel(
            "title", "url", "2000", 1, 1,
            1, 1.1, 1, listOf(), "overview",
        )
        val expectedMovieInfo = MovieInfo(
            "title", BASE_IMAGE_URL + "url", "2000", 1, 1,
            1, 1.1, 1, listOf(), "overview",
        )

        val actualMovieInfo = movieInfoModel.mapToMovieInfo()
        assertEquals(expectedMovieInfo, actualMovieInfo)
    }
}

