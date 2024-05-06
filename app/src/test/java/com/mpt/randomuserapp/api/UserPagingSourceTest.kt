package com.mpt.randomuserapp.api

import android.nfc.tech.MifareUltralight
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class UserPagingSourceTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiRepository: ApiRepositoryImpl
    private lateinit var userPagingSource: UserPagingSource

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockWebServer = MockWebServer()
        Dispatchers.setMain(Dispatchers.Unconfined)
        apiRepository = ApiRepositoryImpl(
            Retrofit.Builder().baseUrl(mockWebServer.url("/"))//Pass any base url like this
                .addConverterFactory(GsonConverterFactory.create()).build().create(Api::class.java)
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLoadDataFromPagingSourceSuccess(): Unit = runBlocking {
        val mockResponse = createSuccessResponse()
        mockWebServer.enqueue(mockResponse)
        userPagingSource = UserPagingSource(apiRepository,null,null)
        delay(1000)

        val loadParams = PagingSource.LoadParams.Refresh(key = 0, loadSize = MifareUltralight.PAGE_SIZE, placeholdersEnabled = false)
        val loadResult = userPagingSource.load(loadParams)

        val dataList = (loadResult as? PagingSource.LoadResult.Page)?.data
        assertThat(dataList).isNotEmpty()

        val firstUser = dataList?.firstOrNull()
        assertThat(firstUser?.email).isNotNull()
        assertThat(firstUser?.email).isEqualTo("hunter.slawa@mpt.com")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun whenGetLoadResulError(): Unit = runBlocking {
       val mockResponse = createErrorResponse()
        mockWebServer.enqueue(mockResponse)
        userPagingSource = UserPagingSource(apiRepository,null,null)
        delay(2000)

        val loadResult = userPagingSource.load(PagingSource.LoadParams.Refresh( 0,  MifareUltralight.PAGE_SIZE, false))
        if( loadResult is PagingSource.LoadResult.Error){
            val exception = loadResult.throwable
            assertThat(exception).isNotNull()
            assertThat(exception.message).isEqualTo("Error en la solicitud: 404 + Uh oh, something has gone wrong. Please tweet us @randomapi about the issue. Thank you.")
        }
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

    private fun createSuccessResponse(): MockResponse {
        val json = """
{
    "results": [
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Hunter",
                "last": "Slawa"
            },
            "location": {
                "street": {
                    "number": 4344,
                    "name": "Dufferin St"
                },
                "city": "Cadillac",
                "state": "Newfoundland and Labrador",
                "country": "Canada",
                "postcode": "J7K 4X3",
                "coordinates": {
                    "latitude": "-67.3148",
                    "longitude": "-144.1575"
                },
                "timezone": {
                    "offset": "+4:30",
                    "description": "Kabul"
                }
            },
            "email": "hunter.slawa@mpt.com",
            "login": {
                "uuid": "6da66154-7072-41eb-8fc8-bcf9561352bc",
                "username": "silverleopard762",
                "password": "wheels",
                "salt": "DbBkg4QH",
                "md5": "6363cdfeaf93eb97e8f984aa38e1448b",
                "sha1": "568bcefd8d6a7546dae2302384f2485e6cf5e40c",
                "sha256": "3db7317b82a2194e8708e82da65fc9a2341ea734b440bd4e16e8a76010adb113"
            },
            "dob": {
                "date": "1989-03-10T15:58:35.365Z",
                "age": 34
            },
            "registered": {
                "date": "2022-04-05T01:12:14.315Z",
                "age": 1
            },
            "phone": "J40 F31-7717",
            "cell": "G56 M95-6201",
            "id": {
                "name": "SIN",
                "value": "442397360"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/57.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/57.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/57.jpg"
            },
            "nat": "CA"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Gavin",
                "last": "Cook"
            },
            "location": {
                "street": {
                    "number": 1950,
                    "name": "Taylor St"
                },
                "city": "Townsville",
                "state": "South Australia",
                "country": "Australia",
                "postcode": 3994,
                "coordinates": {
                    "latitude": "-60.3515",
                    "longitude": "79.8251"
                },
                "timezone": {
                    "offset": "+9:00",
                    "description": "Tokyo, Seoul, Osaka, Sapporo, Yakutsk"
                }
            },
            "email": "gavin.cook@mpt.com",
            "login": {
                "uuid": "38a7e8a3-02da-4f1b-8726-5dc5d6976c3a",
                "username": "silversnake287",
                "password": "bond007",
                "salt": "ACsAAbLR",
                "md5": "ab98ec76c2b943464496ae6a23b6a104",
                "sha1": "90b68eb930c482d00b257cc0d291c9cee83d5503",
                "sha256": "c1c995dc7268fc32564715d1761580f20a14a1fb4e0da08a9251ae5af183d1e4"
            },
            "dob": {
                "date": "1960-04-21T06:22:30.541Z",
                "age": 63
            },
            "registered": {
                "date": "2009-10-01T04:21:17.468Z",
                "age": 14
            },
            "phone": "08-4460-2138",
            "cell": "0441-371-256",
            "id": {
                "name": "TFN",
                "value": "979200907"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/42.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/42.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/42.jpg"
            },
            "nat": "AU"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Adolfo",
                "last": "Mora"
            },
            "location": {
                "street": {
                    "number": 3885,
                    "name": "Calle de Pedro Bosch"
                },
                "city": "Castellón de la Plana",
                "state": "Comunidad de Madrid",
                "country": "Spain",
                "postcode": 90392,
                "coordinates": {
                    "latitude": "75.3269",
                    "longitude": "-5.6264"
                },
                "timezone": {
                    "offset": "-12:00",
                    "description": "Eniwetok, Kwajalein"
                }
            },
            "email": "adolfo.mora@mpt.com",
            "login": {
                "uuid": "b71449b1-75e8-4a66-b80c-74d83f71dcf5",
                "username": "happyleopard609",
                "password": "trebor",
                "salt": "hrjQu6mC",
                "md5": "e2ff4568d3920cc4a697f8279598cd08",
                "sha1": "33ffd931210d3d85d38d2a503d2a4085cdeb0895",
                "sha256": "7795d96b8599c81f2a9e073420cbb05376f12172e0e7db2ec5a47cea1082f310"
            },
            "dob": {
                "date": "1986-10-22T10:13:06.288Z",
                "age": 37
            },
            "registered": {
                "date": "2018-02-11T08:22:35.021Z",
                "age": 5
            },
            "phone": "944-324-788",
            "cell": "615-624-998",
            "id": {
                "name": "DNI",
                "value": "77358709-P"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/65.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/65.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/65.jpg"
            },
            "nat": "ES"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Chris",
                "last": "Young"
            },
            "location": {
                "street": {
                    "number": 9587,
                    "name": "N Stelling Rd"
                },
                "city": "Geelong",
                "state": "Queensland",
                "country": "Australia",
                "postcode": 8388,
                "coordinates": {
                    "latitude": "16.3619",
                    "longitude": "-144.6461"
                },
                "timezone": {
                    "offset": "-6:00",
                    "description": "Central Time (US & Canada), Mexico City"
                }
            },
            "email": "chris.young@mpt.com",
            "login": {
                "uuid": "91b8df30-ba99-4600-b0b8-87fadf7734fb",
                "username": "brownbear915",
                "password": "track",
                "salt": "4aFIdvkX",
                "md5": "3b7ba0e4f0dd93a873f665a266e9f8b1",
                "sha1": "14db8c1a93c0f56bcc859912759d90ecdfbefb8a",
                "sha256": "f3746e6573d1ecf58f30d0d4b9c9b58ef583b0492a336de9934ff2e82c6bf734"
            },
            "dob": {
                "date": "1991-03-24T15:51:08.680Z",
                "age": 32
            },
            "registered": {
                "date": "2012-07-09T07:56:50.964Z",
                "age": 11
            },
            "phone": "06-3098-4870",
            "cell": "0437-271-097",
            "id": {
                "name": "TFN",
                "value": "029378536"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/77.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/77.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/77.jpg"
            },
            "nat": "AU"
        },
        {
            "gender": "female",
            "name": {
                "title": "Ms",
                "first": "Anna",
                "last": "Flores"
            },
            "location": {
                "street": {
                    "number": 4855,
                    "name": "Calle del Pez"
                },
                "city": "San Sebastián de Los Reyes",
                "state": "País Vasco",
                "country": "Spain",
                "postcode": 77453,
                "coordinates": {
                    "latitude": "-26.3828",
                    "longitude": "46.0775"
                },
                "timezone": {
                    "offset": "-11:00",
                    "description": "Midway Island, Samoa"
                }
            },
            "email": "anna.flores@mpt.com",
            "login": {
                "uuid": "05c67e40-652e-43cd-9167-46609b2bd345",
                "username": "orangedog966",
                "password": "dogfood",
                "salt": "RPevSuDX",
                "md5": "34317ec602d0bc790e5c45ca2a4f1eb5",
                "sha1": "e0f17d0cd08ce8ce3b51b0ad002a16b734810e0f",
                "sha256": "2bf80cdc980fe20e1ee524880ed688a43fc7a89995a37df158675551f5210a95"
            },
            "dob": {
                "date": "1999-06-02T03:49:58.351Z",
                "age": 24
            },
            "registered": {
                "date": "2014-06-16T15:37:20.023Z",
                "age": 9
            },
            "phone": "961-013-906",
            "cell": "667-372-265",
            "id": {
                "name": "DNI",
                "value": "50919739-G"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/women/55.jpg",
                "medium": "https://randomuser.me/api/portraits/med/women/55.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/women/55.jpg"
            },
            "nat": "ES"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Jackson",
                "last": "Reyes"
            },
            "location": {
                "street": {
                    "number": 2235,
                    "name": "Killarney Road"
                },
                "city": "Thurles",
                "state": "Leitrim",
                "country": "Ireland",
                "postcode": 48810,
                "coordinates": {
                    "latitude": "-13.5703",
                    "longitude": "121.8301"
                },
                "timezone": {
                    "offset": "+7:00",
                    "description": "Bangkok, Hanoi, Jakarta"
                }
            },
            "email": "jackson.reyes@mpt.com",
            "login": {
                "uuid": "67a0325d-735b-4138-aebe-311e0b647090",
                "username": "redkoala785",
                "password": "mohawk",
                "salt": "1STZYEPZ",
                "md5": "d0bd9a49a65dcfa2da3cf21a85282d0b",
                "sha1": "c02d497a634b1e3fc8ce73cb56473edd459b7cfd",
                "sha256": "f91222e395ec69bf78a6a2e70694f6888e99ea5d851987608a574fe6a49f498e"
            },
            "dob": {
                "date": "1993-06-17T17:52:14.187Z",
                "age": 30
            },
            "registered": {
                "date": "2019-01-30T01:24:37.988Z",
                "age": 5
            },
            "phone": "051-811-8612",
            "cell": "081-438-3490",
            "id": {
                "name": "PPS",
                "value": "9057749T"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/69.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/69.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/69.jpg"
            },
            "nat": "IE"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Philippe",
                "last": "Miller"
            },
            "location": {
                "street": {
                    "number": 7383,
                    "name": "Coastal Highway"
                },
                "city": "Bath",
                "state": "Québec",
                "country": "Canada",
                "postcode": "R4W 5V6",
                "coordinates": {
                    "latitude": "-20.0735",
                    "longitude": "23.2939"
                },
                "timezone": {
                    "offset": "+4:30",
                    "description": "Kabul"
                }
            },
            "email": "philippe.miller@mpt.com",
            "login": {
                "uuid": "28e242a5-e37e-4689-bca7-7436bee29936",
                "username": "lazykoala515",
                "password": "kristen",
                "salt": "0s7b65jI",
                "md5": "8d0b3facd023f95144303f427a6bce01",
                "sha1": "2bcb1871b3342717ac20ad756783c72097db9c1e",
                "sha256": "11447c26cefede0feea51564bef905fe97faf6a3920c725c8fcff4e108f3158c"
            },
            "dob": {
                "date": "1987-06-27T13:48:12.767Z",
                "age": 36
            },
            "registered": {
                "date": "2006-01-23T12:55:25.995Z",
                "age": 18
            },
            "phone": "B41 E88-8833",
            "cell": "Y89 E22-3320",
            "id": {
                "name": "SIN",
                "value": "349518613"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/83.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/83.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/83.jpg"
            },
            "nat": "CA"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Esat",
                "last": "Koyuncu"
            },
            "location": {
                "street": {
                    "number": 5064,
                    "name": "Tunalı Hilmi Cd"
                },
                "city": "Kırklareli",
                "state": "Trabzon",
                "country": "Turkey",
                "postcode": 48370,
                "coordinates": {
                    "latitude": "75.8355",
                    "longitude": "152.6481"
                },
                "timezone": {
                    "offset": "+2:00",
                    "description": "Kaliningrad, South Africa"
                }
            },
            "email": "esat.koyuncu@mpt.com",
            "login": {
                "uuid": "cbb58dc1-f902-42f3-b867-2e088052ce40",
                "username": "redduck311",
                "password": "soul",
                "salt": "2Wy9hkxh",
                "md5": "4cff9129d60feab2ccc8a7460d86f1f4",
                "sha1": "049ffd2b304816a7939d629a60f2d81d0f36abd4",
                "sha256": "6dde9706c8e82e6bedb180091d323fd973647ecbc4d448f76c4e20078df1dca5"
            },
            "dob": {
                "date": "1982-07-03T23:15:49.899Z",
                "age": 41
            },
            "registered": {
                "date": "2019-08-18T22:12:04.097Z",
                "age": 4
            },
            "phone": "(071)-677-5816",
            "cell": "(893)-857-3795",
            "id": {
                "name": "",
                "value": null
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/27.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/27.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/27.jpg"
            },
            "nat": "TR"
        },
        {
            "gender": "male",
            "name": {
                "title": "Mr",
                "first": "Dimitri",
                "last": "Treurniet"
            },
            "location": {
                "street": {
                    "number": 8797,
                    "name": "Harm Nijholtstraat"
                },
                "city": "Camerig",
                "state": "Noord-Brabant",
                "country": "Netherlands",
                "postcode": "8338 JE",
                "coordinates": {
                    "latitude": "73.0255",
                    "longitude": "115.4287"
                },
                "timezone": {
                    "offset": "-9:00",
                    "description": "Alaska"
                }
            },
            "email": "dimitri.treurniet@mpt.com",
            "login": {
                "uuid": "8ca41ed7-db15-4c4b-a0fc-13f5cd95ad59",
                "username": "smallelephant671",
                "password": "closer",
                "salt": "TT0tlwwk",
                "md5": "abbc6aef6f9c4984a0e721e3a1510fbc",
                "sha1": "3e4b494035eb99ca25b4b992f458be7de919b639",
                "sha256": "7bbac3b86f566406e2cd3977f15c04b9c771c051b2989a6028266d40bdd64ac0"
            },
            "dob": {
                "date": "1971-03-20T12:45:22.185Z",
                "age": 52
            },
            "registered": {
                "date": "2019-01-19T22:09:35.305Z",
                "age": 5
            },
            "phone": "(0066) 961611",
            "cell": "(06) 49158459",
            "id": {
                "name": "BSN",
                "value": "49949980"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/men/74.jpg",
                "medium": "https://randomuser.me/api/portraits/med/men/74.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/men/74.jpg"
            },
            "nat": "NL"
        },
        {
            "gender": "female",
            "name": {
                "title": "Mrs",
                "first": "Noelia",
                "last": "Flores"
            },
            "location": {
                "street": {
                    "number": 7439,
                    "name": "Calle de Alberto Aguilera"
                },
                "city": "Pontevedra",
                "state": "Aragón",
                "country": "Spain",
                "postcode": 25999,
                "coordinates": {
                    "latitude": "-73.7977",
                    "longitude": "-82.4889"
                },
                "timezone": {
                    "offset": "+5:30",
                    "description": "Bombay, Calcutta, Madras, New Delhi"
                }
            },
            "email": "noelia.flores@mpt.com",
            "login": {
                "uuid": "e17cbebb-a15b-457c-882f-f9992035f1aa",
                "username": "smallelephant138",
                "password": "gotribe",
                "salt": "cOz1E41t",
                "md5": "e32b07482df7a48cb7383af1d9d72300",
                "sha1": "373c35c682dc3d97ea1d1ea45280bb09b8d9e6f4",
                "sha256": "bea0d852b3c7d698aa03304b9d824d781d37d17782e6fcf4aa3a95e05d1904b6"
            },
            "dob": {
                "date": "1996-05-01T01:00:08.840Z",
                "age": 27
            },
            "registered": {
                "date": "2013-09-22T01:11:29.638Z",
                "age": 10
            },
            "phone": "905-758-975",
            "cell": "667-630-237",
            "id": {
                "name": "DNI",
                "value": "25004152-A"
            },
            "picture": {
                "large": "https://randomuser.me/api/portraits/women/94.jpg",
                "medium": "https://randomuser.me/api/portraits/med/women/94.jpg",
                "thumbnail": "https://randomuser.me/api/portraits/thumb/women/94.jpg"
            },
            "nat": "ES"
        }
    ],
    "info": {
        "seed": "6c995e2d70cb8de8",
        "results": 10,
        "page": 1,
        "version": "1.4"
    }
}
            """.trimIndent()
        return MockResponse().setResponseCode(200).setBody(json)
    }
    private fun createErrorResponse(): MockResponse {
        val json = """ 
            {
              "error": "Uh oh, something has gone wrong. Please tweet us @randomapi about the issue. Thank you."
            }
        """.trimIndent()
        return MockResponse().setResponseCode(404).setBody(json)
    }

}