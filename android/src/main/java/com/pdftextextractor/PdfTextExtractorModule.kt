package com.pdftextextractor

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.BufferedReader
import java.io.InputStream

class PdfTextExtractorModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  //Extracting file path and their extension
  @ReactMethod
  fun extractContent(filePath: String, promise: Promise) {
    val uri = Uri.parse(filePath)
    val context = reactApplicationContext
    val fileExtension = filePath.substringAfterLast('.').lowercase()

    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
          val extension = getFileExtension(context, uri)

          when (extension.lowercase()) {
              "txt" -> parseTxt(inputStream, promise)
              "docx" -> parseDocx(inputStream, promise)
              "doc" -> parseDoc(inputStream, promise)
              "pdf" -> parsePdf(inputStream, promise)
              else -> promise.reject("unsupported_format", "Unsupported file format")
          }
        } ?: run {
          promise.reject("file_not_found", "File not found")
        }
    } catch (e: Exception) {
      promise.reject("parse_error", "Failed to parse file", e)
    }
  }

  //Method to extract file extension
  private fun getFileExtension(context: Context, uri: Uri): String {
    val fileName = getFileName(context, uri)
    val lastIndex = fileName.lastIndexOf('.')
    return if (lastIndex == -1) "" else fileName.substring(lastIndex + 1)
  }

  //Method to extract file name
  private fun getFileName(context: Context, uri: Uri): String {
    var fileName = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
      if (it.moveToFirst()) {
        fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
      }
    }
    return fileName
  }

  //Method to parse txt file
  private fun parseTxt(inputStream: InputStream, promise: Promise) {
    try {
      val content = inputStream.bufferedReader().use(BufferedReader::readText)
      promise.resolve(content)
    } catch (e: Exception) {
      promise.reject("parse_error", "Failed to parse TXT file", e)
    }
  }

  //Method to extract content from pdf files
  private fun parsePdf(inputStream: InputStream, promise: Promise) {
    try {
      var extractedText = ""
      val pdfReader: PdfReader = PdfReader(inputStream)
      val n = pdfReader.numberOfPages
      for (i in 0 until n) {
        extractedText =
            """ 
          $extractedText${
                PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
            }       
          """.trimIndent()
      }
      pdfReader.close()
      promise.resolve(extractedText)
    }catch (error: Exception){
      println("Error while parsing pdf files" + error);
      promise.reject("Error while parsing the pdf files");
    }
  }

  //Method to extract text from doc file
  private fun parseDoc(inputStream: InputStream, promise: Promise) {
    //Will implement it later
  }

  //Method to extract text from docx file
  private fun parseDocx(inputStream: InputStream, promise: Promise) {
    //Will implement it later
  }

  companion object {
    const val NAME = "PdfTextExtractor"
  }
}
