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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
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
import com.example.model.ScheduleDayItem
import com.example.ui.components.OrderListItemCard
import com.example.ui.components.SearchBarField
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipUiState

@Composable
fun HomeScreen(
    uiState: GoShipUiState,
    onSearchChange: (String) -> Unit,
    onTabSelect: (Int) -> Unit,
    onSelectScheduleDay: (Int) -> Unit = {},
    onPerformCheckIn: () -> Unit = {},
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

            // Top Header: Executive Profile, Role Badge & Action Buttons
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
                                        .background(BrandBlue.copy(alpha = if (isDark) 0.3f else 0.12f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "CEO • Quản trị",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = BrandBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "The Business Manage • ${uiState.orders.size} dự án ($activeDeliveringCount đang xử lý)",
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
                    placeholderText = "Tìm mã dự án, hợp đồng, phòng ban hoặc nhân sự…"
                )
            }

            // 4 Business KPI Cards (Responsive: 4 in a row on tablet, 2x2 grid on phone)
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

            // Weekly Schedule & Shift / Attendance Strip (Matches Screen 1 of Figma Design)
            item {
                WeeklyScheduleCard(
                    schedule = uiState.weeklySchedule,
                    selectedIndex = uiState.selectedScheduleDayIndex,
                    attendanceCount = uiState.attendanceCount,
                    onSelectDay = onSelectScheduleDay
                )
            }

            // Quick Business Process Actions Bar
            item {
                SectionHeader(title = "Quy trình nghiệp vụ nhanh")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Add,
                        label = "Tạo dự án mới",
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
                        icon = Icons.Default.LocationOn,
                        label = "Chấm công GPS",
                        isPrimary = false,
                        onClick = {
                            onPerformCheckIn()
                            onTabSelect(2)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Default.AccountBalanceWallet,
                        label = "Duyệt quỹ",
                        isPrimary = false,
                        onClick = { onTabSelect(4) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent Business Projects & Tasks Header
            item {
                SectionHeader(
                    title = "Dự án & Quy trình trọng tâm",
                    actionText = "Xem tất cả (${uiState.orders.size})",
                    onActionClick = { onTabSelect(1) }
                )
            }

            // Recent Projects & Contracts List (Top 3)
            items(uiState.orders.take(3), key = { it.id }) { order ->
                OrderListItemCard(
                    order = order,
                    onTrackClick = {
                        onTrackOrderClick(order.trackingNumber)
                        onTabSelect(2)
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun WeeklyScheduleCard(
    schedule: List<ScheduleDayItem>,
    selectedIndex: Int,
    attendanceCount: Int,
    onSelectDay: (Int) -> Unit
) {
    val isDark = LocalThemeIsDark.current
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface else Color.White
    val borderCol = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.4f) else Color(0xFFE2E8F0)
    val activeDay = schedule.getOrNull(selectedIndex) ?: schedule.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderCol, RoundedCornerShape(16.dp))
            .testTag("weekly_schedule_card"),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lịch điều hành & Giao ban tuần",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Tháng 11, 2026",
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7-Day Strip (T2 .. CN) with highlighted active pill as in Figma Screen 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                schedule.forEachIndexed { index, day ->
                    val isSelected = index == selectedIndex
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) BrandBlue
                                else if (isDark) MaterialTheme.colorScheme.surfaceVariant
                                else Color(0xFFF8FAFC)
                            )
                            .clickable { onSelectDay(index) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayOfWeek,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = day.dayNumber,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            if (activeDay != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF8FAFC))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(BrandBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                tint = BrandBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = activeDay.meetingTitle,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${activeDay.department} • ${activeDay.timeSlot}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SuccessGreen.copy(alpha = if (isDark) 0.25f else 0.12f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "$attendanceCount/130 có mặt",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDark) Color(0xFF6EE7B7) else SuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(BrandOrange.copy(alpha = if (isDark) 0.25f else 0.12f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = activeDay.statusBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
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
