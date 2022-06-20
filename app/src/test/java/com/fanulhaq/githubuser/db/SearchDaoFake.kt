package com.fanulhaq.githubuser.db

import com.fanulhaq.githubuser.data.local.search.SearchDao
import com.fanulhaq.githubuser.data.local.search.SearchModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchDaoFake(
    private val db: AppDatabaseFake
): SearchDao {
    override suspend fun insertAll(data: List<SearchModel>) {
        db.search.addAll(data)
    }

    override fun getDataWithKey(key: String): Flow<List<SearchModel>> {
        val data = db.search.filter { it.username == key }
        return flow { emit(data) }
    }

    override suspend fun deleteWithKey(key: String) {
        val delete = db.search.filter { it.username == key }
        delete.forEach {
            db.search.remove(it)
        }
    }

}