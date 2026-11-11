package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.model.CourierDriver
import com.example.model.TrackingStep
import com.example.ui.components.InteractiveMapView
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.LocalThemeIsDark
import com.example.ui.theme.SuccessGreen
import com.example.viewmodel.GoShipUiState

@Composable
fun TrackingScreen(
    uiState: GoShipUiState,
    onPerformCheckIn: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val driver = uiState.activeDriver
    var isCalling by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth > 700.dp

        if (isTablet) {
            // Tablet Side-by-Side Fluid Layout: Left Branch Map (58%), Right Branch Info & Workflow Timeline (42%)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Map column
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxSize()
                ) {
                    TrackingTopBar(
                        trackingNumber = uiState.trackingOrderId,
                        isCheckedIn = uiState.isCheckedInToday
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    InteractiveMapView(
                        driverEtaMinutes = driver?.etaMinutes ?: 12,
                        distanceKm = driver?.currentDistanceKm ?: 0.1f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                // Branch & Workflow Steps Column
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (driver != null) {
                        DriverCard(
                            driver = driver,
                            isCheckedIn = uiState.isCheckedInToday,
                            isCalling = isCalling,
                            onPerformCheckIn = onPerformCheckIn,
                            onToggleCall = { isCalling = !isCalling }
                        )
                    }

                    TimelineCard(steps = uiState.trackingSteps)

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            // Mobile Layout: Branch Map on top, Branch Card & Operational Timeline below
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                TrackingTopBar(
                    trackingNumber = uiState.trackingOrderId,
                    isCheckedIn = uiState.isCheckedInToday
                )

                InteractiveMapView(
                    driverEtaMinutes = driver?.etaMinutes ?: 12,
                    distanceKm = driver?.currentDistanceKm ?: 0.1f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                )

                if (driver != null) {
                    DriverCard(
                        driver = driver,
                        isCheckedIn = uiState.isCheckedInToday,
                        isCalling = isCalling,
                        onPerformCheckIn = onPerformCheckIn,
                        onToggleCall = { isCalling = !isCalling }
                    )
                }

                TimelineCard(steps = uiState.trackingSteps)

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun TrackingTopBar(trackingNumber: String, isCheckedIn: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Chi nhánh & Chấm công GPS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Dự án trọng điểm: $trackingNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandBlue,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isCheckedIn) SuccessGreen.copy(alpha = 0.15f)
                    else BrandOrange.copy(alpha = 0.15f)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (isCheckedIn) "Đã chấm công" else "Ca hành chính",
                style = MaterialTheme.typography.labelSmall,
                color = if (isCheckedIn) SuccessGreen else BrandOrange,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DriverCard(
    driver: CourierDriver,
    isCheckedIn: Boolean,
    isCalling: Boolean,
    onPerformCheckIn: () -> Unit,
    onToggleCall: () -> Unit
) {
    val isDark = LocalThemeIsDark.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isDark) MaterialTheme.colorScheme.outline else Color(0xFFE2E8F0),
                RoundedCornerShape(16.dp)
            )
            .testTag("driver_info_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BrandBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = BrandBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = driver.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${driver.vehicleModel} • ${driver.licensePlate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "KPI Chi nhánh: ${driver.rating}/5.0 (${driver.completedTrips} dự án hoàn tất)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Process Button: GPS Attendance Check-in & Contact Branch Manager
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onPerformCheckIn,
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("btn_gps_checkin"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCheckedIn) SuccessGreen else BrandBlue
                    )
                ) {
                    Icon(
                        if (isCheckedIn) Icons.Default.CheckCircle else Icons.Default.MyLocation,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isCheckedIn) "Đã Chấm Công GPS" else "Chấm công GPS",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onToggleCall,
                    modifier = Modifier.weight(0.8f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Liên hệ", style = MaterialTheme.typography.labelMedium)
                }
            }

            AnimatedVisibility(visible = isCalling) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SuccessGreen.copy(alpha = 0.15f))
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Đang kết nối tổng đài nội bộ tới ${driver.name} (${driver.phone})…",
                        style = MaterialTheme.typography.bodySmall,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineCard(steps: List<TrackingStep>) {
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
                .padding(16.dp)
        ) {
            Text(
                text = "Nhật ký Quy trình Vận hành trong ngày",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            steps.forEachIndexed { index, step ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Left Stepper Indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(28.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        step.isDone -> SuccessGreen
                                        step.isCurrent -> BrandBlue
                                        else -> if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (step.isDone) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            } else if (step.isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(44.dp)
                                    .background(
                                        if (step.isDone) SuccessGreen.copy(alpha = 0.5f)
                                        else if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Step Content
                    Column(modifier = Modifier.padding(bottom = 12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (step.isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (step.isCurrent) BrandBlue else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = step.timestamp,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = step.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
