package com.cs346.spotseeker

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.cs346.spotseeker.data.ServiceLocator
import com.cs346.spotseeker.domain.VoteModel
import com.cs346.spotseeker.presentation.components.BottomBarItem
import com.cs346.spotseeker.presentation.components.BottomNavigationBar
import com.cs346.spotseeker.presentation.screens.ExploreScreen
import com.cs346.spotseeker.presentation.screens.HomeScreen
import com.cs346.spotseeker.presentation.screens.VoteScreen
import com.cs346.spotseeker.presentation.theme.AppTheme
import com.cs346.spotseeker.presentation.viewModels.AppViewModel
import com.cs346.spotseeker.presentation.viewModels.VoteViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: AppViewModel) {
    ServiceLocator.repository = viewModel.photoRepository
    val voteModel = VoteModel(viewModel.photoRepository)
    ServiceLocator.voteViewModel = VoteViewModel(voteModel)
    // Screens
    val homeScreen = HomeScreen()
    // VoteScreen is now a no-arg Screen
    val voteScreen = VoteScreen()
    val exploreScreen = ExploreScreen()

    AppTheme {
        Navigator(homeScreen) { navigator ->
            val currentScreen = navigator.lastItem

            // Simple placeholder icon – you can replace with real icons later
            val emptyImageVector = ImageVector.Builder(
                name = "Empty",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).build()

            val bottomBarItems = listOf(
                BottomBarItem("Home Screen", emptyImageVector, homeScreen),
                BottomBarItem("Vote Screen", emptyImageVector, voteScreen),
                BottomBarItem("Explore Screen", emptyImageVector, exploreScreen),
            )

            val showBottomBar = bottomBarItems.any { it.screen == currentScreen }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomNavigationBar(
                            bottomBarItems = bottomBarItems,
                            selectedIndex = viewModel.selectedScreenIndex,
                            onTabSelected = { index ->
                                viewModel.selectScreen(index)
                                navigator.replace(bottomBarItems[index].screen)
                            }
                        )
                    }
                },
            ) { _ ->
                currentScreen.Content()
            }
        }
    }
}
