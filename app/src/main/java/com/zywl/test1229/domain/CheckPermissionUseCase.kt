package com.zywl.test1229.domain

import android.os.Build
import android.os.Environment
import com.zywl.test1229.data.PermissionState
import com.zywl.test1229.di.PermissionStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CheckPermissionUseCase @Inject constructor(
    @PermissionStateFlow
    private val permissionState: MutableStateFlow<PermissionState>
) {
    operator fun invoke(): Boolean {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            TODO()
        }
        permissionState.value = if (granted) PermissionState.Granted else PermissionState.Denied
        return granted
    }
}