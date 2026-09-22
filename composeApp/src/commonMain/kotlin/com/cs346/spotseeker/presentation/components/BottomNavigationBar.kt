package com.cs346.spotseeker.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen

data class BottomBarItem(val label: String, val icon: ImageVector, val screen: Screen)

@Composable
fun BottomNavigationBar(
    bottomBarItems: List<BottomBarItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        bottomBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) }
            )
        }
    }
}