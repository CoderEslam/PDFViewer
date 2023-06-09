package com.doubleclick.pdfviewer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.doubleclick.pdfviewernew.PdfViewerActivity
import com.doubleclick.pdfviewernew.databinding.ActivityPdfViewerBinding

class PDFViewerActivity : AppCompatActivity() {



//    private val requiredPermissionList = arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_EXTERNAL_STORAGE
//    )

    private var download_file_url = "https://github.com/afreakyelf/afreakyelf/raw/main/Log4_Shell_Mid_Term_final.pdf"
    var per = 0f
    private val PERMISSION_CODE = 4040

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer_new)

        findViewById<Button>(R.id.openPdf).setOnClickListener {
            if (checkAndRequestPermission())
                launchPdf()
        }
    }

    private fun launchPdf() {
        startActivity(
            PdfViewerActivity.launchPdfFromUrl(
                context = this, pdfUrl = download_file_url,
                pdfTitle = "Title", directoryName = "dir", enableDownload = true)
        )
    }

    private fun checkAndRequestPermission(): Boolean {
        val permissionsNeeded = ArrayList<String>()

//        for (permission in requiredPermissionList) {
//            if (ContextCompat.checkSelfPermission(this, permission) !=
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                permissionsNeeded.add(permission)
//            }
//        }

//        if (permissionsNeeded.isNotEmpty()) {
//            ActivityCompat.requestPermissions(
//                this,
//                permissionsNeeded.toTypedArray(),
//                PERMISSION_CODE
//            )
//            return false
//        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (readPermission && writePermission)
                    launchPdf()
                else {
                    Toast.makeText(this, " Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}