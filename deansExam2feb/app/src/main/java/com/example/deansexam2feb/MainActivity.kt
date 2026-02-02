/* Student Number: ST10494127
   Full Name: Vuyo Sandile Shezi */

package com.example.deansexam2feb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.deansexam2feb.ui.theme.DeansExam2febTheme

// Main Activity of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Makes content go edge-to-edge on the screen

        setContent {
            DeansExam2febTheme {
                // State variables to store book reviews
                var bookTitles by remember { mutableStateOf(listOf<String>()) }
                var authors by remember { mutableStateOf(listOf<String>()) }
                var ratings by remember { mutableStateOf(listOf<Int>()) }
                var comments by remember { mutableStateOf(listOf<String>()) }

                var showDetailScreen by remember { mutableStateOf(false) } // Toggle between main and detail screens

                // Scaffold provides basic material design layout structure
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (showDetailScreen) {
                            // Show screen to add a new book review
                            DetailScreen(
                                onSave = { title, author, rating, comment ->
                                    // Add new book review to lists
                                    bookTitles = bookTitles + title
                                    authors = authors + author
                                    ratings = ratings + rating
                                    comments = comments + comment
                                    showDetailScreen = false // Return to main screen
                                },
                                onBack = { showDetailScreen = false } // Go back without saving
                            )
                        } else {
                            // Show main screen with list of books
                            MainScreen(
                                bookTitles = bookTitles,
                                authors = authors,
                                ratings = ratings,
                                comments = comments,
                                onAddReview = { showDetailScreen = true } // Go to detail screen
                            )
                        }
                    }
                }
            }
        }
    }
}

// Main screen showing book list and average rating
@Composable
fun MainScreen(
    bookTitles: List<String>,
    authors: List<String>,
    ratings: List<Int>,
    comments: List<String>,
    onAddReview: () -> Unit
) {
    val average = if (ratings.isNotEmpty()) ratings.average() else 0.0 // Calculate average rating

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // App title
        Text(
            text = "Welcome to the Best Book Review App!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to add a new book review
        Button(
            onClick = onAddReview,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Add to Book Review", color = MaterialTheme.colorScheme.onSecondary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display average rating
        Text(
            text = "Average Rating: %.2f".format(average),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn to show scrollable list of books
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(bookTitles) { index, title ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Book details
                        Text(text = title, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Author: ${authors[index]}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Rating: ${ratings[index]}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Comment: ${comments[index]}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

// Screen to add a new book review
@Composable
fun DetailScreen(
    onSave: (title: String, author: String, rating: Int, comment: String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // State variables for input fields
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var author by remember { mutableStateOf(TextFieldValue("")) }
    var ratingText by remember { mutableStateOf(TextFieldValue("")) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Screen title
        Text(
            text = "Add Book Review",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Input for book title
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Book Title") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Input for author name
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Author") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Input for rating (1-5)
        OutlinedTextField(
            value = ratingText,
            onValueChange = { ratingText = it },
            label = { Text("Rating 1-5") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Input for comment
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Buttons row
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Save button
            Button(
                onClick = {
                    val rating = ratingText.text.toIntOrNull()
                    // Validate all fields
                    if (title.text.isBlank() || author.text.isBlank() || ratingText.text.isBlank() || comment.text.isBlank()) {
                        Toast.makeText(context, "Fill in all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    // Validate rating range
                    if (rating == null || rating < 1 || rating > 5) {
                        Toast.makeText(context, "Rating must be 1 to 5", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onSave(title.text, author.text, rating, comment.text) // Call save callback
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Save", color = MaterialTheme.colorScheme.onSecondary)
            }

            // Back button
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Back", color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    }
}
