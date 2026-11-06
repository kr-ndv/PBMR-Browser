package com.pbmr.browser.feature.browser

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pbmr.browser.core.design.components.FamilyScreen
import com.pbmr.browser.core.design.tokens.FamilySpacing
import com.pbmr.browser.core.reading.ArticleContent

enum class ReaderTheme(val bg: Color, val text: Color) {
    DEFAULT(Color(0xFFF5F6F0), Color(0xFF17231D)),
    SEPIA(Color(0xFFFBF0D9), Color(0xFF382F24)),
    DARK(Color(0xFF14231B), Color(0xFFF2F6EF))
}

/**
 * Distraction-Free Reader Mode Screen
 */
@Composable
fun ReaderModeScreen(
    article: ArticleContent,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fontSizeSp by remember { mutableStateOf(17) }
    var readerTheme by remember { mutableStateOf(ReaderTheme.DEFAULT) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(readerTheme.bg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 700.dp)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Toolbar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit Reader Mode",
                        tint = readerTheme.text
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Font Size Toggles
                    IconButton(onClick = { if (fontSizeSp > 13) fontSizeSp -= 2 }) {
                        Text("A-", style = MaterialTheme.typography.titleMedium, color = readerTheme.text)
                    }
                    IconButton(onClick = { if (fontSizeSp < 26) fontSizeSp += 2 }) {
                        Text("A+", style = MaterialTheme.typography.titleMedium, color = readerTheme.text)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Theme Swatches
                    ReaderTheme.values().forEach { theme ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(theme.bg)
                                .border(
                                    width = if (theme == readerTheme) 2.dp else 1.dp,
                                    color = readerTheme.text,
                                    shape = CircleShape
                                )
                                .clickable { readerTheme = theme }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Article Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = article.sourceDomain.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = readerTheme.text.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = readerTheme.text
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${article.estimatedReadTimeMinutes} min read",
                    style = MaterialTheme.typography.bodySmall,
                    color = readerTheme.text.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = readerTheme.text.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                article.paragraphs.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp * 1.55).sp,
                            fontFamily = FontFamily.Serif
                        ),
                        color = readerTheme.text
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
