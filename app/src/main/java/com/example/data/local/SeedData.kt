package com.example.data.local

import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.PaymentShareEntity
import com.example.data.local.entity.RevenueEntity
import com.example.data.local.entity.ServiceEntity
import com.example.model.CourierDriver
import com.example.model.OrderStatus
import com.example.model.ScheduleDayItem
import com.example.model.TrackingStep

/**
 * Centralized Business Management seed data and sample import batches for Room SQLite.
 */
object SeedData {

    fun defaultOrders(): List<OrderEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            OrderEntity(
                id = "ORD-001",
                trackingNumber = "PRJ-88492-ERP",
                senderName = "Phòng Kỹ Thuật & R&D",
                senderAddress = "Dự án: Triển khai Hệ thống Quản trị ERP Cloud Q4",
                recipientName = "Phạm Thị Thu Thảo (Project Lead)",
                recipientAddress = "Trụ sở Hà Nội • Tiến độ 85% • Ưu tiên Cao",
                statusName = OrderStatus.DELIVERING.name,
                serviceType = "Dự án Công nghệ",
                feeVnd = 4500000L,
                codVnd = 28500000L,
                createdAt = "08:30 • Hôm nay",
                estimatedDelivery = "Hạn: 25/11/2026",
                weightKg = 15.0f,
                timestampMillis = now - 1000L
            ),
            OrderEntity(
                id = "ORD-002",
                trackingNumber = "CTR-88491-B2B",
                senderName = "Phòng Kinh Doanh B2B",
                senderAddress = "Hợp đồng: Cung ứng Phần mềm Quản lý Chuỗi Bán lẻ",
                recipientName = "Nguyễn Minh Khang (Sales Manager)",
                recipientAddress = "Chi nhánh Đà Nẵng • Đã bàn giao nghiệm thu",
                statusName = OrderStatus.COMPLETED.name,
                serviceType = "Hợp đồng B2B",
                feeVnd = 2100000L,
                codVnd = 42000000L,
                createdAt = "09:15 • 09/11/2026",
                estimatedDelivery = "Đã nghiệm thu 16:20",
                weightKg = 10.0f,
                timestampMillis = now - 2000L
            ),
            OrderEntity(
                id = "ORD-003",
                trackingNumber = "FIN-88490-AUD",
                senderName = "Phòng Tài Chính - Kế Toán",
                senderAddress = "Quy trình: Phê duyệt Ngân sách Vận hành & Lương T11",
                recipientName = "Trần Đình Trọng (CFO)",
                recipientAddress = "Trụ sở Chính • Chờ Ban Giám Đốc ký duyệt",
                statusName = OrderStatus.PENDING.name,
                serviceType = "Ngân sách & Lương",
                feeVnd = 1800000L,
                codVnd = 65000000L,
                createdAt = "07:30 • Hôm nay",
                estimatedDelivery = "Hạn duyệt: 18:00",
                weightKg = 5.0f,
                timestampMillis = now - 3000L
            ),
            OrderEntity(
                id = "ORD-004",
                trackingNumber = "MKT-88489-CMP",
                senderName = "Phòng Marketing & Truyền Thông",
                senderAddress = "Chiến dịch: Ra mắt Bộ Giải pháp Doanh nghiệp Số 2026",
                recipientName = "Hoàng Kim Oanh (CMO)",
                recipientAddress = "Chi nhánh TP.HCM • Đạt 120% KPI khách hàng",
                statusName = OrderStatus.COMPLETED.name,
                serviceType = "Chiến dịch MKT",
                feeVnd = 3200000L,
                codVnd = 19500000L,
                createdAt = "14:20 • 08/11/2026",
                estimatedDelivery = "Hoàn thành vượt KPI",
                weightKg = 8.5f,
                timestampMillis = now - 4000L
            ),
            OrderEntity(
                id = "ORD-005",
                trackingNumber = "HR-88488-REC",
                senderName = "Phòng Hành Chính - Nhân Sự",
                senderAddress = "Đề xuất: Tuyển dụng & Đào tạo 15 Kỹ sư Phần mềm",
                recipientName = "Lê Hoàng Long (HR Director)",
                recipientAddress = "Chi nhánh Cần Thơ • Tạm dừng chờ điều chỉnh định biên",
                statusName = OrderStatus.CANCELLED.name,
                serviceType = "Nhân sự & Đào tạo",
                feeVnd = 950000L,
                codVnd = 12000000L,
                createdAt = "09:00 • 07/11/2026",
                estimatedDelivery = "Tạm dừng xét duyệt",
                weightKg = 6.0f,
                timestampMillis = now - 5000L
            )
        )
    }

    fun defaultServices(): List<ServiceEntity> = listOf(
        ServiceEntity(
            id = "SRV-01",
            name = "Phạm Thị Thu Thảo — Trưởng phòng Dự án",
            description = "Chuyên gia Quản trị Dự án ERP & Chuyển đổi số. Phụ trách 12 dự án trọng điểm, tỷ lệ đúng hạn 98.5%.",
            basePriceVnd = 32000000L,
            deliveryTime = "Phòng Kỹ Thuật • KPI 98%",
            popularTag = "Nhân sự Xuất sắc"
        ),
        ServiceEntity(
            id = "SRV-02",
            name = "Nguyễn Minh Khang — Giám đốc Kinh doanh B2B",
            description = "Quản lý mạng lưới 85 đối tác doanh nghiệp lớn toàn quốc, vượt 135% chỉ tiêu doanh số Quý 4.",
            basePriceVnd = 38000000L,
            deliveryTime = "Phòng Kinh Doanh • KPI 135%",
            popularTag = "Top Doanh Số"
        ),
        ServiceEntity(
            id = "SRV-03",
            name = "Gói Giải pháp ERP Doanh nghiệp Toàn diện",
            description = "Hệ thống quản lý đồng bộ Tài chính, Kho vận, Nhân sự (HRM) và CRM cho doanh nghiệp vừa và lớn.",
            basePriceVnd = 45000000L,
            deliveryTime = "Sẵn sàng triển khai",
            popularTag = "Sản phẩm Chủ lực"
        ),
        ServiceEntity(
            id = "SRV-04",
            name = "Hệ thống POS & Quản trị Chuỗi Chi nhánh",
            description = "Giải pháp phần cứng kết hợp Cloud AI đồng bộ tồn kho, hóa đơn điện tử và báo cáo lãi lỗ theo thời gian thực.",
            basePriceVnd = 18500000L,
            deliveryTime = "Tồn kho: 145 bộ",
            popularTag = "Bán chạy nhất"
        )
    )

    fun defaultRevenues(): List<RevenueEntity> = listOf(
        RevenueEntity(0, "T2", 3450000L, 18, false),
        RevenueEntity(1, "T3", 4200000L, 22, false),
        RevenueEntity(2, "T4", 5100000L, 26, false),
        RevenueEntity(3, "T5", 4800000L, 24, false),
        RevenueEntity(4, "T6", 7650000L, 38, true),
        RevenueEntity(5, "T7", 5900000L, 30, false),
        RevenueEntity(6, "CN", 3550000L, 16, false)
    )

    fun defaultPaymentShares(): List<PaymentShareEntity> = listOf(
        PaymentShareEntity("Doanh thu Hợp đồng & Dịch vụ B2B", 45, 15590000L, 0),
        PaymentShareEntity("Quỹ Vận hành & Lương thưởng Nhân sự", 35, 12120000L, 1),
        PaymentShareEntity("Đầu tư R&D & Hạ tầng Công nghệ", 20, 6940000L, 2)
    )

    fun defaultDriver(): CourierDriver = CourierDriver(
        name = "Trần Văn Mạnh — Giám đốc Vận hành",
        phone = "0982 123 456",
        licensePlate = "HQ-HANOI-01 • 124/130 Nhân sự có mặt",
        vehicleModel = "Trụ sở Chính Ba Đình, Hà Nội",
        rating = 4.9f,
        completedTrips = 148,
        currentDistanceKm = 0.1f,
        etaMinutes = 12
    )

    fun defaultTrackingSteps(): List<TrackingStep> = listOf(
        TrackingStep(
            title = "Mở ca vận hành & Đồng bộ hệ thống",
            description = "Hệ thống chấm công sinh trắc học & GPS tại 3 chi nhánh (Hà Nội, Đà Nẵng, TP.HCM) đã kích hoạt.",
            timestamp = "08:00",
            isDone = true,
            isCurrent = false
        ),
        TrackingStep(
            title = "Họp giao ban Trưởng bộ phận",
            description = "Phê duyệt chỉ tiêu KPI ngày và phân bổ nguồn lực cho 5 phòng ban chuyên môn.",
            timestamp = "08:45",
            isDone = true,
            isCurrent = false
        ),
        TrackingStep(
            title = "Kiểm soát tiến độ Dự án & Hợp đồng",
            description = "Phòng Kỹ thuật & Kinh doanh cập nhật dữ liệu nghiệm thu lên Room SQLite Database.",
            timestamp = "10:15",
            isDone = true,
            isCurrent = false
        ),
        TrackingStep(
            title = "Giám sát vận hành & Phê duyệt ngân sách",
            description = "Ban Giám đốc đang xét duyệt 3 đề xuất giải ngân và theo dõi chấm công ca chiều.",
            timestamp = "14:00",
            isDone = false,
            isCurrent = true
        ),
        TrackingStep(
            title = "Tổng kết doanh thu & Đóng sổ cuối ngày",
            description = "Tự động đối soát dòng tiền, xuất báo cáo tài chính và sao lưu Database.",
            timestamp = "Dự kiến 17:30",
            isDone = false,
            isCurrent = false
        )
    )

    fun defaultWeeklySchedule(): List<ScheduleDayItem> = listOf(
        ScheduleDayItem("T2", "06", "Họp Giao ban Đầu tuần & Rà soát KPI", "Ban Giám Đốc & Trưởng phòng", "08:30 - 10:00", "Phòng họp VIP A1", "Đã hoàn tất"),
        ScheduleDayItem("T3", "07", "Đào tạo Quy trình ERP & Database", "Phòng Kỹ Thuật & Nhân Sự", "09:30 - 11:30", "Hội trường Tầng 4", "Đã hoàn tất"),
        ScheduleDayItem("T4", "08", "Ký kết Hợp đồng Chuỗi Bán lẻ B2B", "Phòng Kinh Doanh B2B", "14:00 - 16:00", "Chi nhánh TP.HCM", "Đã hoàn tất"),
        ScheduleDayItem("T5", "09", "Kiểm toán Chi phí & Đối soát Quỹ", "Phòng Tài Chính - Kế Toán", "10:00 - 11:30", "Phòng họp B2", "Đã hoàn tất"),
        ScheduleDayItem("T6", "10", "Hội nghị Đánh giá Dự án Trọng điểm Q4", "Toàn bộ Khối Vận Hành", "14:00 - 16:30", "Trụ sở Chính Hà Nội", "Đang diễn ra"),
        ScheduleDayItem("T7", "11", "Tổng kết Chấm công & Thưởng Tuần", "Phòng Hành Chính - Nhân Sự", "09:00 - 11:00", "Trực tuyến / Hybrid", "Sắp tới"),
        ScheduleDayItem("CN", "12", "Bảo trì Máy chủ & Sao lưu SQLite DB", "Đội ngũ IT SysAdmin", "22:00 - 23:30", "Data Center Cloud", "Lịch cố định")
    )

    /**
     * Sample JSON batch for 1-click or custom Database Import in the UI.
     */
    const val SAMPLE_IMPORT_JSON = """{
  "orders": [
    {
      "id": "ORD-IMP-101",
      "trackingNumber": "VN-99201-HN",
      "senderName": "Phòng Kinh Doanh Quốc Tế",
      "senderAddress": "Dự án: Ký kết Hợp đồng Cung ứng Thiết bị POS",
      "recipientName": "Đặng Quốc Việt (Account Lead)",
      "recipientAddress": "Trụ sở Hà Nội • Ưu tiên Cao • Tiến độ 70%",
      "status": "DELIVERING",
      "serviceType": "Hợp đồng B2B",
      "feeVnd": 3500000,
      "codVnd": 45000000,
      "createdAt": "Vừa nhập DB",
      "estimatedDelivery": "Hạn: 28/11/2026",
      "weightKg": 12.0
    },
    {
      "id": "ORD-IMP-102",
      "trackingNumber": "PRJ-99202-SG",
      "senderName": "Phòng Công Nghệ & AI",
      "senderAddress": "Dự án: Nâng cấp Cơ sở dữ liệu Doanh nghiệp",
      "recipientName": "Vũ Ngọc Ánh (Tech Lead)",
      "recipientAddress": "Chi nhánh TP.HCM • Chờ phê duyệt ngân sách",
      "status": "PENDING",
      "serviceType": "Dự án Công nghệ",
      "feeVnd": 2900000,
      "codVnd": 32000000,
      "createdAt": "Vừa nhập DB",
      "estimatedDelivery": "Hạn: 30/11/2026",
      "weightKg": 18.5
    },
    {
      "id": "ORD-IMP-103",
      "trackingNumber": "FIN-99203-BD",
      "senderName": "Phòng Tài Chính - Kế Toán",
      "senderAddress": "Quy trình: Quyết toán Thuế & Thưởng Quý 4",
      "recipientName": "Công ty Kiểm toán Minh Phát",
      "recipientAddress": "Chi nhánh Bình Dương • Đã hoàn tất nghiệm thu",
      "status": "COMPLETED",
      "serviceType": "Tài chính & Kiểm toán",
      "feeVnd": 1500000,
      "codVnd": 25000000,
      "createdAt": "Vừa nhập DB",
      "estimatedDelivery": "Đã nghiệm thu",
      "weightKg": 6.0
    }
  ],
  "revenues": [
    { "dayIndex": 0, "dayLabel": "T2", "amountVnd": 4100000, "orderCount": 21, "isPeak": false },
    { "dayIndex": 1, "dayLabel": "T3", "amountVnd": 5300000, "orderCount": 27, "isPeak": false },
    { "dayIndex": 2, "dayLabel": "T4", "amountVnd": 6200000, "orderCount": 31, "isPeak": false },
    { "dayIndex": 3, "dayLabel": "T5", "amountVnd": 5850000, "orderCount": 29, "isPeak": false },
    { "dayIndex": 4, "dayLabel": "T6", "amountVnd": 9400000, "orderCount": 48, "isPeak": true },
    { "dayIndex": 5, "dayLabel": "T7", "amountVnd": 7200000, "orderCount": 36, "isPeak": false },
    { "dayIndex": 6, "dayLabel": "CN", "amountVnd": 4800000, "orderCount": 24, "isPeak": false }
  ]
}"""

    const val SAMPLE_IMPORT_CSV = """trackingNumber,senderName,senderAddress,recipientName,recipientAddress,status,serviceType,feeVnd,codVnd,weightKg
PRJ-77501-HN,Phòng Kinh Doanh,Triển khai Đại lý Khu vực Miền Bắc,Lê Văn Tùng,Trụ sở Ba Đình Hà Nội,DELIVERING,Hợp đồng B2B,2800000,35000000,10.0
PRJ-77502-DN,Phòng Kỹ Thuật,Bảo trì Hạ tầng Cloud Chi nhánh Miền Trung,Bùi Bích Phương,Chi nhánh Hải Châu Đà Nẵng,PENDING,Dự án Công nghệ,1900000,21500000,8.0
PRJ-77503-HP,Phòng Nhân Sự,Đào tạo Hội nhập Nhân sự Mới T11,Ngô Thanh Vân,Chi nhánh Ngô Quyền Hải Phòng,COMPLETED,Nhân sự & Đào tạo,950000,12000000,4.5"""
}
