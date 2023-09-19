package dev.jombi.ubi.util.response

class CustomError(val reason: ErrorDetail) : RuntimeException(reason.message)