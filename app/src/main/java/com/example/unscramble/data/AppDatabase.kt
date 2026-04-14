package com.example.unscramble.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "game_history")
data class GameHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,
    val wordCount: Int,
    val dateTimestamp: Long = System.currentTimeMillis()
)

@Dao
interface GameHistoryDao {
    @Insert
    suspend fun insert(history: GameHistory)

    @Query("SELECT * FROM game_history ORDER BY id ASC")
    fun getAllHistory(): Flow<List<GameHistory>>
}

@Database(entities = [GameHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "game_history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}