package dev.jombi.ubi.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import dev.jombi.ubi.entity.UploadedFile
import dev.jombi.ubi.repository.FileRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileService(@Value("\${cloud.aws.s3.bucket}") private val bucket: String, private val amazonS3Client: AmazonS3Client, private val fileRepository: FileRepository) {
    @Suppress("unused")
    fun upload(file: MultipartFile, directory: String): UploadedFile {
        val meta = ObjectMetadata()
        meta.contentType = file.contentType
        meta.contentLength = file.size

        val extension = file.originalFilename?.let { it.substring(it.lastIndexOf(".") + 1) }
            ?: throw CustomError(ErrorStatus.FILE_EXTENSION_NOT_PROVIDED)

        val key = "$directory/${UUID.randomUUID()}_${System.currentTimeMillis()}.$extension"
        file.inputStream.use {
            amazonS3Client.putObject(PutObjectRequest(bucket, key, it, meta).withCannedAcl(CannedAccessControlList.PublicRead))
        }
        val url = amazonS3Client.getUrl(bucket, key).toString()
        val f = UploadedFile(name = file.originalFilename ?: file.name, url = url)
        return fileRepository.save(f)
    }
}