/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import java.io.File

@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

/**
 * The backward compatible version of [PackageManager.queryIntentActivities] method.
 */
fun PackageManager.queryIntentActivitiesCompat(intent: Intent): List<ResolveInfo> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        queryIntentActivities(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        queryIntentActivities(intent, 0)
    }
}

/**
 * Starts the activity from [intent] if it is possible,
 * otherwise [failCallback] is triggered.
 */
fun Context.safeStartActivity(intent: Intent, failCallback: (() -> Unit)? = null) {
    if (packageManager.queryIntentActivitiesCompat(intent).isNotEmpty()) {
        ContextCompat.startActivity(this, intent, null)
    } else {
        failCallback?.invoke()
    }
}

fun Context.callPhoneNumber(phoneNumber: String) {
    val phoneNumberUri = "tel:$phoneNumber"
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumberUri))
    safeStartActivity(intent)
}

fun Context.openUrlInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    safeStartActivity(intent)
}

fun Context.openSettings() {
    val intent = Intent(Settings.ACTION_SETTINGS)
    safeStartActivity(intent)
}

fun Context.openApplicationSettings(fragment: Fragment) {
    val packageName = this.packageName
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, fragment.tag)
    }
    val list = packageManager.queryIntentActivitiesCompat(intent)
    if (list.isNotEmpty()) {
        fragment.startActivity(intent)
    }
}

fun Context.openLocationSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    safeStartActivity(intent)
}

fun Context.openFile(file: File) {
    val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
    val mime = contentResolver.getType(uri)

    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.data = uri
    intent.setDataAndType(uri, mime)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    safeStartActivity(intent)
}

fun colorStateListOf(vararg statesWithColorRes: Pair<IntArray, Int>): ColorStateList {
    val (states, colors) = statesWithColorRes.unzip()
    return ColorStateList(states.toTypedArray(), colors.toIntArray())
}

fun Context.colorStateListOf(@ColorRes colorRes: Int): ColorStateList {
    return ColorStateList.valueOf(color(colorRes))
}

fun Context.openGoogleMapNavigation(latitude: Double, longitude: Double) {
    Intent(Intent.ACTION_VIEW, Uri.parse("$latitude,$longitude")).apply {
        setPackage("com.google.android.apps.maps")
        resolveActivity(packageManager)?.let {
            startActivity(this)
        }
    }
}

fun Context.copyToClipboard(label: String = "text", text: String) {
    if (isMainThread()) {
        copyToClipboardInternal(label, text)
    } else {
        Handler(Looper.getMainLooper()).post {
            copyToClipboardInternal(label, text)
        }
    }
}

private fun Context.copyToClipboardInternal(label: String = "text", text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

fun Context.getClipboardText(): String? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clipData = clipboard?.primaryClip
    return if (clipData != null && clipData.itemCount > 0) {
        clipData.getItemAt(0).coerceToText(this).toString()
    } else {
        null
    }
}

fun Context.getAppDeviceOsFullNameForAgent(): String {
    val packageInfo = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0)
    } else {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    }
    val appVersion = packageInfo.versionName
    val androidNameVersion = getAndroidVersion(Build.VERSION.SDK_INT)
    return "name/$appVersion ($androidNameVersion)"
}


fun getAndroidVersion(sdk: Int): String {
    return when (sdk) {
        21 -> "Android/5.0"
        22 -> "Android/5.1"
        23 -> "Android/6.0"
        24 -> "Android/7.0"
        25 -> "Android/7.1.1"
        26 -> "Android/8.0"
        27 -> "Android/8.1"
        28 -> "Android/9.0"
        29 -> "Android/10.0"
        30 -> "Android/11.0"
        31 -> "Android/12.0"
        32 -> "Android/12.1"
        33 -> "Android/13.0"
        else -> "Android/Unsupported"
    }
}

fun Activity.reviewApp() {
    val manager = ReviewManagerFactory.create(this)
    val request: Task<ReviewInfo> = manager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            manager.launchReviewFlow(this, reviewInfo)
        }
    }
}

fun Context.reviewAppOnPlayMarket() {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=com.romankryvolapov.swapi")
            ).apply {
                setPackage("com.android.vending")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.romankryvolapov.swapi")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}

