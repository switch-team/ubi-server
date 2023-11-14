package dev.jombi.ubi.file.repository

import dev.jombi.ubi.file.entity.UploadedFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : JpaRepository<UploadedFile, Long>