package dev.jombi.ubi.websocket.structure

import dev.jombi.ubi.websocket.request.Assemble

data class AssembleStruct(val title: String, val users: List<Assemble> = emptyList())