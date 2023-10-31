package dev.jombi.ubi.dto

import java.net.URL

data class Profile(val name: String, val phone: String, val email: String, val friends: Int, val profileImage: URL?)