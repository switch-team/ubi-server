package dev.jombi.ubi.repository

import dev.jombi.ubi.entity.UploadedFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : JpaRepository<UploadedFile, Long>