package com.belleMusica.viewmodel

import org.http4k.template.ViewModel

data class LoginViewModel(val email: String, val password: String, val error: String) : ViewModel
