package com.mpt.randomuserapp.modules.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mpt.randomuserapp.models.Coordinates
import com.mpt.randomuserapp.models.Dob
import com.mpt.randomuserapp.models.Id
import com.mpt.randomuserapp.models.Location
import com.mpt.randomuserapp.models.Login
import com.mpt.randomuserapp.models.Name
import com.mpt.randomuserapp.models.ProfilePicture
import com.mpt.randomuserapp.models.Registered
import com.mpt.randomuserapp.models.Street
import com.mpt.randomuserapp.models.Timezone
import com.mpt.randomuserapp.models.User
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProfileDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var userObserver: Observer<User>

    private lateinit var viewModel: ProfileDetailViewModel

    @Before
    fun setup() {
        viewModel = ProfileDetailViewModel()
    }

    @Test
    fun testGetUsersValid() {
        val userJSON = getmptUserJSON()
        val user = getmptUser()

        viewModel.user.observeForever(userObserver)
        viewModel.getUsers(userJSON)

        assertThat(viewModel.user.value).isNotNull()
        assertThat(viewModel.user.value?.email).isEqualTo(user.email)
    }

    @Test
    fun testGetUsersInvalidJSON(){
        val userJSON ="""hola"""

        viewModel.getUsers(userJSON)
        assertThat(viewModel.error.value).isNotNull()
        assertThat(viewModel.user.value).isNull()
    }

    private fun getmptUserJSON(): String {
        return """ {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Jozef",
                "last": "Lorenz"
            },
            "location": {
                "street": {
                    "number": 6661,
                    "name": "Brunnenstraße"
                },
                "city": "Eggesin",
                "state": "Schleswig-Holstein",
                "country": "Germany",
                "postcode": 19655,
                "coordinates": {
                    "latitude": "-47.9789",
                    "longitude": "-156.8078"
                },
                "timezone": {
                    "offset": "+3:30",
                    "description": "Tehran"
                }
            },
            "email": "jozef.lorenz@mpt.com",
            "login": {
                "uuid": "889a23e1-34a6-4771-b42f-c644ec29a79d",
                "username": "bluewolf820",
                "password": "doogie",
                "salt": "oqNAEBgB",
                "md5": "4a6cb4678f0207944e3c59c6f1962bdc",
                "sha1": "e7c9f89558fe51b65d63e4c7626e4d6a122872f0",
                "sha256": "625b88df9534f21bd19950515479aece9eb9afcde5e1624724bf3341fd997d7f"
            },
            "dob": {
                "date": "1969-03-07T06:08:44.483Z",
                "age": 54
            },
            "registered": {
                "date": "2007-06-22T13:41:11.091Z",
                "age": 16
            },
            "phone": "0981-5646860",
            "cell": "0170-8973998",
            "id": {
                "name": "SVNR",
                "value": "02 070369 L 219"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/28.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/28.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/28.jpg"
            },
            "nat": "DE"
        }""".trimIndent()
    }

    private fun getmptUser(): User {
        return User(
            gender = "male",
            name = Name(
                title = "Mr",
                first = "Jozef",
                last = "Lorenz"
            ),
            location = Location(
                street = Street(
                    number = 6661,
                    name = "Brunnenstraße"
                ),
                city = "Eggesin",
                state = "Schleswig-Holstein",
                country = "Germany",
                postcode = "19655",
                coordinates = Coordinates(
                    latitude = "-47.9789",
                    longitude = "-156.8078"
                ),
                timezone = Timezone(
                    offset = "+3:30",
                    description = "Tehran"
                )
            ),
            email = "jozef.lorenz@mpt.com",
            login = Login(
                uuid = "889a23e1-34a6-4771-b42f-c644ec29a79d",
                username = "bluewolf820",
                password = "doogie",
                salt = "oqNAEBgB",
                md5 = "4a6cb4678f0207944e3c59c6f1962bdc",
                sha1 = "e7c9f89558fe51b65d63e4c7626e4d6a122872f0",
                sha256 = "625b88df9534f21bd19950515479aece9eb9afcde5e1624724bf3341fd997d7f"
            ),
            dob = Dob(
                date = "1969-03-07T06:08:44.483Z",
                age = 54
            ),
            registered = Registered(
                date = "2007-06-22T13:41:11.091Z",
                age = 16
            ),
            phone = "0981-5646860",
            cell = "0170-8973998",
            id = Id(
                name = "SVNR",
                value = "02 070369 L 219"
            ),
            picture = ProfilePicture(
                large = "https://randomuser.me/api/portraits/men/28.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/28.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/28.jpg"
            ),
            nat = "DE"
        )
    }
}