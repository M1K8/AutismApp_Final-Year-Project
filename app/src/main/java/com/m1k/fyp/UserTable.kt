package com.m1k.fyp

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.content.Context

@Entity data class UserTable(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "uName") var uName: String,
                       @ColumnInfo(name = "draw_vibrate") var draw_vibrate: Boolean,
                       @ColumnInfo(name = "general_haptic") var general_vibrate: Boolean,
                       @ColumnInfo(name = "txt_speech") var txt2Speech: Boolean,
                       @ColumnInfo (name = "pic_path") var picPath : String?)

@Dao interface UserDBDao {

    @Query("SELECT * from UserTable")
    fun getAll(): LiveData<List<UserTable>>

    @Query("SELECT * from UserTable where uName =  :name ")
    fun getByUserName(name : String) : UserTable

    @Insert(onConflict = REPLACE)
    fun insert(userData: UserTable)

    @Query("DELETE from UserTable")
    fun deleteAll()

    @Update
    fun updateUser(userData : UserTable)
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

