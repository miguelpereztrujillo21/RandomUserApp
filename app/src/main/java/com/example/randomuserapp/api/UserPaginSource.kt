package com.example.randomuserapp.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.randomuserapp.models.User
import kotlin.math.max

class UserPagingSource(
    private val apiRepository: ApiRepository,
    private val filterEmail: String?
) : PagingSource<Int, User>() {

    private val startingKey = 1
    private val uniqueUsers = hashSetOf<String>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val start = params.key ?: startingKey

        return try {
            val response = apiRepository.getUsers(start, 10)
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
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val user = state.closestItemToPosition(anchorPosition) ?: return null

        return ensureValidKey(user.id.value.toInt() - (state.config.pageSize / 2))
    }

/*    fun refresh() {
        uniqueUsers.clear()
        invalidate()
    }*/

    private fun ensureValidKey(key: Int) = max(startingKey, key)
}