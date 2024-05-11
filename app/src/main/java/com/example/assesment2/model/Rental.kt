package com.example.assesment2.model

import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rental")
data class Rental(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val jenis_kendaraan: String,
    val gender: String,
    var selectedOption: String = ""
)