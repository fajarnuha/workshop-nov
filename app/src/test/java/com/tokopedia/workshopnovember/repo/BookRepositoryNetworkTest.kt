package com.tokopedia.workshopnovember.repo

import com.tokopedia.workshopnovember.repo.cloud.BookApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookRepositoryNetworkTest {

    private val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://openlibrary.org").build()
        .create(BookApi::class.java)

    private val sut = BookRepository(api)

    @Test
    fun searchWithQuery() {
        runBlocking {
            val result = sut.searchWithQuery("lord of the ring")
            val listOfFirstAuthor = result.map {
                it.authorName?.first()
            }

            val listOfFirstIsbn = result.map {
                it.isbn?.first()
            }
            print(
                """author nulls: ${listOfFirstAuthor.filter { it == null }.size}
                    |isbn nulls: ${listOfFirstIsbn.filter { it == null }.size}""".trimMargin()
            )

            assertEquals(25, result.size)
        }
    }

}