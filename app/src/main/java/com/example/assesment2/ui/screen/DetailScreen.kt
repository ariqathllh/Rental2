package com.example.assesment2.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.assesment2.R
import com.example.assesment2.database.RentalDb
import com.example.assesment2.model.Rental
import com.example.assesment2.ui.theme.Assesment2Theme
import com.example.assesment2.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = RentalDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var jenis_kendaraan by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }


    val onSaveClicked: () -> Unit = {
        // Atur output sesuai dengan data yang dimasukkan
        output = "Nama: $nama\nJenis Kendaraan: $jenis_kendaraan\nGender: $gender"
    }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val rental: Rental = viewModel.getRental(id) ?: return@LaunchedEffect
        nama = rental.nama
        jenis_kendaraan = rental.jenis_kendaraan
        gender = rental.gender
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_mahasiswa))
                    else
                        Text(text = stringResource(id = R.string.edit_mahasiswa))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (nama == "" || jenis_kendaraan == "" || gender == "") { // Memeriksa apakah nama, nim, dan selectedOption sudah diisi
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(nama, jenis_kendaraan, gender)
                        } else {
                            viewModel.update(id, nama, jenis_kendaraan, gender)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction {
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tampilkan FormRental
            FormRental(
                nama = nama,
                onNameChange = { nama = it },
                jenis_kendaraan = jenis_kendaraan,
                onJenis_kendaraanChange = { jenis_kendaraan = it },
                selectedOption = gender,
                onOptionSelected = { gender = it },
                modifier = Modifier.weight(1f),
                onSaveClicked = onSaveClicked // Teruskan onSaveClicked ke FormRental
            )

            // Tampilkan tombol "Bagikan" di atas keluaran output
            Button(
                onClick = {
                    // Bagikan data
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.bagikan_template,
                            nama, jenis_kendaraan, gender.toString()
                        )
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.bagikan))
            }

            // Tampilkan output di bawah tombol "Bagikan"
            Text(
                text = output,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }

}

@Composable
fun FormRental(
    nama: String, onNameChange: (String) -> Unit,
    jenis_kendaraan: String, onJenis_kendaraanChange: (String) -> Unit,
    selectedOption: String, onOptionSelected: (String) -> Unit,
    modifier: Modifier,
    onSaveClicked: () -> Unit // Fungsi yang akan dipanggil saat tombol "Simpan" diklik
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNameChange(it) },
            label = { Text(text = stringResource(R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = jenis_kendaraan,
            onValueChange = { onJenis_kendaraanChange(it) },
            label = { Text(text = stringResource(R.string.jenis_kendaraan)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        RadioButtonGrup(
            options = listOf("Laki-laki", "Perempuan"),
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
        )

        // Tambahkan tombol "Simpan" di sini
        Button(
            onClick = onSaveClicked,
            modifier = Modifier.align(Alignment.End) // Mengatur tombol ke kanan
        ) {
            Text(text = stringResource(R.string.simpan))
        }
    }
}


@Composable
fun RadioButtonGrup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val outlineColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = outlineColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) }
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assesment2Theme {
        DetailScreen(rememberNavController())
    }
}
