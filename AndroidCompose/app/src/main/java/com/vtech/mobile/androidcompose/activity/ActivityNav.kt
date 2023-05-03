package com.vtech.mobile.androidcompose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vtech.mobile.androidcompose.ui.theme.AndroidComposeTheme

class ActivityNav: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary
                ) {
                    ScaffoldBottomBarSamples()
                }
            }
        }
    }
}

@Composable
fun ScaffoldBottomBarSamples(){
    var checkIndex by remember {
        mutableStateOf(0)
    }

    val bottomNumber = listOf("首页","清除","电话","邮箱")
    val bottomIcons = listOf(Icons.Default.Home,Icons.Default.Clear,Icons.Default.Call,Icons.Default.Email)

    Divider(thickness = 0.5.dp, color = Color.Red)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "我是标题")},
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack,contentDescription = null)
                },
                actions = {
                    Icon(imageVector = Icons.Default.Add,contentDescription = null)
                    Text(text = "添加",Modifier.padding(10.dp,0.dp,20.dp,0.dp))
                }) },
        bottomBar = {
            BottomNavigation {
                bottomNumber.forEachIndexed { index, contentText ->
                    BottomNavigationItem(
                        selected = checkIndex == index,
                        onClick = { checkIndex = index },
                        label = {
                            Text(text = contentText)
                        },
                        icon = {
                            if (checkIndex == index) {
                                BadgedBox(
                                    badge = {
                                        Badge(content = {
                                            Text(text = "99+")
                                        }, contentColor = Color.White, backgroundColor = Color.Red)
                                    },
                                ) {
                                    Icon(
                                        imageVector = bottomIcons[index],
                                        contentDescription = null
                                    )
                                }
                            }else{
                                Icon(imageVector = bottomIcons[index], contentDescription = null)
                            }
                        },
                    selectedContentColor = Color.Red, unselectedContentColor = Color.White
                        )
                }
            }
        },modifier = Modifier.height(480.dp)){
        Text(text = "我是最重要的")
    }
}