package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.model.ShippingOrder
import com.example.model.StatCardData
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipViewModel

@Composable
fun StatusPill(status: OrderStatus, modifier: Modifier = Modifier) {
    val isDark = LocalThemeIsDark.current
    val (bgColor, textColor) = when (status) {
        OrderStatus.DELIVERING -> if (isDark) Color(0xFF432507) to Color(0xFFFFB84D) else Color(0xFFFFF7ED) to Color(0xFFC2410C)
        OrderStatus.PENDING -> if (isDark) Color(0xFF102747) to Color(0xFF93C5FD) else Color(0xFFEFF6FF) to Color(0xFF1D4ED8)
        OrderStatus.COMPLETED -> if (isDark) Color(0xFF063321) to Color(0xFF6EE7B7) else Color(0xFFECFDF5) to Color(0xFF047857)
        OrderStatus.CANCELLED -> if (isDark) Color(0xFF381313) to Color(0xFFFCA5A5) else Color(0xFFFEF2F2) to Color(0xFFB91C1C)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun StatMetricCard(
    stat: StatCardData,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    val isDark = LocalThemeIsDark.current
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface else Color.White
    val borderCol = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f) else Color(0xFFE2E8F0)

    Card(
        modifier = modifier
            .border(1.dp, borderCol, RoundedCornerShape(16.dp))
            .testTag("stat_card_${stat.title}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconColor.copy(alpha = if (isDark) 0.25f else 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (stat.isPositive) SuccessGreen.copy(alpha = if (isDark) 0.25f else 0.12f)
                            else ErrorRed.copy(alpha = if (isDark) 0.25f else 0.12f)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stat.trend,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (stat.isPositive) (if (isDark) Color(0xFF6EE7B7) else SuccessGreen) else ErrorRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stat.count,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stat.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SearchBarField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    val isDark = LocalThemeIsDark.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = BrandBlue,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandBlue,
            unfocusedBorderColor = if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0),
            focusedContainerColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF8FAFC),
            unfocusedContainerColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF8FAFC)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_bar_input")
    )
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                color = BrandBlue,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onActionClick() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .testTag("action_$title")
            )
        }
    }
}

@Composable
fun OrderListItemCard(
    order: ShippingOrder,
    onTrackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalThemeIsDark.current
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface else Color.White
    val borderCol = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f) else Color(0xFFE2E8F0)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderCol, RoundedCornerShape(16.dp))
            .clickable { onTrackClick() }
            .testTag("order_card_${order.trackingNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Tracking number & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrandBlue.copy(alpha = if (isDark) 0.25f else 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = order.trackingNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${order.serviceType} • ${order.weightKg} kg",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                StatusPill(status = order.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Route visual indicators
            Row(modifier = Modifier.fillMaxWidth()) {
                // Pin dots and connecting line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 4.dp, end = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(BrandBlue)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.5.dp)
                            .height(26.dp)
                            .background(if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1))
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(BrandOrange)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.senderAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = order.recipientAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Fee, COD and Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Cước phí: ${GoShipViewModel.formatVnd(order.feeVnd)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    if (order.codVnd > 0) {
                        Text(
                            text = "COD: ${GoShipViewModel.formatVnd(order.codVnd)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandBlue.copy(alpha = if (isDark) 0.2f else 0.08f))
                        .clickable { onTrackClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Theo dõi",
                        style = MaterialTheme.typography.labelMedium,
                        color = BrandBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveMapView(
    driverEtaMinutes: Int,
    distanceKm: Float,
    modifier: Modifier = Modifier
) {
    val isDark = LocalThemeIsDark.current
    var zoomLevel by remember { mutableFloatStateOf(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isDark) Color(0xFF0F172A) else Color(0xFFE2E8F0))
            .border(
                1.dp,
                if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                RoundedCornerShape(20.dp)
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background road network map rendering
            val roadColor = if (isDark) Color(0xFF1E293B) else Color(0xFFFFFFFF)
            val highwayColor = if (isDark) Color(0xFF334155) else Color(0xFFFEF3C7)
            val riverColor = if (isDark) Color(0xFF0C2442) else Color(0xFFBFDBFE)

            // Draw curved river
            val riverPath = Path().apply {
                moveTo(0f, h * 0.75f)
                cubicTo(w * 0.3f, h * 0.65f, w * 0.6f, h * 0.85f, w, h * 0.68f)
            }
            drawPath(
                path = riverPath,
                color = riverColor,
                style = Stroke(width = 28f * zoomLevel, cap = StrokeCap.Round)
            )

            // Draw Grid roads
            val roadStroke = 7f * zoomLevel
            for (i in 1..8) {
                val y = (h / 9f) * i
                drawLine(
                    color = roadColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = roadStroke
                )
            }
            for (j in 1..8) {
                val x = (w / 9f) * j
                drawLine(
                    color = roadColor,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = roadStroke
                )
            }

            // Diagonal Express Avenue
            drawLine(
                color = highwayColor,
                start = Offset(0f, h * 0.2f),
                end = Offset(w, h * 0.8f),
                strokeWidth = 14f * zoomLevel
            )

            // Route Path (From Hub to Driver to Destination)
            val pStart = Offset(w * 0.18f, h * 0.35f)
            val pDriver = Offset(w * 0.52f, h * 0.48f)
            val pEnd = Offset(w * 0.82f, h * 0.28f)

            // Solid completed route
            drawLine(
                color = BrandBlue,
                start = pStart,
                end = pDriver,
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )

            // Dashed pending route
            val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 12f), 0f)
            val pendingRoute = Path().apply {
                moveTo(pDriver.x, pDriver.y)
                lineTo(pEnd.x, pEnd.y)
            }
            drawPath(
                path = pendingRoute,
                color = BrandOrange,
                style = Stroke(width = 6f, pathEffect = dashedEffect, cap = StrokeCap.Round)
            )

            // Start Hub Marker
            drawCircle(color = BrandBlue.copy(alpha = 0.3f), radius = 14f, center = pStart)
            drawCircle(color = BrandBlue, radius = 7f, center = pStart)

            // Destination Marker
            drawCircle(color = BrandOrange.copy(alpha = 0.3f), radius = 16f, center = pEnd)
            drawCircle(color = BrandOrange, radius = 8f, center = pEnd)

            // Courier pulse radar effect
            drawCircle(
                color = BrandBlue.copy(alpha = (1f - (pulseScale - 0.8f) / 1.4f).coerceIn(0f, 0.4f)),
                radius = 16f * pulseScale,
                center = pDriver
            )
            drawCircle(color = Color.White, radius = 14f, center = pDriver)
            drawCircle(color = BrandBlue, radius = 10f, center = pDriver)
        }

        // Live badge overlay at top left
        Row(
            modifier = Modifier
                .padding(14.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.92f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tài xế cách $distanceKm km • Đến sau ~$driverEtaMinutes phút",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Map control buttons at top right
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(4.dp, CircleShape)
                    .clickable { zoomLevel = (zoomLevel + 0.2f).coerceAtMost(2.0f) },
                shape = CircleShape,
                color = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom in", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(4.dp, CircleShape)
                    .clickable { zoomLevel = (zoomLevel - 0.2f).coerceAtLeast(0.8f) },
                shape = CircleShape,
                color = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom out", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
