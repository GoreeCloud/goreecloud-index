package com.goreecloud.index

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.goreecloud.index.engine.SearchEngine
import com.goreecloud.index.model.SearchAction
import com.goreecloud.index.provider.AndroidAppSearchProvider
import com.goreecloud.index.ui.GoreeCloudIndexApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val engine = remember {
                SearchEngine(
                    providers = listOf(AndroidAppSearchProvider(packageManager)),
                )
            }
            GoreeCloudIndexApp(
                engine = engine,
                onAction = ::performAction,
            )
        }
    }

    private fun performAction(action: SearchAction) {
        when (action) {
            is SearchAction.LaunchApplication -> launchApplication(action.packageName)
        }
    }

    private fun launchApplication(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent == null) {
            showLaunchFailure()
            return
        }

        try {
            startActivity(launchIntent)
        } catch (_: ActivityNotFoundException) {
            showLaunchFailure()
        } catch (_: SecurityException) {
            showLaunchFailure()
        }
    }

    private fun showLaunchFailure() {
        Toast.makeText(this, "Unable to open this application.", Toast.LENGTH_SHORT).show()
    }
}
