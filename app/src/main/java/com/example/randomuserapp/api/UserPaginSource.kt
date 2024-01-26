package com.example.randomuserapp.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.randomuserapp.models.User
import kotlin.math.max
class UserPagingSource(private val apiRepository: ApiRepository) : PagingSource<Int, User>() {

    private val STARTING_KEY = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val start = params.key ?: STARTING_KEY

        return try {
            val response = apiRepository.getUsers(start)
            val users = response.results ?: emptyList()

            LoadResult.Page(
                data = users,
                prevKey = if (start == STARTING_KEY) null else ensureValidKey(start - params.loadSize),
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

    fun refresh() {
        invalidate()
    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}
