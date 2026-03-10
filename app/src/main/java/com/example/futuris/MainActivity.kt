package com.example.futuris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.futuris.navigation.FuturisApp
import com.example.futuris.ui.theme.FuturisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuturisTheme {
                FuturisApp()
            }
        }
    }
}