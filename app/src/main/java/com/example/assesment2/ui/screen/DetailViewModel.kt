package com.example.assesment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assesment2.database.RentalDao
import com.example.assesment2.model.Rental
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: RentalDao) : ViewModel() {

    fun insertOrUpdateRental(id: Long?, nama: String, jenis_kendaraan: String, gender: String) {
        if (id == null) {
            insert(nama, jenis_kendaraan, gender)
        } else {
            update(id, nama, jenis_kendaraan, gender)
        }
    }

    fun insert(nama: String, jenis_kendaraan: String, gender: String) {
        val rental = Rental(
            nama = nama,
            jenis_kendaraan = jenis_kendaraan,
            gender = gender,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(rental)
        }
    }

    suspend fun getRental(id: Long): Rental? {
        return dao.getRentalById(id)
    }

    fun update(id: Long, nama: String, jenis_kendaraan: String, gender: String) {
        val rental = Rental(
            id = id,
            nama = nama,
            jenis_kendaraan = jenis_kendaraan,
            gender = gender,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(rental)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
