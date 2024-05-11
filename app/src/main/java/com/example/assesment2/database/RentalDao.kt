package com.example.assesment2.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.assesment2.model.Rental
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Insert
    suspend fun insert(rental: Rental)

    @Update
    suspend fun update(rental: Rental)

    @Query("SELECT * FROM rental ORDER BY jenis_kendaraan ASC")
    fun getRental(): Flow<List<Rental>>

    @Query("SELECT * FROM rental WHERE id = :id")
    suspend fun getRentalById(id: Long): Rental?

    @Query("DELETE FROM rental WHERE id = :id")
    suspend fun deleteById(id: Long)
}