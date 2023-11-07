package dev.jombi.ubi.util

import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import java.util.UUID

fun UUIDSafe(uuid: String) = try {
    UUID.fromString(uuid)
} catch (e: IllegalArgumentException) {
    throw CustomError(ErrorStatus.INVALID_PATH_VARIABLE)
}