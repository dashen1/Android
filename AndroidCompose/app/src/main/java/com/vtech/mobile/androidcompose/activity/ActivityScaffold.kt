package com.vtech.mobile.androidcompose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtech.mobile.androidcompose.ui.theme.AndroidComposeTheme
import com.vtech.mobile.androidcompose.ui.theme.Purple700
import kotlinx.coroutines.launch

class ActivityScaffold:ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary
                ) {
                    ScaffoldExample()
                }
            }
        }
    }
}

@Composable
fun TopBar(onMenuClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Scaffold Example", color = Color.White)
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.clickable(onClick = onMenuClicked),
                tint = Color.White
            )
        },
        backgroundColor = Purple700,
        elevation = 12.dp
    )
}

@Composable
fun BottomBar(items: List<String>) {
//    BottomAppBar(
//        backgroundColor = Purple700
//    ) {
//        Text(text = "BottomAppBar", color = Color.White)
//    }
    var selectedItem by remember {
        mutableStateOf(0)
    }
    BottomNavigation {
        items.forEachIndexed { index, str ->
            BottomNavigationItem(
                selected = selectedItem==index,
                onClick = { selectedItem=index},
                icon = {},
                alwaysShowLabel = true,
                label = { Text(text = str)}
            )
        }
    }
}

@Composable
fun Content() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(text = "Content", color = Purple700)
    }
}

@Composable
fun Drawer(){
    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()) {
        repeat(5){item ->
            Text(text = "Item $item", modifier = Modifier.padding(8.dp), color = Color.Black)
        }
    }
}


@Composable
fun ScaffoldExample(){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    val items = listOf<String>("主页","列表","设置")

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(onMenuClicked = {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            })
        },
        bottomBar = { BottomBar(items)},
        drawerContent = {
            Drawer()
        },
        content = {
            Content()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    when (scaffoldState.snackbarHostState.showSnackbar(
                        message = "Snack Bar",
                        actionLabel = "Dismiss"
                    )){
                        SnackbarResult.Dismissed ->{

                        }
                        SnackbarResult.ActionPerformed -> {

                        }
                    }
                }
            }) {
                Text(text = "+", color = Color.White, fontSize = 26.sp)
            }
        }
    )
}
