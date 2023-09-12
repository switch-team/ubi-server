package dev.jombi.ubi.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.jombi.ubi.util.response.GuidedResponseBuilder
import org.junit.jupiter.api.Test

class GuidedResponseSerializationTest {
    private val mapper = jacksonObjectMapper()
    @Test
    fun `this will be no data field`() {
        val t = GuidedResponseBuilder{message = "Success without data"}.noData()

        val serialized = mapper.readTree(mapper.writeValueAsString(t))
        val rightAnswer = mapper.readTree(mapper.writeValueAsString(mapOf("status" to "OK", "message" to "Success without data")))

        assert(serialized == rightAnswer)
    }

    @Test
    fun `this will be have some data`() {
        val dat = SomeClassForTesting("yes")
        val t = GuidedResponseBuilder { message = "Success with data" }
            .build(dat)

        val serialized = mapper.readTree(mapper.writeValueAsString(t))
        val rightAnswer = mapper.readTree(mapper.writeValueAsString(mapOf("status" to "OK", "message" to "Success with data", "data" to dat)))

        assert(serialized == rightAnswer)
    }
    data class SomeClassForTesting(val string: String)
}