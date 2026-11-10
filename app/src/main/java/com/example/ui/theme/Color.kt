package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Brand Palette
val BrandBlue = Color(0xFF1E60FF)
val BrandBlueDark = Color(0xFF1447C6)
val BrandBlueLight = Color(0xFFEBF2FF)
val BrandBlueContainer = Color(0xFFDBEAFE)

// Secondary & Accent Palette
val BrandOrange = Color(0xFFFF9500)
val BrandOrangeDark = Color(0xFFD97706)
val BrandOrangeLight = Color(0xFFFFF7ED)
val BrandOrangeContainer = Color(0xFFFFEDD5)

// Status & Semantic Colors
val SuccessGreen = Color(0xFF10B981)
val SuccessGreenContainer = Color(0xFFD1FAE5)
val WarningAmber = Color(0xFFF59E0B)
val WarningAmberContainer = Color(0xFFFEF3C7)
val ErrorRed = Color(0xFFEF4444)
val ErrorRedContainer = Color(0xFFFEE2E2)
val InfoCyan = Color(0xFF06B6D4)
val InfoCyanContainer = Color(0xFFCFFAFE)
val AccentPurple = Color(0xFF8B5CF6)
val AccentPurpleContainer = Color(0xFFEDE9FE)

// Light Theme Neutrals
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightOutline = Color(0xFFE2E8F0)
val LightOutlineVariant = Color(0xFFCBD5E1)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)
val LightTextTertiary = Color(0xFF94A3B8)

// Dark Theme Neutrals
val DarkBackground = Color(0xFF0B111E)
val DarkSurface = Color(0xFF151E2E)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkOutline = Color(0xFF2E3E55)
val DarkOutlineVariant = Color(0xFF475569)
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)
val DarkTextTertiary = Color(0xFF64748B)

// Gradients
val WalletCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0F2B66),
        Color(0xFF1644A8),
        Color(0xFF1E60FF)
    )
)

val PromoCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFF1EB),
        Color(0xFFFFEDD5)
    )
)

val PromoCardDarkGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF271B11),
        Color(0xFF3B2513)
    )
)
