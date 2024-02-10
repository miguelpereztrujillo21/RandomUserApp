package com.example.randomuserapp.helpers

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.randomuserapp.models.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UtilsTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val context = mock(Context::class.java)

    @Before
    fun setup() {


    }

    @Test
    fun testUtilsGetFullName() {
        val firstName = "Miguel"
        val LastName = "Perez"



    }

}