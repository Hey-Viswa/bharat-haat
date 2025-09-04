package com.optivus.bharathaat.data.local.dao

import androidx.room.*
import com.optivus.bharathaat.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUserById(uid: String): UserEntity?

    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserByIdFlow(uid: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE role = :role")
    suspend fun getUsersByRole(role: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE role = :role")
    fun getUsersByRoleFlow(role: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteUserById(uid: String)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}
