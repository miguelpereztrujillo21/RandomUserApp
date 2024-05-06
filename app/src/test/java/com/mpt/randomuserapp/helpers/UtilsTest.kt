package com.mpt.randomuserapp.helpers

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mpt.randomuserapp.R
import com.mpt.randomuserapp.models.Name
import com.mpt.randomuserapp.models.User
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UtilsTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    private var mockContext: Context = mock(Context::class.java)
    private lateinit var utils:  Utils

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockContext = mock(Context::class.java)
        utils = Utils(mockContext)
    }

    @Test
    fun testUtilsGetFullName() {
        val firstName = "Miguel"
        val lastName = "Perez"
        val userMock = mock(User::class.java)

        `when`(userMock.name).thenReturn(Name("", firstName, lastName))
        `when`(mockContext.getString(R.string.user_full_name, firstName, lastName)).thenReturn("$firstName $lastName")
        assertNotNull(mockContext)
        val fullname = utils.getUserFullName(userMock)
        assertThat(fullname).isEqualTo("Miguel Perez")
    }

    @Test
    fun testFormatDateStringValid() {
        val inputDate = "2024-02-10T10:15:30.000Z"

        val formattedDate = utils.formatDateString(inputDate)

        assertEquals("10/02/2024", formattedDate)
    }
    @Test
    fun testFormatDateString_InvalidInput() {
        val inputDate = "fecha_invalida"

        val formattedDate = utils.formatDateString(inputDate)

        assertEquals("", formattedDate)
    }

    @Test
    fun testFormatGender_Male() {
        `when`(mockContext.getString(R.string.simple_male)).thenReturn("Hombre")

        val formattedGender = utils.formarGender(Constants.MALE_KEY)

        assertEquals("Hombre", formattedGender)
    }

    @Test
    fun testFormatGender_Female() {
        `when`(mockContext.getString(R.string.simple_female)).thenReturn("Mujer")

        val formattedGender = utils.formarGender(Constants.FEMALE_KEY)

        assertEquals("Mujer", formattedGender)
    }

    @Test
    fun testFormatGender_Other() {
        val formattedGender = utils.formarGender("Otro")

        assertEquals("Otro", formattedGender)
    }
}