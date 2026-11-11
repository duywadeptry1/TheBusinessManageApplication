package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ShippingService
import com.example.ui.components.SearchBarField
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipUiState
import com.example.viewmodel.GoShipViewModel

@Composable
fun ServicesScreen(
    uiState: GoShipUiState,
    onSearchChange: (String) -> Unit,
    onSelectService: (ShippingService) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredServices = remember(uiState.services, uiState.searchQuery) {
        if (uiState.searchQuery.isBlank()) {
            uiState.services
        } else {
            uiState.services.filter {
                it.name.contains(uiState.searchQuery, ignoreCase = true) ||
                        it.description.contains(uiState.searchQuery, ignoreCase = true) ||
                        it.deliveryTime.contains(uiState.searchQuery, ignoreCase = true)
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth > 700.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 32.dp else 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Column {
                    Text(
                        text = "Nhân sự Chủ chốt & Giải pháp",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Danh bạ nhân sự quản lý (HR) và danh mục sản phẩm/giải pháp doanh nghiệp",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                SearchBarField(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchChange,
                    placeholderText = "Tìm kiếm nhân sự, phòng ban hoặc gói giải pháp…"
                )
            }

            items(filteredServices, key = { it.id }) { service ->
                val isStaff = service.name.contains("—")
                val icon = when {
                    isStaff -> Icons.Default.Badge
                    service.name.contains("POS", ignoreCase = true) -> Icons.Default.Devices
                    else -> Icons.Default.Inventory2
                }

                val iconBg = when {
                    isStaff && service.id == "SRV-01" -> BrandBlue
                    isStaff -> BrandOrange
                    service.name.contains("POS", ignoreCase = true) -> AccentPurple
                    else -> SuccessGreen
                }

                ServiceItemCard(
                    service = service,
                    icon = icon,
                    iconColor = iconBg,
                    isStaff = isStaff,
                    onBookClick = { onSelectService(service) }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun ServiceItemCard(
    service: ShippingService,
    icon: ImageVector,
    iconColor: Color,
    isStaff: Boolean,
    onBookClick: () -> Unit
) {
    val isDark = LocalThemeIsDark.current
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface else Color.White
    val borderCol = if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderCol, RoundedCornerShape(16.dp))
            .testTag("service_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(iconColor.copy(alpha = if (isDark) 0.25f else 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isStaff) Icons.Default.VerifiedUser else Icons.Default.BusinessCenter,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = service.deliveryTime,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (service.popularTag != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrandOrange.copy(alpha = if (isDark) 0.25f else 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = service.popularTag,
                            style = MaterialTheme.typography.labelSmall,
                            color = BrandOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isStaff) "Định mức lương & KPI tháng" else "Đơn giá gói giải pháp",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = GoShipViewModel.formatVnd(service.basePriceVnd),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue
                    )
                }

                Button(
                    onClick = onBookClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    modifier = Modifier.testTag("btn_book_${service.id}")
                ) {
                    Text(
                        if (isStaff) "Giao dự án" else "Tạo hợp đồng",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
