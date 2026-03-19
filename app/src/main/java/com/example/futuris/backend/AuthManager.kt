package com.example.futuris.backend

class AuthManager {

    fun login(username: String, password: String): Boolean {
        // TEMPORARY logic (we will improve later)

        if (username == "admin" && password == "1234") {
            return true
        }

        return false
    }
}