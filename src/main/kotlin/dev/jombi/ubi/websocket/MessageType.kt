package dev.jombi.ubi.websocket

enum class MessageType(val isServerSide: Boolean = false, val isError: Boolean = false) {
    // CLIENT-SIDE Message
    SEND_LOCATION,

    // SERVER-SIDE Message
    LOCATION_INFO(true),
    NEW_POSTS(true),
    ASSEMBLE_REQUEST(true),
}