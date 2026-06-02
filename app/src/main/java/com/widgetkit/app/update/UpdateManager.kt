package com.widgetkit.app.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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
            val json = URL("https://api.github.com/repos/pmprince2025-alt/widget/releases/latest").readText()
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
        } catch (_: Exception) {
            null
        }
    }

    fun downloadAndInstall(updateInfo: UpdateInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(context.cacheDir, "update-${updateInfo.tagName}.apk")
                val conn = URL(updateInfo.apkUrl).openConnection()
                conn.getInputStream().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/vnd.android.package-archive")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(intent)
            } catch (_: Exception) { }
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
