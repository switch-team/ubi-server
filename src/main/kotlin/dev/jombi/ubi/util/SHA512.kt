package dev.jombi.ubi.util

import java.security.MessageDigest

object SHA512 {
    fun encrypt(str: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        return md.digest(str.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}