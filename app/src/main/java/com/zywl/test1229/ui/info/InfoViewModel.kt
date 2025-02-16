package com.zywl.test1229.ui.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.zywl.test1229.bean.StakeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val info = savedStateHandle.toRoute<StakeInfo>()
    private val _state = MutableStateFlow(InfoViewState(info = info))
    val state = _state.asStateFlow()
    val actions = InfoActions(
        onInfoChange = ::onInfoChange
    )

    private fun onInfoChange(value: StakeInfo) {
        _state.update { it.copy(info = value) }
    }
}

data class InfoViewState(
    val info: StakeInfo = StakeInfo.Empty
)

data class InfoActions(
    val onInfoChange: (StakeInfo) -> Unit = {},
    val onDone: () -> Unit = {},
    val onCancel: () -> Unit = {},
)