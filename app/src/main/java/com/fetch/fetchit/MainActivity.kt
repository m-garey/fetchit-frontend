package com.fetch.fetchit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fetch.fetchit.ui.stickerbook.StickerbookRoute
import com.fetch.fetchit.ui.stickerbook.StickerbookScreen
import com.fetch.fetchit.ui.theme.FetchitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchitTheme {
                StickerbookRoute()
            }
        }
    }
}