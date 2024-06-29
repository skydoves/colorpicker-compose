/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalComposeUiApi::class)

package com.github.skydoves.colorpickercomposedemo

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import colorpickercomposedemo.app.generated.resources.Res
import colorpickercomposedemo.app.generated.resources.app_name
import colorpickercomposedemo.app.generated.resources.image_24px
import colorpickercomposedemo.app.generated.resources.palette_24px
import com.github.skydoves.colorpickercomposedemo.screens.HsvColorPickerColoredSelectorScreen
import com.github.skydoves.colorpickercomposedemo.screens.ImageColorPickerScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed class Screen(val route: String, val name: String, val drawable: DrawableResource) {
  data object ImageColorPicker : Screen("image_picker", "Image", Res.drawable.image_24px)
  data object HsvPicker : Screen("hsv_picker", "HSV", Res.drawable.palette_24px)
}

val navigationItems = listOf(
  Screen.ImageColorPicker,
  Screen.HsvPicker,
)

@Composable
fun MainScreen() {
  val navController = rememberNavController()
  Scaffold(
    bottomBar = { BottomBar(navController) },
    topBar = { MainToolBar() },
  ) { innerPadding ->
    NavHost(
      modifier = Modifier.padding(innerPadding),
      navController = navController,
      startDestination = Screen.ImageColorPicker.route,
    ) {
      composable(Screen.ImageColorPicker.route) {
        ImageColorPickerScreen()
      }
      composable(Screen.HsvPicker.route) {
        HsvColorPickerColoredSelectorScreen()
      }
    }
  }
}

@Composable
fun MainToolBar() {
  TopAppBar(
    elevation = 6.dp,
    backgroundColor = Color(0xFF6200EE),
    modifier = Modifier.height(58.dp),
  ) {
    Text(
      modifier = Modifier
        .padding(8.dp)
        .align(Alignment.CenterVertically),
      text = stringResource(Res.string.app_name),
      color = Color.White,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
    )
  }
}

@Composable
fun BottomBar(navController: NavController) {
  BottomNavigation {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    navigationItems.forEach { screen ->
      BottomNavigationItem(
        modifier = Modifier.testTag(screen.name),
        icon = { Icon(painterResource(screen.drawable), contentDescription = null) },
        label = { Text(screen.name) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
          navController.navigate(screen.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            navController.graph.findStartDestination().route?.let {
              popUpTo(it) {
                saveState = true
              }
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
          }
        },
      )
    }
  }
}
