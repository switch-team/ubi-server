package dev.jombi.ubi.dto

import java.net.URL
import java.util.UUID

data class Profile(val id: UUID, val name: String, val phone: String, val email: String, val friends: Int, val profileImage: URL?)