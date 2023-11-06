package dev.jombi.ubi.websocket

enum class MessageType(val isServerSide: Boolean = false) {
    // CLIENT-SIDE Message
    SEND_LOCATION,

    // SERVER-SIDE Message
    LOCATION_INFO(true),
    NEW_POSTS(true),
    NEW_INVITE_REQUEST(true),
    NEW_JOIN_REQUEST(true),
    NEW_FRIEND_REQUEST(true),
}