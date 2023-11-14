package dev.jombi.ubi.error

import dev.jombi.ubi.error.status.ErrorStatus

class CustomError(val reason: ErrorStatus) : RuntimeException(reason.message)