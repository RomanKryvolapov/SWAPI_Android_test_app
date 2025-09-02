/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import java.io.File

fun Context.shareText(title: String = "text", text: String) {
    if (isMainThread()) {
        shareTextInternal(title, text)
    } else {
        Handler(Looper.getMainLooper()).post {
            shareTextInternal(title, text)
        }
    }
}

private fun Context.shareTextInternal(title: String = "text", text: String) {
    val intent = ShareCompat.IntentBuilder(this)
        .setText(text)
        .setType("text/plain")
        .setChooserTitle(title)
        .createChooserIntent()
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, title))
    }
}


fun Context.sharePdf(file: File) {
    val intent = ShareCompat.IntentBuilder(this)
        .setType("application/pdf")
        .setStream(FileProvider.getUriForFile(this, "${this.packageName}.provider", file))
        .setChooserTitle("Document")
        .createChooserIntent()
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.sendEmailWithAttachment(email: String, subject: String, text: String, file: File?) {
    val builder = ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setChooserTitle("Email")
        .setEmailTo(arrayOf(email))
        .setSubject(subject)
        .setText(text)

    // Attach file if needed
    if (file != null) {
        val fileUri = FileProvider
            .getUriForFile(this, "${this.packageName}.provider", file)

        builder
            .setType(contentResolver.getType(fileUri))
            .setStream(fileUri)
    }

    val intent = builder.intent
        .setAction(Intent.ACTION_SENDTO)
        .setData(Uri.parse("mailto:")) // only email apps should handle this
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}