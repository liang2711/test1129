package com.zywl.test1229.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywl.test1229.bean.Constant
import com.zywl.test1229.bean.StakeInfo
import com.zywl.test1229.data.PermissionState
import com.zywl.test1229.database.DataDao
import com.zywl.test1229.di.PermissionStateFlow
import com.zywl.test1229.ktx.TAG
import com.zywl.test1229.ktx.toast
import com.zywl.test1229.utils.ExcelUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    @PermissionStateFlow
    private val permissionState: MutableStateFlow<PermissionState>,
    private val dao: DataDao
) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()
    val actions = HomeActions(
        onFileSelected = ::onFileSelected,
        onPipeLineChanged = ::onPipeLineChanged,
        onExport = ::onExport,
        onDeleteItem = ::onDeleteItem,
        onEditingIndexChange = ::onEditingIndexChange,
        onDeleteAll = ::onDeleteAll
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(10_000)
                val items = _state.value.items
                if (items.isEmpty()) continue
//                ExcelUtils.produceExcel(context, items, true)
                dao.deleteAll()
                dao.insertAll(items)
                Log.d(TAG, "HomeViewModel: produceExcel")
            }
        }
        permissionState.onEach {
            if (it == PermissionState.Granted) {
//                val file = File(File(Constant.SDCARD, Constant.projectName), "backup/main.xls")
//                onFileSelected(file.path)
                onUpdateItems(dao)
            }
        }.launchIn(viewModelScope)
    }

    private fun onFileSelected(file: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val stakeInfoList = ExcelUtils.parseExcel(file)
            Log.d(TAG, "onFileSelected: $stakeInfoList")
            _state.update { it.copy(items = it.items + stakeInfoList) }
        }
    }
    private fun onUpdateItems(dataDao: DataDao){
        viewModelScope.launch(Dispatchers.IO) {
            val stakeInfoList = dataDao.getAllStakeInfo()
            Log.d(TAG, "onFileSelected: $stakeInfoList")
            _state.update { it.copy(items = stakeInfoList) }
        }
    }

    private fun onPipeLineChanged(index: Int, value: StakeInfo) {
        Log.d(TAG, "onPipeLineChanged: $index, $value")
        _state.update {
            val stakeInfoList = it.items.toMutableList()
            if (index >= 0) {
                stakeInfoList[index] = value
            } else {
                stakeInfoList.add(value)
            }
            it.copy(items = stakeInfoList)
        }
    }

    private fun onExport() {
        val items = _state.value.items
        if (items.isEmpty()) return context.toast("导出失败，没有数据")
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(exporting = true) }
            var state=true
            try {
                ExcelUtils.produceExcel(context, items)
            }catch (e:Exception){
                state=false
            }
            withContext(Dispatchers.Main) {
                if (state)
                    context.toast("导出成功")
                else
                    context.toast("导出失败")
            }
            _state.update { it.copy(exporting = false) }
        }
    }

    private fun onDeleteItem(index: Int) {
        _state.update {
            val stakeInfoList = it.items.toMutableList()
            stakeInfoList.removeAt(index)
            it.copy(items = stakeInfoList)
        }
    }
    private fun onDeleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll()
            val stakeInfoList:MutableList<StakeInfo> = mutableListOf()
            _state.update { it.copy(items = stakeInfoList) }
        }
    }

    private fun onEditingIndexChange(index: Int) {
        _state.update { it.copy(editingIndex = index) }
    }
}

data class HomeViewState(
    val items: List<StakeInfo> = emptyList(),
    val exporting: Boolean = false,
    val editingIndex: Int = -1,
)

data class HomeActions(
    val navigateToInfo: (StakeInfo?) -> Unit = {},
    val onFinishedActivity: () -> Unit = {},
    val onPipeLineChanged: (index: Int, StakeInfo) -> Unit = { _, _ -> },
    val onExport: () -> Unit = {},
    val navigateToFiles: () -> Unit = {},
    val onFileSelected: (String) -> Unit = {},
    val onDeleteItem: (index: Int) -> Unit = {},
    val onEditingIndexChange: (Int) -> Unit = {},
    val onDeleteAll: () -> Unit = {}
)