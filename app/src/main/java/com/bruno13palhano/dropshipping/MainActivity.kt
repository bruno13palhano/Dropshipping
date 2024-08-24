package com.bruno13palhano.dropshipping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bruno13palhano.dropshipping.ui.menu.BottomMenu
import com.bruno13palhano.dropshipping.ui.navigation.MainNavGraph
import com.bruno13palhano.dropshipping.ui.theme.DropshippingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DropshippingTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomMenu(
                                navController = navController
                            )
                        }
                    ) {
                        MainNavGraph(
                            modifier = Modifier.padding(it),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}