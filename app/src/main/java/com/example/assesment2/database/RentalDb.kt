package com.example.assesment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assesment2.model.Rental

@Database(entities = [Rental::class], version = 3, exportSchema = false)
abstract class RentalDb : RoomDatabase() {
    abstract val dao: RentalDao

    companion object {
        @Volatile
        private var INSTANCE: RentalDb? = null

        fun getInstance(context: Context): RentalDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RentalDb::class.java,
                    "rental_database"
                )
                    .fallbackToDestructiveMigration() // Hapus dan buat ulang skema jika versi berbeda
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}