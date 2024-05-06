package com.mpt.randomuserapp.api

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mpt.randomuserapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.max

class UserPagingSource @Inject constructor(
    private val apiRepository: ApiRepository,
    private var filterEmail: String?,
    private var filterGender: String?
) : PagingSource<Int, User>() {

    private val startingKey = 1
    private val uniqueUsers = hashSetOf<String>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val start = params.key ?: startingKey
        return withContext(Dispatchers.IO) {
            try {
                val response = apiRepository.getUsers(start, 10, filterGender)
                val users = response.results ?: emptyList()

                val filteredUsers = users.asSequence().filter(::matchesFilter).toList()
                LoadResult.Page(
                    data = filteredUsers,
                    prevKey = if (start == startingKey) null else ensureValidKey(start - params.loadSize),
                    nextKey = if (users.isNotEmpty()) start + params.loadSize else null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val user = state.closestItemToPosition(anchorPosition) ?: return null

        return ensureValidKey(user.id.value.toInt() - (state.config.pageSize / 2))
    }

    private fun ensureValidKey(key: Int) = max(startingKey, key)

    private fun matchesFilter(user: User): Boolean {
        val localFilterEmail = filterEmail
        return (localFilterEmail == null || user.email.contains(localFilterEmail, ignoreCase = true))
                && uniqueUsers.add("${user.login.username}${user.email}")
    }
}
