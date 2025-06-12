package com.fetch.fetchit.ui.stickerbook

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fetch.fetchit.utils.Constants

// Bottom sheet composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScanBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
    sheetState: SheetState,
) {
    val options = Constants.storeTypes

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.Companion
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Simulate Receipt Scan",
                style = MaterialTheme.typography.titleMedium,
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = selectedOption ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select store type") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                            )
                        }
                    },
                    modifier = Modifier.Companion
                        .menuAnchor()
                        .fillMaxWidth()
                        .let { mod ->
                            if (showError && selectedOption == null) {
                                mod.border(1.dp, Color.Companion.Red, RoundedCornerShape(4.dp))
                            } else mod
                        },
                    isError = showError && selectedOption == null,
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                                showError = false
                            },
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedOption == null) {
                        showError = true
                    } else {
                        onSubmit(selectedOption!!)
                    }
                },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier.Companion.fillMaxWidth(),
            ) {
                Text("Submit")
            }
        }
    }
}