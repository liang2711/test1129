package com.zywl.test1229.ui.info

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.zywl.test1229.bean.StakeInfo
import kotlinx.serialization.Serializable

fun NavGraphBuilder.info(navController: NavHostController) = composable<StakeInfo> {
    val viewModel: InfoViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val actions = remember(viewModel.actions) {
        viewModel.actions.copy(
            onDone = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    StakeInfoSavedStateKey,
                    state.info
                )
                navController.navigateUp()
            },
            onCancel = navController::navigateUp,
        )
    }
    InfoScreen(state = state, actions = actions)
}

fun NavHostController.navigateToInfo(info: StakeInfo) {
    navigate(info)
}

const val StakeInfoSavedStateKey = "stake_info"

@Serializable
data class InfoRoute(
    val info: StakeInfo
)