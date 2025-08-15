package com.optivus.bharat_haat.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import com.optivus.bharat_haat.constants.AppConstants
import java.io.*
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

object FileUtils {

    /**
     * File size calculations
     */
    fun getFileSize(file: File): Long {
        return if (file.exists()) file.length() else 0L
    }

    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()

        return DecimalFormat("#,##0.#").format(
            sizeInBytes / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    fun isFileSizeValid(file: File, maxSizeInBytes: Int = AppConstants.MAX_IMAGE_SIZE): Boolean {
        return getFileSize(file) <= maxSizeInBytes
    }

    /**
     * File operations
     */
    fun createFile(directory: File, fileName: String): File {
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return File(directory, fileName)
    }

    fun deleteFile(file: File): Boolean {
        return if (file.exists()) {
            file.delete()
        } else false
    }

    fun copyFile(source: File, destination: File): Boolean {
        return try {
            source.copyTo(destination, overwrite = true)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun moveFile(source: File, destination: File): Boolean {
        return try {
            if (copyFile(source, destination)) {
                deleteFile(source)
            } else false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Directory operations
     */
    fun getAppDirectory(context: Context): File {
        return File(context.filesDir, AppConstants.APP_NAME)
    }

    fun getCacheDirectory(context: Context): File {
        return File(context.cacheDir, AppConstants.CACHE_DIR)
    }

    fun getImagesDirectory(context: Context): File {
        return File(getAppDirectory(context), AppConstants.IMAGES_DIR)
    }

    fun getDocumentsDirectory(context: Context): File {
        return File(getAppDirectory(context), AppConstants.DOCUMENTS_DIR)
    }

    fun getExternalStorageDirectory(): File? {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory()
        } else null
    }

    fun clearDirectory(directory: File): Boolean {
        return try {
            if (directory.exists() && directory.isDirectory) {
                directory.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        clearDirectory(file)
                    }
                    file.delete()
                }
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    fun getDirectorySize(directory: File): Long {
        var size = 0L
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                size += if (file.isDirectory) {
                    getDirectorySize(file)
                } else {
                    file.length()
                }
            }
        }
        return size
    }

    /**
     * File reading and writing
     */
    fun writeStringToFile(file: File, content: String): Boolean {
        return try {
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun readStringFromFile(file: File): String? {
        return try {
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            null
        }
    }

    fun writeBytesToFile(file: File, bytes: ByteArray): Boolean {
        return try {
            file.parentFile?.mkdirs()
            file.writeBytes(bytes)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun readBytesFromFile(file: File): ByteArray? {
        return try {
            if (file.exists()) file.readBytes() else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * URI to File conversion
     */
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "temp_${System.currentTimeMillis()}"
            val tempFile = File(getCacheDirectory(context), fileName)

            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        it.getString(columnIndex)
                    } else null
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * File validation
     */
    fun isValidImageFile(file: File): Boolean {
        val validExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        val extension = file.extension.lowercase()
        return validExtensions.contains(extension) && file.exists()
    }

    fun isValidVideoFile(file: File): Boolean {
        val validExtensions = listOf("mp4", "avi", "mov", "wmv", "flv", "webm", "3gp")
        val extension = file.extension.lowercase()
        return validExtensions.contains(extension) && file.exists()
    }

    fun isValidDocumentFile(file: File): Boolean {
        val validExtensions = listOf("pdf", "doc", "docx", "txt", "rtf")
        val extension = file.extension.lowercase()
        return validExtensions.contains(extension) && file.exists()
    }

    /**
     * Temporary files
     */
    fun createTempFile(context: Context, prefix: String = "temp", suffix: String = ""): File {
        return File.createTempFile(prefix, suffix, getCacheDirectory(context))
    }

    fun cleanupTempFiles(context: Context) {
        val cacheDir = getCacheDirectory(context)
        if (cacheDir.exists()) {
            cacheDir.listFiles()?.forEach { file ->
                if (file.name.startsWith("temp") &&
                    System.currentTimeMillis() - file.lastModified() > 24 * 60 * 60 * 1000) { // 24 hours
                    file.delete()
                }
            }
        }
    }

    /**
     * Backup and restore
     */
    fun createBackup(context: Context, data: String, fileName: String): File? {
        return try {
            val backupDir = File(getAppDirectory(context), "backups")
            val backupFile = createFile(backupDir, fileName)
            if (writeStringToFile(backupFile, data)) backupFile else null
        } catch (e: Exception) {
            null
        }
    }

    fun restoreFromBackup(context: Context, fileName: String): String? {
        return try {
            val backupDir = File(getAppDirectory(context), "backups")
            val backupFile = File(backupDir, fileName)
            readStringFromFile(backupFile)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Cache management
     */
    fun clearCache(context: Context): Boolean {
        val cacheDir = getCacheDirectory(context)
        return clearDirectory(cacheDir)
    }

    fun getCacheSize(context: Context): Long {
        val cacheDir = getCacheDirectory(context)
        return getDirectorySize(cacheDir)
    }

    fun getCacheSizeFormatted(context: Context): String {
        return formatFileSize(getCacheSize(context))
    }

    /**
     * File sharing
     */
    fun shareFile(context: Context, file: File): Uri? {
        return try {
            androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * MIME type detection
     */
    fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            "webp" -> "image/webp"
            "mp4" -> "video/mp4"
            "avi" -> "video/avi"
            "mov" -> "video/quicktime"
            "pdf" -> "application/pdf"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "txt" -> "text/plain"
            "json" -> "application/json"
            "xml" -> "application/xml"
            else -> "application/octet-stream"
        }
    }
}
