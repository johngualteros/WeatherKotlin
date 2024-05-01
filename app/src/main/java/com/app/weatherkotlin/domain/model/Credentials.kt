package com.app.weatherkotlin.domain.model

data class Credentials(
    var email: String = "",
    var pwd: String = "",
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && pwd.isNotEmpty()
    }
}
