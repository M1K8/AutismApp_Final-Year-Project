package com.m1k.fyp

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.content.Context

@Entity data class UserTable(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "uName") var uname: String,
                       @ColumnInfo(name = "uName") var pwd: String



){
    constructor():this(null,"","")
}


@Dao interface UserDBDao {

    @Query("SELECT * from UserTable")
    fun getAll(): List<UserTable>

    @Insert(onConflict = REPLACE)
    fun insert(weatherData: UserTable)

    @Query("DELETE from UserTable")
    fun deleteAll()
}

@Database(entities = arrayOf(UserTable::class), version = 1)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDataDao(): UserDBDao

    companion object {
        private var INSTANCE: UserDataBase? = null

        fun getInstance(context: Context): UserDataBase? {
            if (INSTANCE == null) {
                synchronized(UserDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,  UserDataBase::class.java, "user.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}