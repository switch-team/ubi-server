package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.UserImage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserImageRepository: JpaRepository<UserImage, UUID> {
}