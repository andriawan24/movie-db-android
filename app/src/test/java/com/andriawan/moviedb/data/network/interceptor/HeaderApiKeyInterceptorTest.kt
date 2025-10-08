package com.andriawan.moviedb.data.network.interceptor

import com.andriawan.moviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HeaderApiKeyInterceptorTest {

    private lateinit var interceptor: HeaderApiKeyInterceptor
    private lateinit var chain: Interceptor.Chain
    private lateinit var request: Request
    private lateinit var requestBuilder: Request.Builder
    private lateinit var response: Response

    @Before
    fun setup() {
        interceptor = HeaderApiKeyInterceptor()
        chain = mock()
        request = mock()
        requestBuilder = mock()
        response = mock()
    }

    @Test
    fun `intercept should add Authorization header with Bearer token`() {
        // Given
        val expectedApiKey = BuildConfig.MOVIE_API_KEY
        val expectedAuthHeader = "Bearer $expectedApiKey"
        val newRequest = mock<Request>()

        whenever(chain.request()).thenReturn(request)
        whenever(request.newBuilder()).thenReturn(requestBuilder)
        whenever(requestBuilder.addHeader("Authorization", expectedAuthHeader)).thenReturn(
            requestBuilder
        )
        whenever(requestBuilder.build()).thenReturn(newRequest)
        whenever(chain.proceed(newRequest)).thenReturn(response)

        // When
        val result = interceptor.intercept(chain)

        // Then
        verify(chain).request()
        verify(request).newBuilder()
        verify(requestBuilder).addHeader("Authorization", expectedAuthHeader)
        verify(requestBuilder).build()
        verify(chain).proceed(newRequest)
        assert(result == response)
    }

    @Test
    fun `intercept should proceed with modified request`() {
        // Given
        val newRequest = mock<Request>()

        whenever(chain.request()).thenReturn(request)
        whenever(request.newBuilder()).thenReturn(requestBuilder)
        whenever(
            requestBuilder.addHeader(
                org.mockito.kotlin.any(),
                org.mockito.kotlin.any()
            )
        ).thenReturn(requestBuilder)
        whenever(requestBuilder.build()).thenReturn(newRequest)
        whenever(chain.proceed(newRequest)).thenReturn(response)

        // When
        interceptor.intercept(chain)

        // Then
        verify(chain).proceed(newRequest)
    }

    @Test
    fun `intercept should return response from chain`() {
        // Given
        val newRequest = mock<Request>()

        whenever(chain.request()).thenReturn(request)
        whenever(request.newBuilder()).thenReturn(requestBuilder)
        whenever(
            requestBuilder.addHeader(
                org.mockito.kotlin.any(),
                org.mockito.kotlin.any()
            )
        ).thenReturn(requestBuilder)
        whenever(requestBuilder.build()).thenReturn(newRequest)
        whenever(chain.proceed(newRequest)).thenReturn(response)

        // When
        val result = interceptor.intercept(chain)

        // Then
        assert(result == response)
    }
}