package com.doubleclick.pdfviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doubleclick.pdfview.PDFView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<PDFView>(R.id.activity_main_pdf_view)
            .fromAsset("paper.pdf").show()

    }

}