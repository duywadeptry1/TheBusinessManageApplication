package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.OrderStatus
import com.example.ui.components.OrderListItemCard
import com.example.ui.components.SearchBarField
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.PromoCardDarkGradient
import com.example.ui.theme.PromoCardGradient
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipUiState

@Composable
fun HomeScreen(
    uiState: GoShipUiState,
    onSearchChange: (String) -> Unit,
    onTabSelect: (Int) -> Unit,
    onCreateOrderClick: () -> Unit,
    onOpenDbImportClick: () -> Unit,
    onTrackOrderClick: (String) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalThemeIsDark.current
    val activeDeliveringCount = uiState.orders.count {
        it.status == OrderStatus.DELIVERING || it.status == OrderStatus.PENDING
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 32.dp else 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Top Header: User Profile, Greeting & Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("user_profile_header")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BrandBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AN",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Alex Nguyễn",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BrandOrange.copy(alpha = if (isDark) 0.3f else 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "VIP Kim Cương",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = BrandOrange,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "Database đang lưu ${uiState.orders.size} đơn ($activeDeliveringCount đang xử lý)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Header Right Actions: DB Import, Dark Mode Switcher & Notification Bell
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onOpenDbImportClick,
                            modifier = Modifier.testTag("header_db_import_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = "Import Database",
                                tint = BrandBlue
                            )
                        }

                        IconButton(
                            onClick = { onToggleDarkMode(!isDark) },
                            modifier = Modifier.testTag("toggle_dark_mode_button")
                        ) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Dark Mode",
                                tint = if (isDark) Color(0xFFFBBF24) else Color(0xFF475569)
                            )
                        }

                        Box {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ErrorRed)
                                    .align(Alignment.TopEnd)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                SearchBarField(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchChange,
                    placeholderText = "Tra cứu mã vận đơn, người nhận hoặc dịch vụ…"
                )
            }

            // 4 Stats Cards (Responsive: 4 in a row on tablet, 2x2 grid on phone)
            item {
                val statColors = listOf(BrandBlue, BrandOrange, SuccessGreen, ErrorRed)

                if (isTablet) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        uiState.stats.forEachIndexed { index, stat ->
                            StatMetricCard(
                                stat = stat,
                                iconColor = statColors.getOrElse(index) { BrandBlue },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            uiState.stats.take(2).forEachIndexed { index, stat ->
                                StatMetricCard(
                                    stat = stat,
                                    iconColor = statColors[index],
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            uiState.stats.drop(2).take(2).forEachIndexed { index, stat ->
                                StatMetricCard(
                                    stat = stat,
                                    iconColor = statColors[index + 2],
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Actions Bar
            item {
                SectionHeader(title = "Thao tác nhanh")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Add,
                        label = "Tạo đơn mới",
                        isPrimary = true,
                        onClick = onCreateOrderClick,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Default.CloudDownload,
                        label = "Nhập DB mới",
                        isPrimary = false,
                        onClick = onOpenDbImportClick,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_action_import_db")
                    )
                    QuickActionButton(
                        icon = Icons.Default.Send,
                        label = "Gửi hỏa tốc",
                        isPrimary = false,
                        onClick = { onTabSelect(3) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Default.AccountBalanceWallet,
                        label = "Nạp ví",
                        isPrimary = false,
                        onClick = { onTabSelect(4) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Promotional Banner Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("promo_banner_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isDark) PromoCardDarkGradient else PromoCardGradient)
                            .border(
                                1.dp,
                                if (isDark) Color(0xFF7C2D12) else Color(0xFFFED7AA),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(BrandOrange)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "ƯU ĐÃI THÁNG 11",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Giảm 20% đơn giao liên tỉnh",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFFFFEDD5) else Color(0xFF7C2D12)
                                )
                                Text(
                                    text = "Áp dụng tối đa 50.000 đ cho tất cả kiện hàng",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDark) Color(0xFFFDBA74) else Color(0xFF9A3412)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandOrange)
                                    .clickable { }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "GOSHIP20",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy code",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Recent Orders Header
            item {
                SectionHeader(
                    title = "Đơn hàng gần đây",
                    actionText = "Xem tất cả (${uiState.orders.size})",
                    onActionClick = { onTabSelect(1) }
                )
            }

            // Recent Orders List (Top 3)
            items(uiState.orders.take(3), key = { it.id }) { order ->
                OrderListItemCard(
                    order = order,
                    onTrackClick = {
                        onTrackOrderClick(order.trackingNumber)
                        onTabSelect(2) // Jump to tracking screen
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalThemeIsDark.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                                .size(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (isPrimary) BrandBlue
                    else if (isDark) MaterialTheme.colorScheme.surfaceVariant
                    else Color(0xFFEFF6FF)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isPrimary) Color.White else BrandBlue,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}
