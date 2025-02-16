package com.zywl.test1229.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zywl.test1229.bean.Constant
import com.zywl.test1229.ui.common.EditableExposedDropdownMenuSample
import com.zywl.test1229.ui.common.EditableTextField
import com.zywl.test1229.ui.common.EditableTextFieldInt
import com.zywl.test1229.ui.common.EditableTime
import com.zywl.test1229.ui.common.ExposedDropdownMenuSample
import com.zywl.test1229.ui.common.PreviewSurface
import com.zywl.test1229.ui.common.Previews


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    state: InfoViewState,
    actions: InfoActions
) {
    val scrollState = rememberScrollState()
    val data = state.info
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .statusBarsPadding()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .verticalScroll(scrollState)
    ) {
        EditableTextField(
            "桩号",
            data.tempNo,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(tempNo = it))
            }
        )
        EditableTextField(
            "管线",
            data.pipeLine,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(pipeLine = it))
            }
        )
        EditableExposedDropdownMenuSample(
            Constant.stakeTypeArray,
            "桩类型",
            data.stakeType,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(stakeType = it))
            }
        )
        EditableExposedDropdownMenuSample(
            Constant.depthArray,
            "管中埋深(米)",
            data.pipeDepth,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(pipeDepth = it))
            }
        )
        EditableTextField(
            "里程",
            data.mileage,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(mileage = it))
            }
        )
        EditableExposedDropdownMenuSample(
            Constant.BuriedTechnologyArray,
            "埋设工艺",
            data.buryTech,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(buryTech = it))
            }
        )
        ExposedDropdownMenuSample(
            "是否出入土点",
            data.isInPoint,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(isInPoint = it))
            }
        )
        EditableExposedDropdownMenuSample(
            Constant.terrainArray,
            "地形",
            data.terrain,
            modifier = Modifier.fillMaxWidth(),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(terrain = it))
            }
        )
        Row {
            EditableTextFieldInt(
                label = "经度",
                content = (data.latitude).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), onPipeLineChanged = {
                    actions.onInfoChange(data.copy(latitude = it))
                }
            )
            EditableTextFieldInt(
                label = "维度",
                content = (data.longitude).toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), onPipeLineChanged = {
                    actions.onInfoChange(data.copy(longitude = it))
                }
            )
        }
        EditableTime(
            label = "采集时间",
            date = data.collectDate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, top = 18.dp, bottom = 12.dp),
            onPipeLineChanged = {
                actions.onInfoChange(data.copy(collectDate = it))
            }
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier,
                onClick = actions.onDone
            ) {
                Text(text = "确定")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                modifier = Modifier,
                onClick = actions.onCancel
            ) {
                Text(text = "取消")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
@Previews
fun InfoScreenPreview() {
    PreviewSurface {
        InfoScreen(state = InfoViewState(), actions = InfoActions())
    }
}