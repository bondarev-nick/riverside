package com.example.riverside.ui.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.riverside.R
import com.example.riverside.ui.MainActivity
import kotlinx.coroutines.launch

@SuppressLint("InflateParams")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(onDismiss: () -> Unit) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.wrapContentHeight(),
        onDismissRequest = {
            scope.launch {
                bottomSheetState.hide()
                onDismiss()
            }
        },
        sheetState = bottomSheetState,
        dragHandle = null
    ) {
        AndroidView(factory = { context ->
            val fragmentManager = activityLookup(context)?.supportFragmentManager
            fragmentManager?.findFragmentByTag("details_fragment")?.let { addedFragment ->
                fragmentManager.beginTransaction().apply {
                    remove(addedFragment)
                    commitNowAllowingStateLoss()
                }
            }
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_container, null)
        })
    }
}

private fun activityLookup(context: Context): MainActivity? =
    generateSequence(context) { (it as? ContextWrapper)?.baseContext }
        .filterIsInstance<MainActivity>()
        .firstOrNull()