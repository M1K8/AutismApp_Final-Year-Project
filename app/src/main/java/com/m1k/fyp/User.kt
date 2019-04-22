package com.m1k.fyp

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.content.Context


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

@Entity
class Week() {
    var monday: String = ""
    var monday_pic_path: String? = null

    var tuesday: String = ""
    var tuesday_pic_path: String? = null

    var wednesday: String = ""
    var wednesday_pic_path: String? = null

    var thursday: String = ""
    var thursday_pic_path: String? = null

    var friday: String = ""
    var friday_pic_path: String? = null

    var saturday: String = ""
    var saturday_pic_path: String? = null

    var sunday: String = ""
    var sunday_pic_path: String? = null

    internal constructor(w: String, m: String, l: String, a: String, e: String, d: String, b: String) : this() {
        monday = w
        monday_pic_path = null

        tuesday = m
        tuesday_pic_path = null

        wednesday = l
        wednesday_pic_path = null

        thursday = a
        thursday_pic_path = null

        friday= e
        friday_pic_path = null

        saturday = d
        saturday_pic_path = null

        sunday = b
        sunday_pic_path = null
    }
}

@Entity
class NamePicPair {
    var uName: String = ""
    var picPath: String? = null
}

@Entity(tableName = "Users")
data class User(@PrimaryKey(autoGenerate = false) @ColumnInfo(name="uName")var uName: String,
                @ColumnInfo(name="draw_vibrate")var draw_vibrate: Boolean,
                @ColumnInfo(name="general_vibrate")var general_vibrate: Boolean,
                @ColumnInfo(name="txt2Speech")var txt2Speech: Boolean,
                @ColumnInfo(name="calWeekly")var calWeekly: Boolean,
                @ColumnInfo(name="picPath")var picPath : String?,
                @Embedded
                        var calender: Calender?,
                @Embedded
                        var week: Week?)

@Dao interface UserDBDao {

    @Query("SELECT * from Users")
    fun getAll(): List<User>

    @Query("SELECT uName, picPath from Users")
    fun getAllNames(): List<NamePicPair>?

    @Query("SELECT count(*) from Users")
    fun getCount(): Int

    @Query("SELECT * from Users where uName =  :name ")
    fun getByUserName(name : String) : User?

    @Query("SELECT wakeUp,wakeUp_pic_path,morning, morning_pic_path, lunchTime, lunchTime_pic_path, afternoon, afternoon_pic_path, evening, evening_pic_path, dinnerTime, dinnerTime_pic_path, bedTime, bedTime_pic_path from Users where uName = :name")
    fun getCalenderByName(name : String) : Calender?

    @Query("SELECT monday,monday_pic_path,tuesday, tuesday_pic_path, wednesday, wednesday_pic_path, thursday, thursday_pic_path, friday, friday_pic_path, saturday, saturday_pic_path, sunday, sunday_pic_path from Users where uName = :name")
    fun getWeekByName(name : String) : Week?

    @Query("SELECT draw_vibrate,general_vibrate, txt2Speech, calWeekly from Users where uName = :name")
    fun getSettingsByName(name : String) : Settings?

    @Insert(onConflict = IGNORE)
    fun insert(userData: User) : Long

    @Query("DELETE from Users")
    fun deleteAll()

    @Query("UPDATE Users SET draw_vibrate = :d, general_vibrate = :g, txt2Speech = :t, calWeekly = :c, picPath = :p where uName = :name")
    fun updateSettingsByUser(name : String, d : Boolean, g : Boolean, t : Boolean, c : Boolean, p : Boolean)

    @Query("UPDATE Users SET draw_vibrate = :d where uName = :name")
    fun updateDrawVibByUser(name : String, d : Boolean)

    @Query("UPDATE Users SET general_vibrate = :d where uName = :name")
    fun updateGenVibByUser(name : String, d : Boolean)

    @Query("UPDATE Users SET txt2Speech = :d where uName = :name")
    fun updateT2sByUser(name : String, d : Boolean)

    @Query("UPDATE Users SET calWeekly = :d where uName = :name")
    fun updateCalWeeklyByUser(name : String, d : Boolean)

    @Query("UPDATE Users SET picPath = :d where uName = :name")
    fun updatePicByUser(name : String, d : Boolean)

    @Query("UPDATE Users SET monday = :m ,monday_pic_path = :mp ,tuesday = :t , tuesday_pic_path = :tp, wednesday = :w, wednesday_pic_path = :wp, thursday = :th, thursday_pic_path = :thp, friday = :f, friday_pic_path = :fp, saturday = :sa, saturday_pic_path = :sap, sunday = :su, sunday_pic_path = :sup where uName = :name")
    fun updateWeekByUser(
        m: String,
        mp: String?,
        t: String,
        tp: String?,
        w: String,
        wp: String?,
        th: String,
        thp: String?,
        f: String,
        fp: String?,
        sa: String,
        sap: String?,
        su: String,
        sup: String?,
        name: String
    )

    @Query("UPDATE Users SET wakeUp = :w,wakeUp_pic_path= :wp,morning= :m, morning_pic_path= :mp, lunchTime= :l, lunchTime_pic_path= :lp, afternoon= :a, afternoon_pic_path= :ap, evening= :e, evening_pic_path= :ep, dinnerTime= :d, dinnerTime_pic_path= :dp, bedTime= :b, bedTime_pic_path= :bp where uName = :name")
    fun updateCalenderByUser(
        w: String,
        wp: String?,
        m: String,
        mp: String?,
        l: String,
        lp: String?,
        a: String,
        ap: String?,
        e: String,
        ep: String?,
        d: String,
        dp: String?,
        b: String,
        bp: String?,
        name: String
    )


    @Update
    fun updateUser(userData : User)
}

@Database(entities = [User::class], version = 4, exportSchema = false)
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
                    "user_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}

