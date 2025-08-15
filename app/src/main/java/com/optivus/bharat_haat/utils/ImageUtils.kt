package com.optivus.bharat_haat.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import com.optivus.bharat_haat.constants.AppConstants
import java.io.*
import kotlin.math.max
import kotlin.math.min

object ImageUtils {

    /**
     * Image compression
     */
    fun compressImage(
        context: Context,
        uri: Uri,
        maxWidth: Int = AppConstants.MAX_IMAGE_DIMENSION,
        maxHeight: Int = AppConstants.MAX_IMAGE_DIMENSION,
        quality: Int = AppConstants.COMPRESSED_IMAGE_QUALITY
    ): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val compressedBitmap = resizeBitmap(bitmap, maxWidth, maxHeight)
            val compressedFile = FileUtils.createTempFile(context, "compressed_", ".jpg")

            saveBitmapToFile(compressedBitmap, compressedFile, quality)
            compressedFile
        } catch (e: Exception) {
            null
        }
    }

    fun compressImage(
        file: File,
        maxWidth: Int = AppConstants.MAX_IMAGE_DIMENSION,
        maxHeight: Int = AppConstants.MAX_IMAGE_DIMENSION,
        quality: Int = AppConstants.COMPRESSED_IMAGE_QUALITY
    ): File? {
        return try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val compressedBitmap = resizeBitmap(bitmap, maxWidth, maxHeight)

            saveBitmapToFile(compressedBitmap, file, quality)
            file
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Image resizing
     */
    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        val scale = min(
            maxWidth.toFloat() / originalWidth,
            maxHeight.toFloat() / originalHeight
        )

        if (scale >= 1f) return bitmap

        val newWidth = (originalWidth * scale).toInt()
        val newHeight = (originalHeight * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun cropBitmapToSquare(bitmap: Bitmap): Bitmap {
        val size = min(bitmap.width, bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2

        return Bitmap.createBitmap(bitmap, x, y, size, size)
    }

    fun cropBitmap(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(bitmap, x, y, width, height)
    }

    /**
     * Image rotation
     */
    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun getImageRotation(file: File): Float {
        return try {
            val exif = ExifInterface(file.absolutePath)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } catch (e: Exception) {
            0f
        }
    }

    fun correctImageOrientation(file: File): Bitmap? {
        return try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val rotation = getImageRotation(file)
            if (rotation != 0f) {
                rotateBitmap(bitmap, rotation)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Image saving
     */
    fun saveBitmapToFile(
        bitmap: Bitmap,
        file: File,
        quality: Int = AppConstants.COMPRESSED_IMAGE_QUALITY,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Boolean {
        return try {
            file.parentFile?.mkdirs()
            val outputStream = FileOutputStream(file)
            bitmap.compress(format, quality, outputStream)
            outputStream.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun saveBitmapToPNG(bitmap: Bitmap, file: File): Boolean {
        return saveBitmapToFile(bitmap, file, 100, Bitmap.CompressFormat.PNG)
    }

    /**
     * Image effects
     */
    fun createCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = min(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true

        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)

        canvas.drawOval(rectF, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    fun createRoundedBitmap(bitmap: Bitmap, cornerRadius: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true

        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    fun applyGrayscaleFilter(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    fun adjustBrightness(bitmap: Bitmap, brightness: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.set(floatArrayOf(
            1f, 0f, 0f, 0f, brightness,
            0f, 1f, 0f, 0f, brightness,
            0f, 0f, 1f, 0f, brightness,
            0f, 0f, 0f, 1f, 0f
        ))
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    /**
     * Image validation
     */
    fun isValidImageFile(context: Context, uri: Uri): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            options.outWidth > 0 && options.outHeight > 0
        } catch (e: Exception) {
            false
        }
    }

    fun getImageDimensions(file: File): Pair<Int, Int>? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)

            if (options.outWidth > 0 && options.outHeight > 0) {
                Pair(options.outWidth, options.outHeight)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun getImageDimensions(context: Context, uri: Uri): Pair<Int, Int>? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            if (options.outWidth > 0 && options.outHeight > 0) {
                Pair(options.outWidth, options.outHeight)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Watermark
     */
    fun addTextWatermark(
        bitmap: Bitmap,
        watermarkText: String,
        textSize: Float = 24f,
        alpha: Int = 128
    ): Bitmap {
        val output = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.alpha = alpha
        paint.isAntiAlias = true
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK)

        val x = bitmap.width - paint.measureText(watermarkText) - 20f
        val y = bitmap.height - 20f

        canvas.drawText(watermarkText, x, y, paint)
        return output
    }

    fun addImageWatermark(bitmap: Bitmap, watermark: Bitmap, alpha: Int = 128): Bitmap {
        val output = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.alpha = alpha

        val x = bitmap.width - watermark.width - 20f
        val y = bitmap.height - watermark.height - 20f

        canvas.drawBitmap(watermark, x, y, paint)
        return output
    }

    /**
     * Image thumbnails
     */
    fun createThumbnail(bitmap: Bitmap, size: Int): Bitmap {
        return resizeBitmap(bitmap, size, size)
    }

    fun createThumbnailFromFile(file: File, size: Int): Bitmap? {
        return try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            createThumbnail(bitmap, size)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Image quality assessment
     */
    fun calculateImageQuality(bitmap: Bitmap): ImageQuality {
        val pixels = bitmap.width * bitmap.height

        return when {
            pixels >= 8_000_000 -> ImageQuality.VERY_HIGH // 8MP+
            pixels >= 5_000_000 -> ImageQuality.HIGH // 5MP+
            pixels >= 2_000_000 -> ImageQuality.MEDIUM // 2MP+
            pixels >= 1_000_000 -> ImageQuality.LOW // 1MP+
            else -> ImageQuality.VERY_LOW
        }
    }

    fun getOptimalCompressionQuality(bitmap: Bitmap): Int {
        return when (calculateImageQuality(bitmap)) {
            ImageQuality.VERY_HIGH -> 70
            ImageQuality.HIGH -> 75
            ImageQuality.MEDIUM -> 80
            ImageQuality.LOW -> 85
            ImageQuality.VERY_LOW -> 90
        }
    }

    /**
     * Color extraction
     */
    fun getDominantColor(bitmap: Bitmap): Int {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = scaledBitmap.getPixel(0, 0)
        scaledBitmap.recycle()
        return color
    }

    fun getAverageColor(bitmap: Bitmap): Int {
        var redSum = 0L
        var greenSum = 0L
        var blueSum = 0L
        var pixelCount = 0

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true)

        for (x in 0 until scaledBitmap.width) {
            for (y in 0 until scaledBitmap.height) {
                val pixel = scaledBitmap.getPixel(x, y)
                redSum += Color.red(pixel)
                greenSum += Color.green(pixel)
                blueSum += Color.blue(pixel)
                pixelCount++
            }
        }

        scaledBitmap.recycle()

        return Color.rgb(
            (redSum / pixelCount).toInt(),
            (greenSum / pixelCount).toInt(),
            (blueSum / pixelCount).toInt()
        )
    }
}

enum class ImageQuality {
    VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH
}
