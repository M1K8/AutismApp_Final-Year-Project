package com.m1k.fyp

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.content.Context
import kotlinx.coroutines.CoroutineScope


@Entity
class Calender()
{
    var wakeUp: String = ""
    var wakeUp_pic_path: String? = null

    var morning: String = ""
    var morning_pic_path: String? = null

    var lunchTime: String = ""
    var lunchTime_pic_path: String? = null

    var afternoon: String = ""
    var afternoon_pic_path: String? = null

    var evening: String = ""
    var evening_pic_path: String? = null

    var dinnerTime: String = ""
    var dinnerTime_pic_path: String? = null

    var bedTime: String = ""
    var bedTime_pic_path: String? = null

    constructor(w : String, m : String, l : String, a : String, e : String, d : String, b : String) : this() {
        wakeUp = w
        wakeUp_pic_path = null

        morning = m
        morning_pic_path = null

        lunchTime = l
        lunchTime_pic_path = null

        afternoon = a
        afternoon_pic_path = null

        evening= e
        evening_pic_path = null

        dinnerTime = d
        dinnerTime_pic_path = null

        bedTime = b
        bedTime_pic_path = null
    }
}

@Entity(tableName = "Users")
data class User(@PrimaryKey(autoGenerate = true) var id: Long?,
                        var uName: String,
                        var draw_vibrate: Boolean,
                        var general_vibrate: Boolean,
                        var txt2Speech: Boolean,
                        var picPath : String?,
                        @Embedded
                        var calender: Calender?)

@Dao interface UserDBDao {

    @Query("SELECT * from Users")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * from Users where uName =  :name ")
    fun getByUserName(name : String) : User?

    @Query("SELECT wakeUp,wakeUp_pic_path,morning, morning_pic_path, lunchTime, lunchTime_pic_path, afternoon, afternoon_pic_path, evening, evening_pic_path, dinnerTime, dinnerTime_pic_path, bedTime, bedTime_pic_path from Users where uName = :name")
    fun getCalenderByName(name : String) : Calender?

    @Insert(onConflict = REPLACE)
    fun insert(userData: User)

    @Query("DELETE from Users")
    fun deleteAll()

    @Update
    fun updateUser(userData : User)
}

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDataDao(): UserDBDao

    companion object {
        @Volatile
        private var INSTANCE: UserDataBase? = null

        fun getDatabase(context: Context, scope: CoroutineScope?): UserDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase::class.java,
                    "user_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

