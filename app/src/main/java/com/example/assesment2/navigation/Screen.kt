package com.example.assesment2.navigation

sealed class Screen(val route: String) {
    object Home: Screen("mainScreen")
    object FormBaru: Screen("detailScreen")
    object FormUbah : Screen("detailScreen/{id}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}