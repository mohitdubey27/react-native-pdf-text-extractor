
//Created by Mohit Dubey

import Foundation
import UIKit
import MobileCoreServices
import PDFKit

@objc(PdfTextExtractor)
class PdfTextExtractor: NSObject {

  //MARK:- Extrating content of pdf and txt files
  @objc
  func extractContent(_ filePath: String, resolver : @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
    let fileExtension = (filePath as NSString).pathExtension.lowercased()
    
    switch fileExtension {
    case "txt":
      extractTxt(filePath: filePath, resolver: resolver, rejecter: rejecter)
    case "pdf":
      extractPdf(filePath: filePath, resolver: resolver, rejecter: rejecter)
    case "docx":
      extractDocx(filePath: filePath, resolver: resolver, rejecter: rejecter)
    case "doc":
      extractDoc(filePath: filePath, resolver: resolver, rejecter: rejecter)
    default:
      rejecter("unsupported_format", "Unsupported file format", nil)
    }
  }

  //MARK:- Method of extrating contents of txt files
  private func extractTxt(filePath: String, resolver: RCTPromiseResolveBlock, rejecter: RCTPromiseRejectBlock) {
    do {
      let content = try String(contentsOfFile: filePath, encoding: .utf8)
      resolver(content)
    } catch let error {
      rejecter("extract_error", "Failed to extract TXT file", error)
    }
  }

  //MARK:- Method of extrating contents of pdf files
  private func extractPdf(filePath: String, resolver: RCTPromiseResolveBlock, rejecter: RCTPromiseRejectBlock) {
    guard let pdfDocument = PDFDocument(url: URL(fileURLWithPath: filePath)) else {
      rejecter("extract_error", "Failed to open PDF file", nil)
      return
    }
    
    let content = NSMutableString()
    for pageIndex in 0..<pdfDocument.pageCount {
      guard let page = pdfDocument.page(at: pageIndex) else { continue }
      if let pageContent = page.string {
        content.append(pageContent)
      }
    }
    resolver(content as String)
  }

  //MARK:- Method of extrating contents of docx files
  func extractDocx(filePath: String, resolver: RCTPromiseResolveBlock, rejecter: RCTPromiseRejectBlock) {
    // Will implement later
  }
  
  //MARK:- Method of extrating contents of doc files
  private func extractDoc(filePath: String, resolver: RCTPromiseResolveBlock, rejecter:RCTPromiseRejectBlock) {
    // Will implement later
  }
}
