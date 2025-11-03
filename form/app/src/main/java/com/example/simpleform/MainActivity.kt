package com.example.simpleform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.Locale
import android.widget.CalendarView
import androidx.compose.ui.viewinterop.AndroidView
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleFormTheme {
                RegistrationForm()
            }
        }
    }
}

@Composable
fun SimpleFormTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2196F3),
            onPrimary = Color.White,
            surface = Color(0xFFF5F5F5),
            onSurface = Color.Black
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<Int?>(null) }
    var birthday by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }

    var showCalendar by remember { mutableStateOf(false) }

    // Validation states
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }
    var birthdayError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var termsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "LifeCycle",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        // First Name & Last Name Table-like layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .background(Color.White)
        ) {
            // Header Row
            Text(
                text = "First Name",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            )
            Text(
                text = "Last Name",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .background(Color.White)
        ) {
            // First Name Input
            BasicTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = false
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
                    .background(if (firstNameError) Color(0xFFFFCDD2) else Color.Transparent),
                decorationBox = { innerTextField ->
                    if (firstName.isEmpty()) {
                        Text(
                            text = "First Name",
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )

            // Last Name Input
            BasicTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = false
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
                    .background(if (lastNameError) Color(0xFFFFCDD2) else Color.Transparent),
                decorationBox = { innerTextField ->
                    if (lastName.isEmpty()) {
                        Text(
                            text = "Last Name",
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gender
        Text(
            text = "Gender",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (genderError) Color(0xFFFFCDD2) else Color.Transparent)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == 0,
                    onClick = {
                        gender = 0
                        genderError = false
                    }
                )
                Text(
                    text = "Male",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == 1,
                    onClick = {
                        gender = 1
                        genderError = false
                    }
                )
                Text(
                    text = "Female",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Birthday
        Text(
            text = "Birthday",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        var showCalendar by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Birthday Text Field (readonly)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .background(
                            if (birthdayError) Color(0xFFFFCDD2) else Color.White,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (birthday.isEmpty()) {
                        Text(
                            text = "Birthday",
                            color = Color.Gray
                        )
                    } else {
                        Text(text = birthday)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Select Button
                Button(
                    onClick = { showCalendar = !showCalendar },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Select")
                }
            }

            // CalendarView
            if (showCalendar) {
                Spacer(modifier = Modifier.height(8.dp))
                AndroidView(
                    factory = { context ->
                        CalendarView(context).apply {
                            setOnDateChangeListener { _, year, month, dayOfMonth ->
                                val calendar = Calendar.getInstance()
                                calendar.set(year, month, dayOfMonth)
                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                birthday = dateFormat.format(calendar.time)
                                birthdayError = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Address
        Text(
            text = "Address",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            value = address,
            onValueChange = {
                address = it
                addressError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    1.dp,
                    Color.Gray,
                    RoundedCornerShape(4.dp)
                )
                .background(
                    if (addressError) Color(0xFFFFCDD2) else Color.White,
                    RoundedCornerShape(4.dp)
                )
                .padding(12.dp),
            decorationBox = { innerTextField ->
                if (address.isEmpty()) {
                    Text(
                        text = "Address",
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        Text(
            text = "Email",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    1.dp,
                    Color.Gray,
                    RoundedCornerShape(4.dp)
                )
                .background(
                    if (emailError) Color(0xFFFFCDD2) else Color.White,
                    RoundedCornerShape(4.dp)
                )
                .padding(12.dp),
            decorationBox = { innerTextField ->
                if (email.isEmpty()) {
                    Text(
                        text = "Email",
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Terms of Use
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (termsError) Color(0xFFFFCDD2) else Color.Transparent)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreedToTerms,
                onCheckedChange = {
                    agreedToTerms = it
                    termsError = false
                }
            )
            Text(
                text = "I agree to Terms of Use",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Register Button
        Button(
            onClick = {
                // Validate all fields
                firstNameError = firstName.isEmpty()
                lastNameError = lastName.isEmpty()
                genderError = gender == null
                birthdayError = birthday.isEmpty()
                addressError = address.isEmpty()
                emailError = email.isEmpty()
                termsError = !agreedToTerms

                val isValid = !firstNameError && !lastNameError && !genderError &&
                        !birthdayError && !addressError && !emailError && !termsError

                if (isValid) {
                    // Registration successful - show message
                    // In a real app, you would handle the registration logic here
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Register",
                fontSize = 16.sp
            )
        }
    }
}