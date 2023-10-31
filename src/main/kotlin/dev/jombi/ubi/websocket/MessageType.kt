package dev.jombi.ubi.websocket

enum class MessageType(val isServerSide: Boolean = false, val isError: Boolean = false) {
    // CLIENT-SIDE Message
    REMOVE_ASSEMBLE,
    HOST_ASSEMBLE,
    INVITE_ASSEMBLE,
    LIST_ASSEMBLE,

    // SERVER-SIDE Message
    SUCCESS(true),
    USER_ALREADY_JOINED(true, true),
    NO_ASSEMBLE_ROOM(true, true),
    ASSEMBLE_ALREADY_EXISTS(true, true),
}