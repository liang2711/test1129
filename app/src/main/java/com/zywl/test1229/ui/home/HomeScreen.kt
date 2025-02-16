package com.zywl.test1229.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zywl.test1229.R
import com.zywl.test1229.bean.StakeInfo
import com.zywl.test1229.ui.common.FloatingActionButton
import com.zywl.test1229.ui.common.PreviewSurface
import com.zywl.test1229.ui.common.Previews
import com.zywl.test1229.ui.common.ProgressDialog
import com.zywl.test1229.ui.common.StakeInfoView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeViewState,
    actions: HomeActions
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "桩管理")
                },
                navigationIcon = {
                    IconButton(onClick = actions.onFinishedActivity) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onImport = actions.navigateToFiles,
                onExport = actions.onExport,
                onCreate = {
                    actions.onEditingIndexChange(-1)
                    actions.navigateToInfo(null)
                }
            )
        }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.imePadding()
            ) {
                itemsIndexed(state.items) { index, item ->
                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    actions.onEditingIndexChange(index)
                                    actions.navigateToInfo(item)
                                },
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                ), modifier = Modifier.padding(start = 6.dp)
                            )
                            Text(
                                text = "测绘临时编号:${item.tempNo}",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                ), textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    expanded = !expanded
                                }
                            ) {
                                Icon(
                                    painter = painterResource((if (!expanded) R.mipmap.downdraw else R.mipmap.updraw)),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                        StakeInfoView(
                            isShow = expanded,
                            stakeInfo = item,
                            onPipeLineChanged = {
                                actions.onPipeLineChanged(index, it)
                            },
                            onDeleteItem = {
                                actions.onDeleteItem(index)
                            }
                        )
                        VerticalDivider()
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
            if (state.items.isNotEmpty())
                Button(
                    onClick = {
                        actions.onDeleteAll()
                              },
                    modifier = Modifier
                        .align(Alignment.BottomStart)  // 设置它在Box中的右下角
                        .padding(26.dp)
                ) {
                    Text("清除所有数据")
                }
        }
    }
    ProgressDialog(visible = state.exporting)
}


@Composable
@Previews
fun HomeScreenPreview() {
    val state = HomeViewState(
        items = List(10) { StakeInfo.Empty }
    )
    PreviewSurface {
        HomeScreen(state = state, actions = HomeActions())
    }
}