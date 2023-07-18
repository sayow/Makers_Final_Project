package com.belleMusica.viewmodel

import org.http4k.template.ViewModel

data class SignUpViewModel(val email: String, val password: String, val username: String, val error: String) : ViewModel