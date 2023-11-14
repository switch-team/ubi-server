package dev.jombi.ubi.user.dto.response

import java.net.URL
import java.util.*

data class Profile(val id: UUID, val name: String, val phone: String, val email: String, val friends: Int, val profileImage: URL?)