package de.steenbergen.architecture.sample.ui.login.db

import androidx.room.*
import de.steenbergen.architecture.sample.ui.login.domain.Session
import io.reactivex.Observable


@Database(entities = [Session::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM session WHERE session.id IS 1 LIMIT 1")
    fun observe(): Observable<List<Session>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun store(session: Session)

    @Query("DELETE FROM session")
    fun nuke()
}

