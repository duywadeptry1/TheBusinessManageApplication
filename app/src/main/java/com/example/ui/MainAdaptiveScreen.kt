package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.CreateOrderSheet
import com.example.ui.components.DatabaseImportSheet
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.LocalThemeIsDark
import com.example.viewmodel.GoShipUiState
import com.example.viewmodel.GoShipViewModel

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun MainAdaptiveScreen(
    viewModel: GoShipViewModel,
    uiState: GoShipUiState
) {
    val isDark = LocalThemeIsDark.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Return to Home tab when pressing Back on secondary tabs
    BackHandler(enabled = uiState.selectedTab != 0) {
        viewModel.selectTab(0)
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }

    val navItems = listOf(
        NavItem("Tổng quan", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "nav_home"),
        NavItem("Công việc", Icons.Filled.Assignment, Icons.Outlined.Assignment, "nav_orders"),
        NavItem("Chi nhánh", Icons.Filled.LocationOn, Icons.Outlined.LocationOn, "nav_tracking"),
        NavItem("Nhân sự", Icons.Filled.Groups, Icons.Outlined.Groups, "nav_services"),
        NavItem("Tài chính", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet, "nav_analytics"),
        NavItem("Hệ thống", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, "nav_profile")
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        if (isTablet) {
            // Tablet Layout with NavigationRail
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                NavigationRail(
                    containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White,
                    header = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BrandBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.BusinessCenter,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "BizManage",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlue
                            )
                        }
                    },
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    navItems.forEachIndexed { index, item ->
                        val selected = uiState.selectedTab == index
                        NavigationRailItem(
                            selected = selected,
                            onClick = { viewModel.selectTab(index) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = BrandBlue,
                                selectedTextColor = BrandBlue,
                                indicatorColor = BrandBlue.copy(alpha = if (isDark) 0.25f else 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Main Content Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    ScreenContent(
                        tabIndex = uiState.selectedTab,
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        } else {
            // Mobile Layout with Bottom NavigationBar
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    NavigationBar(
                        containerColor = if (isDark) MaterialTheme.colorScheme.surface else Color.White,
                        tonalElevation = 6.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        navItems.forEachIndexed { index, item ->
                            val selected = uiState.selectedTab == index
                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(index) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BrandBlue,
                                    selectedTextColor = BrandBlue,
                                    indicatorColor = BrandBlue.copy(alpha = if (isDark) 0.25f else 0.12f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag(item.tag)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ScreenContent(
                        tabIndex = uiState.selectedTab,
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }

        // Create Business Project / Task Sheet Modal
        CreateOrderSheet(
            isVisible = uiState.isCreateOrderSheetVisible,
            services = uiState.services,
            selectedService = uiState.selectedServiceForOrder,
            onServiceSelect = { viewModel.selectServiceForOrder(it) },
            onDismiss = { viewModel.closeCreateOrderSheet() },
            onConfirm = { name, address, weight, payment ->
                viewModel.createOrder(name, address, weight, payment)
            }
        )

        // Database Import & Management Sheet Modal
        DatabaseImportSheet(
            isVisible = uiState.isDatabaseImportSheetVisible,
            ordersCount = uiState.orders.size,
            servicesCount = uiState.services.size,
            revenuesCount = uiState.dailyRevenues.size,
            editorPayload = uiState.databaseEditorPayload,
            onPayloadChange = { viewModel.updateDatabaseEditorPayload(it) },
            onImportJson = { viewModel.importDataFromJson(it) },
            onImportCsv = { viewModel.importDataFromCsv(it) },
            onExportCurrentDb = { viewModel.loadCurrentDatabaseIntoEditor() },
            onResetDatabase = { viewModel.resetDatabaseToDefaults() },
            onDismiss = { viewModel.closeDatabaseImportSheet() }
        )
    }
}

@Composable
private fun ScreenContent(
    tabIndex: Int,
    viewModel: GoShipViewModel,
    uiState: GoShipUiState
) {
    Crossfade(targetState = tabIndex, label = "screen_transition") { targetTab ->
        when (targetTab) {
            0 -> HomeScreen(
                uiState = uiState,
                onSearchChange = { viewModel.updateSearchQuery(it) },
                onTabSelect = { viewModel.selectTab(it) },
                onSelectScheduleDay = { viewModel.selectScheduleDay(it) },
                onPerformCheckIn = { viewModel.performGpsCheckIn() },
                onCreateOrderClick = { viewModel.openCreateOrderSheet() },
                onOpenDbImportClick = { viewModel.openDatabaseImportSheet() },
                onTrackOrderClick = { viewModel.setTrackingOrder(it) },
                onToggleDarkMode = { viewModel.toggleDarkMode(it) }
            )
            1 -> OrdersScreen(
                uiState = uiState,
                onSearchChange = { viewModel.updateSearchQuery(it) },
                onFilterSelect = { viewModel.filterOrders(it) },
                onCreateOrderClick = { viewModel.openCreateOrderSheet() },
                onOpenDbImportClick = { viewModel.openDatabaseImportSheet() },
                onUpdateOrderStatus = { id, st -> viewModel.updateOrderStatus(id, st) },
                onDeleteOrder = { id -> viewModel.deleteOrder(id) },
                onTrackOrderClick = { viewModel.setTrackingOrder(it) },
                onJumpToTracking = { viewModel.selectTab(2) }
            )
            2 -> TrackingScreen(
                uiState = uiState,
                onPerformCheckIn = { viewModel.performGpsCheckIn() }
            )
            3 -> ServicesScreen(
                uiState = uiState,
                onSearchChange = { viewModel.updateSearchQuery(it) },
                onSelectService = { viewModel.openCreateOrderSheet(it) }
            )
            4 -> AnalyticsScreen(
                uiState = uiState,
                onOpenDbImportClick = { viewModel.openDatabaseImportSheet() }
            )
            5 -> ProfileScreen(
                uiState = uiState,
                onToggleDarkMode = { viewModel.toggleDarkMode(it) },
                onOpenDbImportClick = { viewModel.openDatabaseImportSheet() },
                onResetDbClick = { viewModel.resetDatabaseToDefaults() }
            )
        }
    }
}
