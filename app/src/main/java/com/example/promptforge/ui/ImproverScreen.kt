package com.example.promptforge.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.promptforge.viewmodel.ImproverViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImproverScreen(onBack: () -> Unit, vm: ImproverViewModel = viewModel()) {
    val step by vm.step
    val isLoading by vm.isLoading
    val errorMsg by vm.errorMsg
    val clipboard = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Improve Prompt") },
                navigationIcon = {
                    IconButton(onClick = { vm.reset(); onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (errorMsg.isNotBlank()) {
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (step == 0) {
                Text("Enter your rough prompt:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = vm.rawPrompt.value,
                    onValueChange = { vm.rawPrompt.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    placeholder = { Text("e.g. write me an email about meeting") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { vm.improve() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = vm.rawPrompt.value.isNotBlank()
                ) {
                    Text("Improve")
                }
            } else if (step == 1) {
                val qs = vm.questions.value
                var currentQ by remember(step) { mutableStateOf(0) }

                if (qs.isNotEmpty() && currentQ < qs.size) {
                    val q = qs[currentQ]
                    Text(
                        text = "Question ${currentQ + 1} of ${qs.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = q.question, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (j in 0 until q.options.size) {
                            val opt = q.options[j]
                            OutlinedButton(
                                onClick = {
                                    vm.selectAnswer(currentQ, opt)
                                    if (currentQ < qs.size - 1) {
                                        currentQ++
                                    } else {
                                        vm.generate()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(opt)
                            }
                        }
                    }
                }
            } else if (step == 2) {
                Text("Improved prompt:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = vm.improvedPrompt.value,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { clipboard.setText(AnnotatedString(vm.improvedPrompt.value)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Copy")
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                Text("Rate this result:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    for (i in 1..5) {
                        IconButton(onClick = { vm.rating.value = i }) {
                            if (i <= vm.rating.value) {
                                Icon(Icons.Filled.Star, contentDescription = null)
                            } else {
                                Icon(Icons.Outlined.StarOutline, contentDescription = null)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = vm.comment.value,
                    onValueChange = { vm.comment.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Optional comment (e.g. too long, wrong format)") },
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { vm.save(); onBack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { vm.reset() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start over")
                }
            }
        }
    }
}
