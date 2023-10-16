package dev.jombi.ubi

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.jombi.ubi.dto.request.LoginRequest
import dev.jombi.ubi.dto.request.RegisterRequest
import dev.jombi.ubi.dto.response.TokenResponse
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.jwt.TokenFactory
import dev.jombi.ubi.util.response.GuidedResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest @Autowired constructor(
    val mvc: MockMvc,
    val userRepository: UserRepository,
    val tokenFactory: TokenFactory
) {
    val mapper = jacksonObjectMapper()

    @BeforeAll
    fun `initialize test users by register`() {
        mvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(RegisterRequest("010-0000-0000", "test@jombi.dev", "qpwo12!@", "테스트"))
        }
        val users = userRepository.findAll()
        println(users.map { it.name })
        assertEquals(1, users.size)
    }

    @Test
    fun `register test`() {
        mvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(RegisterRequest("010-0000-0001", "test1@jombi.dev", "qpwo12!@", "테스트"))
        }.andExpect { status { isOk() } }

        assertNotNull(userRepository.getUserByPhone("01000000001"))
    }

    @Test
    fun `login user by email`() {
        val res = mvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(LoginRequest("010-0000-0000", "qpwo12!@"))
        }.andExpect { status { isOk() } }
            .andReturn().response.contentAsString
        val mapped = mapper.readValue<GuidedResponse<TokenResponse>>(res)

        mvc.get("/user/profile") {
            with(authentication(tokenFactory.getAuthenticationByToken(mapped.data!!.token)))
        }.andExpect { status { isOk() } }
            .andDo { print() }
    }

    @Test
    fun `login user by phone`() {
        val res = mvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(LoginRequest("test@jombi.dev", "qpwo12!@"))
        }.andExpect { status { isOk() } }
            .andReturn().response.contentAsString
        val mapped = mapper.readValue<GuidedResponse<TokenResponse>>(res)

        mvc.get("/user/profile") {
            with(authentication(tokenFactory.getAuthenticationByToken(mapped.data!!.token)))
        }.andExpect { status { isOk() } }
    }
}