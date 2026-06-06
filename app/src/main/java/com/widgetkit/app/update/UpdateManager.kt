package com.widgetkit.app.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

data class UpdateInfo(
    val tagName: String,
    val apkUrl: String
)

class UpdateManager(private val context: Context) {

    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val current = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: return@withContext null
            val url = URL("https://api.github.com/repos/pmprince2025-alt/widget/releases/latest")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("User-Agent", "WidgetKit-App")
            conn.connectTimeout = 10000
            conn.readTimeout = 10000

            val responseCode = conn.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("UpdateManager", "Check for update failed with HTTP code: $responseCode")
                return@withContext null
            }

            val json = conn.inputStream.bufferedReader().use { it.readText() }
            val obj = JSONObject(json)
            val latest = obj.getString("tag_name").removePrefix("v")

            if (compareVersions(latest, current) > 0) {
                val assets = obj.getJSONArray("assets")
                for (i in 0 until assets.length()) {
                    val a = assets.getJSONObject(i)
                    if (a.getString("name").endsWith(".apk")) {
                        return@withContext UpdateInfo(latest, a.getString("browser_download_url"))
                    }
                }
            }
            null
        } catch (e: Exception) {
            Log.e("UpdateManager", "Error checking for update", e)
            null
        }
    }

    fun downloadAndInstall(updateInfo: UpdateInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!context.packageManager.canRequestPackageInstalls()) {
                        Log.w("UpdateManager", "Request install packages permission not granted.")
                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                            data = Uri.parse("package:${context.packageName}")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                        return@launch
                    }
                }

                val file = File(context.cacheDir, "update-${updateInfo.tagName}.apk")
                val url = URL(updateInfo.apkUrl)
                val conn = url.openConnection() as HttpURLConnection
                conn.setRequestProperty("User-Agent", "WidgetKit-App")
                conn.connectTimeout = 15000
                conn.readTimeout = 15000
                conn.instanceFollowRedirects = true

                var responseCode = conn.responseCode
                var connectionToRead = conn

                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                    responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    responseCode == 307 || responseCode == 308) {
                    val newUrl = conn.getHeaderField("Location")
                    Log.d("UpdateManager", "Redirecting download to: $newUrl")
                    val redirectConn = URL(newUrl).openConnection() as HttpURLConnection
                    redirectConn.setRequestProperty("User-Agent", "WidgetKit-App")
                    redirectConn.connectTimeout = 15000
                    redirectConn.readTimeout = 15000
                    responseCode = redirectConn.responseCode
                    connectionToRead = redirectConn
                }

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("UpdateManager", "Download failed with response code: $responseCode")
                    return@launch
                }

                connectionToRead.inputStream.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

                Log.d("UpdateManager", "APK downloaded successfully to ${file.absolutePath}")

                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/vnd.android.package-archive")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("UpdateManager", "Error downloading/installing update", e)
            }
        }
    }

    private fun compareVersions(a: String, b: String): Int {
        val pa = a.split(".").map { it.toIntOrNull() ?: 0 }
        val pb = b.split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(pa.size, pb.size)) {
            val diff = (pa.getOrElse(i) { 0 }) - (pb.getOrElse(i) { 0 })
            if (diff != 0) return diff
        }
        return 0
    }
}
