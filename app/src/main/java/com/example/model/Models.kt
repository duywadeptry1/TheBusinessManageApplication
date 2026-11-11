package com.example.model

enum class OrderStatus(val label: String) {
    DELIVERING("Đang triển khai"),
    PENDING("Chờ phê duyệt"),
    COMPLETED("Đã nghiệm thu"),
    CANCELLED("Tạm dừng / Quá hạn")
}

data class ShippingOrder(
    val id: String,
    val trackingNumber: String,      // Mã dự án / Hợp đồng (e.g. PRJ-2026-01)
    val senderName: String,          // Phòng ban phụ trách (e.g. Phòng Kinh Doanh)
    val senderAddress: String,       // Tên dự án / Hạng mục công việc
    val recipientName: String,       // Quản lý / Nhân sự thực hiện (Assignee)
    val recipientAddress: String,    // Chi nhánh & Mục tiêu bàn giao
    val status: OrderStatus,
    val serviceType: String,         // Loại quy trình (Dự án, Hợp đồng, Ngân sách, Nhân sự)
    val feeVnd: Long,                // Chi phí vận hành / Giải ngân thực tế (VND)
    val codVnd: Long,                // Tổng ngân sách / Giá trị hợp đồng (VND)
    val createdAt: String,           // Thời gian khởi tạo
    val estimatedDelivery: String,   // Hạn chót (Deadline) / Nghiệm thu
    val weightKg: Float              // Khối lượng công việc (Man-days) / Quy mô
)

data class StatCardData(
    val title: String,
    val count: String,
    val subtitle: String,
    val trend: String,
    val isPositive: Boolean
)

data class ShippingService(
    val id: String,
    val name: String,                // Tên Nhân sự (HR) hoặc Giải pháp / Sản phẩm
    val description: String,         // Chức vụ, KPI & Kỹ năng hoặc Thông số tồn kho
    val basePriceVnd: Long,          // Mức lương / Định mức hoặc Đơn giá (VND)
    val deliveryTime: String,        // Phòng ban / Trạng thái sẵn sàng
    val popularTag: String? = null   // Nhãn nổi bật (Xuất sắc, Chủ chốt, Bán chạy)
)

data class DailyRevenue(
    val dayLabel: String,
    val amountVnd: Long,
    val orderCount: Int,
    val isPeak: Boolean = false
)

data class PaymentMethodShare(
    val name: String,
    val percentage: Int,
    val totalVnd: Long
)

data class CourierDriver(
    val name: String,                // Giám đốc Chi nhánh / Quản lý ca trực
    val phone: String,
    val licensePlate: String,        // Mã văn phòng & Quân số điểm danh
    val vehicleModel: String,        // Tên Chi nhánh / Trụ sở
    val rating: Float,               // Điểm hiệu suất KPI chi nhánh
    val completedTrips: Int,         // Số dự án hoàn tất
    val currentDistanceKm: Float,    // Khoảng cách GPS tới văn phòng (km)
    val etaMinutes: Int              // Số nhân sự vừa chấm công trong 15 phút qua
)

data class TrackingStep(
    val title: String,
    val description: String,
    val timestamp: String,
    val isDone: Boolean,
    val isCurrent: Boolean
)

data class ScheduleDayItem(
    val dayOfWeek: String,
    val dayNumber: String,
    val meetingTitle: String,
    val department: String,
    val timeSlot: String,
    val roomOrLocation: String,
    val statusBadge: String
)
