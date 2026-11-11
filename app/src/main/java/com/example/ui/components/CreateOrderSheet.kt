package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.model.ShippingService
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderSheet(
    isVisible: Boolean,
    services: List<ShippingService>,
    selectedService: ShippingService?,
    onServiceSelect: (ShippingService) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (recipientName: String, recipientAddress: String, weightKg: Float, paymentMethod: String) -> Unit
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isDark = LocalThemeIsDark.current

    var recipientName by remember { mutableStateOf("Trần Thu Trang (Project Manager)") }
    var recipientAddress by remember { mutableStateOf("Dự án: Mở rộng Hệ thống Quản trị Chi nhánh Miền Bắc") }
    var weightKg by remember { mutableFloatStateOf(10.0f) }
    var selectedDepartmentCode by remember { mutableStateOf("B2B") }

    val activeService = selectedService ?: services.firstOrNull()
    val baseBudget = activeService?.basePriceVnd ?: 25000000L
    val workloadMultiplier = 0.5f + (weightKg * 0.08f)
    val estimatedBudget = (baseBudget * workloadMultiplier).toLong()
    val reserveFund = (estimatedBudget * 0.15f).toLong()
    val totalApproved = estimatedBudget - reserveFund

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Khởi tạo Dự án & Quy trình",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Amber Alert Banner (From Figma modal screenshot)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF3E2405) else Color(0xFFFFF7ED)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isDark) Color(0xFF78350F) else Color(0xFFFED7AA),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BrandOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Quy trình Phê duyệt Doanh nghiệp",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFFFFEDD5) else Color(0xFF9A3412)
                        )
                        Text(
                            text = "Tự động phân bổ ngân sách phòng ban & đồng bộ lên Room SQLite Database!",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) Color(0xFFFDBA74) else Color(0xFFC2410C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Initiator / Executive Office Box
            Text(
                text = "Cấp phê duyệt & Đơn vị khởi tạo",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF8FAFC))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.BusinessCenter,
                    contentDescription = null,
                    tint = BrandBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Alex Nguyễn (CEO) • Ban Giám Đốc Điều Hành",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Trụ sở Chính: 120 Hoàng Hoa Thám, Ba Đình, Hà Nội",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Project & Assignee Input Fields
            Text(
                text = "Thông tin Dự án / Quy trình công việc",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = recipientName,
                onValueChange = { recipientName = it },
                label = { Text("Nhân sự chủ trì / Quản lý dự án (Assignee)") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_recipient_name"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = recipientAddress,
                onValueChange = { recipientAddress = it },
                label = { Text("Tên dự án / Mục tiêu bàn giao chi tiết") },
                leadingIcon = { Icon(Icons.Default.Assignment, contentDescription = null, tint = BrandOrange) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_recipient_address"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandBlue,
                    unfocusedBorderColor = if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Workload Slider (Man-days)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Thời lượng / Định mức nhân công",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${(weightKg * 10).roundToInt() / 10f} ngày công",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue
                )
            }

            Slider(
                value = weightKg,
                onValueChange = { weightKg = it },
                valueRange = 1.0f..30.0f,
                steps = 28,
                colors = SliderDefaults.colors(
                    thumbColor = BrandBlue,
                    activeTrackColor = BrandBlue,
                    inactiveTrackColor = if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Department selector
            Text(
                text = "Phòng ban chịu trách nhiệm chính",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "B2B" to "Kinh doanh",
                    "TECH" to "Kỹ thuật",
                    "MKT" to "Marketing",
                    "FIN" to "Tài chính"
                ).forEach { (code, title) ->
                    val isSelected = selectedDepartmentCode == code
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) BrandBlue
                                else if (isDark) MaterialTheme.colorScheme.surfaceVariant
                                else Color(0xFFF1F5F9)
                            )
                            .clickable { selectedDepartmentCode = code }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = if (isDark) MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) else Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(14.dp))

            // Budget summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tổng dự toán ngân sách:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = GoShipViewModel.formatVnd(estimatedBudget), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tối ưu hóa chi phí nội bộ (15%):", style = MaterialTheme.typography.bodyMedium, color = SuccessGreen)
                Text(text = "-${GoShipViewModel.formatVnd(reserveFund)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = SuccessGreen)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Quyền phê duyệt nhanh (CEO):", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "Hiệu lực ngay", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Ngân sách giải ngân duyệt cấp:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = GoShipViewModel.formatVnd(totalApproved), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BrandBlue)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Actions: Cancel & Confirm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Hủy", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = {
                        onConfirm(recipientName, recipientAddress, weightKg, selectedDepartmentCode)
                    },
                    modifier = Modifier
                        .weight(2f)
                        .testTag("btn_confirm_create_order"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Phê duyệt & Lưu DB", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
