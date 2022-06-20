package com.fanulhaq.githubuser.db

import com.fanulhaq.githubuser.data.local.detail.DetailDao
import com.fanulhaq.githubuser.data.local.detail.DetailModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailDaoFake(
    private val db: AppDatabaseFake
): DetailDao {
    override suspend fun insert(data: DetailModel) {
        db.detail.add(data)
    }

    override suspend fun deleteWithUsername(username: String) {
        val delete = db.detail.filter { it.username == username }
        delete.forEach {
            db.detail.remove(it)
        }
    }

    override fun getDataWithUsername(username: String): Flow<DetailModel> {
        val data = db.detail.first { it.username == username }
        return flow { emit(data) }
    }
}