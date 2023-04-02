package com.doubleclick.pdfviewer

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.doubleclick.pdf_viewer.PDFView
import com.doubleclick.pdf_viewer.listener.OnLoadCompleteListener
import com.doubleclick.pdf_viewer.listener.OnPageChangeListener
import com.doubleclick.pdf_viewer.listener.OnPageErrorListener
import com.doubleclick.pdf_viewer.scroll.DefaultScrollHandle
import com.doubleclick.pdf_viewer.util.FitPolicy
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfDocument.Bookmark

class PDFViewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener,
    OnPageErrorListener {
    var pageNumber = 0
    private val TAG = "PDFViewActivity"
    var pdfFileName: String? = null

    private lateinit var pdfView: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfview)
        pdfView = findViewById(R.id.pdfView)
//        pdfView.fromAsset("sample.pdf")
//            .defaultPage(0)
//            .onPageChange(this)
//            .enableAnnotationRendering(true)
//            .onLoad(this)
//            .scrollHandle(DefaultScrollHandle(this))
//            .spacing(10) // in dp
//            .onPageError(this)
//            .pageFitPolicy(FitPolicy.BOTH)
//            .load();
        displayFromUri(Uri.parse("https://www.africau.edu/images/default/sample.pdf"))
    }

    override fun loadComplete(nbPages: Int) {
        val meta: PdfDocument.Meta = pdfView.getDocumentMeta()
        Log.e(TAG, "title = " + meta.getTitle())
        Log.e(TAG, "author = " + meta.getAuthor())
        Log.e(TAG, "subject = " + meta.getSubject())
        Log.e(TAG, "keywords = " + meta.getKeywords())
        Log.e(TAG, "creator = " + meta.getCreator())
        Log.e(TAG, "producer = " + meta.getProducer())
        Log.e(TAG, "creationDate = " + meta.getCreationDate())
        Log.e(TAG, "modDate = " + meta.getModDate())

        printBookmarksTree(pdfView.getTableOfContents(), "-")
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    override fun onPageError(page: Int, t: Throwable?) {
        Log.e(TAG, "Cannot load page " + page);
    }


    private fun printBookmarksTree(tree: List<Bookmark>, sep: String) {
        for (b in tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.title, b.pageIdx))
            if (b.hasChildren()) {
                printBookmarksTree(b.children, "$sep-")
            }
        }
    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.getScheme().equals("content")) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).toInt()
                    )
                }
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }


    private fun displayFromUri(uri: Uri) {
        pdfFileName = getFileName(uri)
        pdfView.fromUri(uri)
            .defaultPage(pageNumber)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .spacing(10) // in dp
            .onPageError(this)
            .load()

        title = pdfFileName;
    }
}