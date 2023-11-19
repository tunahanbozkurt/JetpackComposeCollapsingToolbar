package com.example.jetpackcomposecollapsingtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposecollapsingtoolbar.ui.theme.JetpackComposeCollapsingToolbarTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetpackComposeCollapsingToolbarTheme {
                val toolbarHeight = 48.dp
                val toolbarHeightPx =
                    with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
                var toolbarOffsetHeightPx by remember { mutableStateOf(-toolbarHeightPx) }
                val lazyListState = rememberLazyListState()
                val isNestedConnectionEnable by remember {
                    derivedStateOf {
                        lazyListState.firstVisibleItemIndex in 0..2
                    }
                }
                val nestedScrollConnection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            val delta = available.y
                            val newOffset = toolbarOffsetHeightPx - delta
                            if (isNestedConnectionEnable) {
                                toolbarOffsetHeightPx = newOffset.coerceIn(-toolbarHeightPx, 0f)
                            }
                            return Offset.Zero
                        }
                    }
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .nestedScroll(nestedScrollConnection)
                ) {
                    LazyColumn(state = lazyListState) {
                        items(100) { index ->
                            Text(
                                "I'm item $index", modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                    TopAppBar(
                        modifier = Modifier
                            .height(toolbarHeight)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = toolbarOffsetHeightPx.roundToInt()
                                )
                            },
                        title = { Text("toolbar offset is ${toolbarOffsetHeightPx}") }
                    )
                }
            }
        }

    }
}

