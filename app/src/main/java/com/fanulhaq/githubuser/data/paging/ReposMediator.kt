package com.fanulhaq.githubuser.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fanulhaq.githubuser.data.local.RoomDB
import com.fanulhaq.githubuser.data.local.pagekey.PageKey
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.service.GithubApi
import com.fanulhaq.githubuser.ext.countDateUpdate
import com.fanulhaq.githubuser.ext.numberShortFormatter
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1
const val NETWORK_PAGE_SIZE = 20

@ExperimentalPagingApi
class ReposMediator(
    private val service: GithubApi,
    private val db: RoomDB,
    private val username: String
) : RemoteMediator<Int, ReposModel>() {

    private val reposDao = db.reposDao()
    private val pageKeyDao = db.pageKeyDao()

    private var beforePage = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReposModel>
    ): MediatorResult {
        return try {
            var page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    getRemoteKey(state)?.next ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            if(page == beforePage) page += 1 else beforePage = page

            Log.d("ReposPagingLog", "page: $page -- beforePage $beforePage")

            val response = remoteData(page)
            response?.map {
                it.username = username
                it.star = it.star?.toInt()?.numberShortFormatter()
                it.updated_at = it.updated_at?.countDateUpdate()
            }
            val endOfPaginationReached = response?.size ?: 0 < NETWORK_PAGE_SIZE

            Log.d("ReposPagingLog", "page: $page -- DONE $endOfPaginationReached")

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pageKeyDao.deleteWithUsername(username)
                    reposDao.deleteWithUsername(username)
                }
                val prevKey = if (page == STARTING_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response?.map {
                    PageKey(it.id, username, prevKey, nextKey)
                }
                keys?.let { pageKeyDao.insertAll(it) }
                response?.let { reposDao.insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKey(state: PagingState<Int, ReposModel>) : PageKey? {
        val lastItem = state.lastItemOrNull()
        return db.withTransaction {
            if(lastItem?.id != null) {
                pageKeyDao.getRemoteKeyWithUsername(lastItem.id, username)
            } else pageKeyDao.getLastRemoteKeyWithUsername(username)
        }
    }

    private suspend fun remoteData(page: Int): List<ReposModel>? {
        val response = service.repos(username = username, page = page)
        if (response.isSuccessful) {
            return response.body()
        } else throw HttpException(response)
    }

}