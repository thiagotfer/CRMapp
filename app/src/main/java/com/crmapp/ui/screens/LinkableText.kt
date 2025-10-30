package com.crmapp.ui.screens

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController

@Composable
fun LinkableText(
    text: String,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit
) {
    val regex = Regex("\\[(.*?)\\]\\((.*?)\\)")
    val matches = regex.findAll(text)

    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        for (match in matches) {
            val (linkText, route) = match.destructured
            append(text.substring(lastIndex, match.range.first))
            pushStringAnnotation(tag = "route", annotation = route)
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(linkText)
            }
            pop()
            lastIndex = match.range.last + 1
        }
        if (lastIndex < text.length) append(text.substring(lastIndex))
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        modifier = modifier,
        onClick = { offset ->
            annotatedString.getStringAnnotations("route", offset, offset)
                .firstOrNull()?.let { annotation ->
                    onLinkClick(annotation.item)
                }
        }
    )
}