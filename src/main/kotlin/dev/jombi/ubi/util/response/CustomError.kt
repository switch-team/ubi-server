package dev.jombi.ubi.util.response

class CustomError(val reason: ErrorStatus) : RuntimeException(reason.message)