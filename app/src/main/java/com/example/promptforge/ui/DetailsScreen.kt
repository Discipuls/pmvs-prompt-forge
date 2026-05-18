package com.example.promptforge.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.promptforge.viewmodel.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(id: Int, onBack: () -> Unit, vm: DetailsViewModel = viewModel()) {
    LaunchedEffect(id) {
        vm.load(id)
    }

    val p = vm.prompt.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prompt Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (p == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val fmt = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.US)

                Text("Saved: ${fmt.format(Date(p.timestamp))}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Original:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(text = p.raw_prompt, modifier = Modifier.padding(12.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Improved:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(text = p.improved_prompt, modifier = Modifier.padding(12.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (p.rating > 0) {
                    Text("Rating: ${p.rating}/5", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
