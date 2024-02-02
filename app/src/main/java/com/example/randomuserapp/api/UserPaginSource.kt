package com.example.randomuserapp.api



import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.randomuserapp.models.User
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import kotlin.math.max

class UserPagingSource(
    private val apiRepository: ApiRepository,
    private val filterEmail: String?,
    private val filterGender: String?
) : PagingSource<Int, User>() {

    private val startingKey = 1
    private val uniqueUsers = hashSetOf<String>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val start = params.key ?: startingKey

        return withContext(Dispatchers.IO) { // Ejecutar en un hilo de E/S
            try {
                val response = apiRepository.getUsers(start, 10, filterGender)
                val users = response.results ?: emptyList()
                val filteredUsers = users.filter {
                    (filterEmail == null || it.email.contains(filterEmail, ignoreCase = true)) &&
                            uniqueUsers.add("${it.login.username}${it.email}")
                }
                LoadResult.Page(
                    data = filteredUsers,
                    prevKey = if (start == startingKey) null else ensureValidKey(start - params.loadSize),
                    nextKey = if (users.isNotEmpty()) start + params.loadSize else null
                )
            } catch (e: Exception) {
                Log.e("RamdomUserApp","Error en UserPaginPaginSource",e)
                throw  e
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val user = state.closestItemToPosition(anchorPosition) ?: return null

        return ensureValidKey(user.id.value.toInt() - (state.config.pageSize / 2))
    }

    private fun ensureValidKey(key: Int) = max(startingKey, key)
}
