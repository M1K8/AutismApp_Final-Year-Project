package com.m1k.fyp

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.content.Context

@Entity data class UserTable(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "uName") var uname: String,
                       @ColumnInfo(name = "pwd") var pwd: String
)

@Dao interface UserDBDao {

    @Query("SELECT * from UserTable")
    fun getAll(): LiveData<List<UserTable>>

    @Insert(onConflict = REPLACE)
    fun insert(weatherData: UserTable)

    @Query("DELETE from UserTable")
    fun deleteAll()
}

@Database(entities = [UserTable::class], version = 1, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDataDao(): UserDBDao

    companion object {
        @Volatile
        private var INSTANCE: UserDataBase? = null

        fun getDatabase(context: Context): UserDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "user.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

