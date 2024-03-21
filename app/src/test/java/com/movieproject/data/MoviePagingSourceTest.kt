package com.movieproject.data

import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult
import com.movieproject.data.model.DataResult
import com.movieproject.data.model.MovieModel
import com.movieproject.data.repository.BASE_IMAGE_URL
import com.movieproject.ui.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class MoviePagingSourceTest {

    private val movieService = mockk<MovieService>()
    private val moviePagingSource = MoviePagingSource(movieService)

    @Test
    fun `MoviePagingSource refresh should return Success`() = runTest {
        val mockMovieModel = mockk<MovieModel>(relaxed = true)
        val response = listOf(mockMovieModel)

        val expectedResult =
            LoadResult.Page(
                data = response.map { it.mapToMovie() },
                prevKey = null,
                nextKey = 2
            )

        coEvery { movieService.getMovies(any()) } returns DataResult(response)

        val actualResult = moviePagingSource.load(
            Refresh(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedResult, actualResult)
        coVerify { movieService.getMovies(any()) }
    }

    @Test
    fun `MoviePagingSource append should return Success`() = runTest {
        val mockMovieModel = mockk<MovieModel>(relaxed = true)
        val response = listOf(mockMovieModel)

        val expectedResult =
            LoadResult.Page(
                data = response.map { it.mapToMovie() },
                prevKey = null,
                nextKey = 2
            )

        coEvery { movieService.getMovies(any()) } returns DataResult(response)

        val actualResult = moviePagingSource.load(
            Append(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `MoviePagingSource should return Error`() = runTest {
        val testError = RuntimeException("test error")

        coEvery { movieService.getMovies(any()) } throws testError

        val actualResult = moviePagingSource.load(
            Refresh(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertEquals(testError, (actualResult as LoadResult.Error).throwable)
        coVerify { movieService.getMovies(any()) }
    }

    @Test
    fun `check mapping from MovieModel to Movie`() = runTest {
        val movieModel = MovieModel(1, "title", "url", "2020")
        val expected = Movie(1, "title", BASE_IMAGE_URL + "url", "2020")

        val actual = movieModel.mapToMovie()
        assertEquals(expected, actual)
    }
}

