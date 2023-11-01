package dev.jombi.ubi.websocket

enum class MessageType(val isServerSide: Boolean = false, val isError: Boolean = false) {
    // CLIENT-SIDE Message
    HOST_ASSEMBLE,
    INVITE_ASSEMBLE,
    LIST_ASSEMBLE,
    JOIN_ASSEMBLE,
    CHECK_ASSEMBLE,

    // SERVER-SIDE Message
    SUCCESS(true),
    USER_ALREADY_JOINED(true, true),
    NO_ASSEMBLE_ROOM(true, true),
    NOT_ASSEMBLE_PERSON(true, true),
    ALREADY_ANSWERED(true, true),

    INVALID_VALUE(true, true)
}