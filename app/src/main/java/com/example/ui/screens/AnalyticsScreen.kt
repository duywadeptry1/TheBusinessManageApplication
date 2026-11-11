package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.DailyRevenue
import com.example.model.PaymentMethodShare
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WalletCardGradient
import com.example.viewmodel.GoShipUiState
import com.example.viewmodel.GoShipViewModel

@Composable
fun AnalyticsScreen(
    uiState: GoShipUiState,
    onOpenDbImportClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableIntStateOf(4) } // Default to T6 (Friday)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth > 700.dp

        if (isTablet) {
            // Tablet Fluid Two-Column Layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Column: Treasury Card & Bar Chart (55%)
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WalletBalanceCard(onImportClick = onOpenDbImportClick)
                    InteractiveRevenueChart(
                        revenues = uiState.dailyRevenues,
                        selectedIndex = selectedDayIndex,
                        onSelectIndex = { selectedDayIndex = it }
                    )
                }

                // Right Column: Cashflow breakdown & Audit Tips (45%)
                Column(
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PaymentBreakdownCard(shares = uiState.paymentShares)
                    QuickFinancialTipsCard()
                }
            }
        } else {
            // Mobile Fluid Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Quản trị Tài chính & Ngân sách",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                WalletBalanceCard(onImportClick = onOpenDbImportClick)

                InteractiveRevenueChart(
                    revenues = uiState.dailyRevenues,
                    selectedIndex = selectedDayIndex,
                    onSelectIndex = { selectedDayIndex = it }
                )

                PaymentBreakdownCard(shares = uiState.paymentShares)

                QuickFinancialTipsCard()

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun WalletBalanceCard(onImportClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("wallet_balance_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WalletCardGradient)
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quỹ Ngân sách Doanh nghiệp khả dụng",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFF6EE7B7),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+18.5%",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF6EE7B7),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "34,650,000 VND",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.2f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Chi phí vận hành tháng",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Text(
                            text = "15,590,000 ₫",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Doanh thu hợp đồng tuần",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Text(
                            text = "19,060,000 ₫",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6EE7B7)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action buttons on card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WalletActionButton(
                        icon = Icons.Default.AddCard,
                        label = "Cấp ngân sách",
                        onClick = onImportClick,
                        modifier = Modifier.weight(1f)
                    )
                    WalletActionButton(
                        icon = Icons.Default.AccountBalance,
                        label = "Giải ngân",
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                    WalletActionButton(
                        icon = Icons.Default.QrCodeScanner,
                        label = "Nhập DB",
                        onClick = onImportClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WalletActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.25f))
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun InteractiveRevenueChart(
    revenues: List<DailyRevenue>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit
) {
    val isDark = LocalThemeIsDark.current
    val maxRev = revenues.maxOfOrNull { it.amountVnd } ?: 1L
    val activeItem = revenues.getOrNull(selectedIndex) ?: revenues.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0),
                RoundedCornerShape(16.dp)
            )
            .testTag("revenue_bar_chart"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Doanh thu & Hiệu suất tuần",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Nhấn vào cột để xem doanh thu & số dự án hoàn tất",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Active Day Tooltip Card
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandBlue.copy(alpha = if (isDark) 0.25f else 0.12f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${activeItem.dayLabel}: ${GoShipViewModel.formatVnd(activeItem.amountVnd)} (${activeItem.orderCount} việc)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Canvas Bar Chart
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val slotWidth = size.width / revenues.size
                            val tapped = (offset.x / slotWidth).toInt().coerceIn(0, revenues.size - 1)
                            onSelectIndex(tapped)
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                val barWidth = (w / revenues.size) * 0.45f
                val slotWidth = w / revenues.size

                revenues.forEachIndexed { i, item ->
                    val x = (slotWidth * i) + (slotWidth - barWidth) / 2f
                    val barHeight = ((item.amountVnd.toFloat() / maxRev.toFloat()) * (h - 40f)).coerceAtLeast(16f)
                    val y = h - 30f - barHeight

                    val isSelected = i == selectedIndex
                    val isPeak = item.isPeak

                    val barColor = when {
                        isSelected -> BrandBlue
                        isPeak -> BrandOrange
                        else -> if (isDark) Color(0xFF334155) else Color(0xFFBFDBFE)
                    }

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
            }

            // Labels row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                revenues.forEachIndexed { i, item ->
                    val isSelected = i == selectedIndex
                    Text(
                        text = item.dayLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable { onSelectIndex(i) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentBreakdownCard(shares: List<PaymentMethodShare>) {
    val isDark = LocalThemeIsDark.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0),
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Cơ cấu Dòng tiền & Phân bổ Ngân sách",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            shares.forEach { share ->
                val barColor = when {
                    share.name.contains("Doanh thu") || share.name.contains("COD") -> SuccessGreen
                    share.name.contains("Vận hành") || share.name.contains("Ví") -> BrandBlue
                    else -> AccentPurple
                }

                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = share.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${share.percentage}% • ${GoShipViewModel.formatVnd(share.totalVnd)}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = barColor
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { share.percentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = barColor,
                        trackColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF1F5F9)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickFinancialTipsCard() {
    val isDark = LocalThemeIsDark.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF14243B) else Color(0xFFEFF6FF)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BrandBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Quy trình Đối soát & Kiểm toán Tự động",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue
                )
                Text(
                    text = "Báo cáo dòng tiền và lương thưởng KPI được đồng bộ trực tiếp với Room SQLite Database vào Thứ 2 và Thứ 6 hàng tuần.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
