package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.OrderStatus
import com.example.model.ShippingOrder
import com.example.ui.components.OrderListItemCard
import com.example.ui.components.SearchBarField
import com.example.ui.components.StatusPill
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipUiState
import com.example.viewmodel.GoShipViewModel

@Composable
fun OrdersScreen(
    uiState: GoShipUiState,
    onSearchChange: (String) -> Unit,
    onFilterSelect: (OrderStatus?) -> Unit,
    onCreateOrderClick: () -> Unit,
    onOpenDbImportClick: () -> Unit,
    onUpdateOrderStatus: (String, OrderStatus) -> Unit,
    onDeleteOrder: (String) -> Unit,
    onTrackOrderClick: (String) -> Unit,
    onJumpToTracking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredOrders = remember(uiState.orders, uiState.searchQuery, uiState.selectedOrderFilter) {
        uiState.orders.filter { order ->
            val matchesFilter = uiState.selectedOrderFilter == null || order.status == uiState.selectedOrderFilter
            val matchesSearch = uiState.searchQuery.isBlank() ||
                    order.trackingNumber.contains(uiState.searchQuery, ignoreCase = true) ||
                    order.recipientName.contains(uiState.searchQuery, ignoreCase = true) ||
                    order.recipientAddress.contains(uiState.searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }

    var selectedOrderDetail by remember { mutableStateOf<ShippingOrder?>(null) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth > 700.dp

        Box(modifier = Modifier.fillMaxSize()) {
            if (isTablet) {
                // Tablet Master-Detail Two-Column Fluid Layout
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Left Column: Filter Chips & Orders List (55%)
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxSize()
                    ) {
                        OrdersTopHeader(
                            searchQuery = uiState.searchQuery,
                            onSearchChange = onSearchChange,
                            selectedFilter = uiState.selectedOrderFilter,
                            onFilterSelect = onFilterSelect,
                            onOpenDbImportClick = onOpenDbImportClick
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredOrders, key = { it.id }) { order ->
                                OrderListItemCard(
                                    order = order,
                                    onTrackClick = {
                                        selectedOrderDetail = order
                                    }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(40.dp)) }
                        }
                    }

                    // Right Column: Order Detail & Action Pane (45%)
                    Column(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxSize()
                    ) {
                        val activeOrder = filteredOrders.find { it.id == selectedOrderDetail?.id }
                            ?: filteredOrders.firstOrNull()
                        if (activeOrder != null) {
                            TabletOrderDetailPane(
                                order = activeOrder,
                                onUpdateStatus = { newStatus ->
                                    onUpdateOrderStatus(activeOrder.id, newStatus)
                                },
                                onDeleteOrder = {
                                    onDeleteOrder(activeOrder.id)
                                },
                                onTrackLive = {
                                    onTrackOrderClick(activeOrder.trackingNumber)
                                    onJumpToTracking()
                                }
                            )
                        }
                    }
                }
            } else {
                // Mobile Fluid Single-Column Layout
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        OrdersTopHeader(
                            searchQuery = uiState.searchQuery,
                            onSearchChange = onSearchChange,
                            selectedFilter = uiState.selectedOrderFilter,
                            onFilterSelect = onFilterSelect,
                            onOpenDbImportClick = onOpenDbImportClick
                        )
                    }

                    if (filteredOrders.isEmpty()) {
                        item {
                            EmptyOrdersState(onImportClick = onOpenDbImportClick)
                        }
                    } else {
                        items(filteredOrders, key = { it.id }) { order ->
                            Column {
                                OrderListItemCard(
                                    order = order,
                                    onTrackClick = {
                                        onTrackOrderClick(order.trackingNumber)
                                        onJumpToTracking()
                                    }
                                )
                                // Quick DB Status & Delete bar below each card
                                OrderQuickDbActionsBar(
                                    order = order,
                                    onUpdateStatus = { newStatus ->
                                        onUpdateOrderStatus(order.id, newStatus)
                                    },
                                    onDelete = {
                                        onDeleteOrder(order.id)
                                    }
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }
            }

            // Floating Action Button to create order
            FloatingActionButton(
                onClick = onCreateOrderClick,
                containerColor = BrandBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .testTag("fab_create_order")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tạo đơn mới", modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
private fun OrderQuickDbActionsBar(
    order: ShippingOrder,
    onUpdateStatus: (OrderStatus) -> Unit,
    onDelete: () -> Unit
) {
    val isDark = LocalThemeIsDark.current
    val nextStatus = when (order.status) {
        OrderStatus.PENDING -> OrderStatus.DELIVERING
        OrderStatus.DELIVERING -> OrderStatus.COMPLETED
        OrderStatus.COMPLETED -> OrderStatus.DELIVERING
        OrderStatus.CANCELLED -> OrderStatus.PENDING
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SuccessGreen.copy(alpha = if (isDark) 0.2f else 0.1f))
                .clickable { onUpdateStatus(nextStatus) }
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Chuyển sang: ${nextStatus.label}",
                style = MaterialTheme.typography.labelSmall,
                color = SuccessGreen,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onDelete() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Xóa đơn",
                tint = ErrorRed,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Xóa",
                style = MaterialTheme.typography.labelSmall,
                color = ErrorRed
            )
        }
    }
}

@Composable
private fun OrdersTopHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedFilter: OrderStatus?,
    onFilterSelect: (OrderStatus?) -> Unit,
    onOpenDbImportClick: () -> Unit
) {
    val isDark = LocalThemeIsDark.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quản lý đơn hàng",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(BrandBlue.copy(alpha = if (isDark) 0.25f else 0.1f))
                    .clickable { onOpenDbImportClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("orders_import_db_btn"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDownload,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nhập DB",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        SearchBarField(
            query = searchQuery,
            onQueryChange = onSearchChange,
            placeholderText = "Tìm theo mã đơn hoặc người nhận…"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipItem(
                label = "Tất cả",
                isSelected = selectedFilter == null,
                onClick = { onFilterSelect(null) }
            )
            OrderStatus.entries.forEach { status ->
                FilterChipItem(
                    label = status.label,
                    isSelected = selectedFilter == status,
                    onClick = { onFilterSelect(status) }
                )
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = LocalThemeIsDark.current
    val bg = if (isSelected) BrandBlue
    else if (isDark) MaterialTheme.colorScheme.surfaceVariant
    else Color(0xFFF1F5F9)

    val textColor = if (isSelected) Color.White
    else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("filter_chip_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
private fun TabletOrderDetailPane(
    order: ShippingOrder,
    onUpdateStatus: (OrderStatus) -> Unit,
    onDeleteOrder: () -> Unit,
    onTrackLive: () -> Unit
) {
    val isDark = LocalThemeIsDark.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0),
                RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Chi tiết kiện hàng",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = order.trackingNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusPill(status = order.status)
                    IconButton(onClick = onDeleteOrder) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Xóa", tint = ErrorRed)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) else Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(16.dp))

            // Route points
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 4.dp, end = 12.dp)
                ) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(BrandBlue))
                    Box(modifier = Modifier.width(1.5.dp).height(36.dp).background(if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1)))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(BrandOrange))
                }
                Column {
                    Text(text = "Điểm gửi hàng", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${order.senderName} • ${order.senderAddress}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "Điểm giao nhận", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${order.recipientName} • ${order.recipientAddress}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Updater Row
            Text(
                text = "Cập nhật trạng thái trong Database",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderStatus.entries.forEach { st ->
                    FilterChipItem(
                        label = st.label,
                        isSelected = order.status == st,
                        onClick = { onUpdateStatus(st) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF8FAFC))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Gói dịch vụ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = order.serviceType, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Column {
                    Text(text = "Trọng lượng", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${order.weightKg} kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Column {
                    Text(text = "Cước phí", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = GoShipViewModel.formatVnd(order.feeVnd), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = BrandBlue)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onTrackLive,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Theo dõi hành trình thực tế", style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
        }
    }
}

@Composable
private fun EmptyOrdersState(onImportClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(BrandBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Không tìm thấy đơn hàng phù hợp",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Hãy thử tìm kiếm khác hoặc nhập dữ liệu mới từ Database",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onImportClick, shape = RoundedCornerShape(10.dp)) {
            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Nhập dữ liệu từ Database")
        }
    }
}
