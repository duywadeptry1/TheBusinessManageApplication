package com.example.data.local

import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.PaymentShareEntity
import com.example.data.local.entity.RevenueEntity
import com.example.data.local.entity.ServiceEntity
import com.example.model.CourierDriver
import com.example.model.OrderStatus
import com.example.model.TrackingStep

/**
 * Clean centralized seed data and sample import batches.
 * Developers can easily update initial database records here or import JSON/CSV at runtime.
 */
object SeedData {

    fun defaultOrders(): List<OrderEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            OrderEntity(
                id = "ORD-001",
                trackingNumber = "VN-88492-HN",
                senderName = "Alex Nguyễn",
                senderAddress = "120 Hoàng Hoa Thám, Ba Đình, Hà Nội",
                recipientName = "Phạm Thị Thu Thảo",
                recipientAddress = "Số 45 Lê Lợi, Quận 1, TP. Hồ Chí Minh",
                statusName = OrderStatus.DELIVERING.name,
                serviceType = "Hỏa tốc 2H",
                feeVnd = 65000L,
                codVnd = 1250000L,
                createdAt = "10:30 • Hôm nay",
                estimatedDelivery = "15:30 • Hôm nay",
                weightKg = 1.5f,
                timestampMillis = now - 1000L
            ),
            OrderEntity(
                id = "ORD-002",
                trackingNumber = "VN-88491-DN",
                senderName = "Alex Nguyễn",
                senderAddress = "120 Hoàng Hoa Thám, Ba Đình, Hà Nội",
                recipientName = "Nguyễn Minh Khang",
                recipientAddress = "28 Bạch Đằng, Hải Châu, Đà Nẵng",
                statusName = OrderStatus.COMPLETED.name,
                serviceType = "Tiết kiệm liên tỉnh",
                feeVnd = 32000L,
                codVnd = 890000L,
                createdAt = "08:15 • 09/11/2026",
                estimatedDelivery = "Đã nhận lúc 16:20",
                weightKg = 0.8f,
                timestampMillis = now - 2000L
            ),
            OrderEntity(
                id = "ORD-003",
                trackingNumber = "VN-88490-HP",
                senderName = "Kho Tổng GoShip",
                senderAddress = "Cụm Công Nghiệp Lai Xá, Hoài Đức",
                recipientName = "Trần Đình Trọng",
                recipientAddress = "15 Lạch Tray, Ngô Quyền, Hải Phòng",
                statusName = OrderStatus.PENDING.name,
                serviceType = "Hàng cồng kềnh",
                feeVnd = 180000L,
                codVnd = 3450000L,
                createdAt = "07:00 • Hôm nay",
                estimatedDelivery = "Dự kiến 18:00",
                weightKg = 12.0f,
                timestampMillis = now - 3000L
            ),
            OrderEntity(
                id = "ORD-004",
                trackingNumber = "VN-88489-SG",
                senderName = "Alex Nguyễn",
                senderAddress = "120 Hoàng Hoa Thám, Ba Đình, Hà Nội",
                recipientName = "Hoàng Kim Oanh",
                recipientAddress = "88 Nguyễn Thị Minh Khai, Quận 3, TP.HCM",
                statusName = OrderStatus.COMPLETED.name,
                serviceType = "Hỏa tốc 2H",
                feeVnd = 55000L,
                codVnd = 640000L,
                createdAt = "14:20 • 08/11/2026",
                estimatedDelivery = "Đã nhận lúc 17:00",
                weightKg = 2.2f,
                timestampMillis = now - 4000L
            ),
            OrderEntity(
                id = "ORD-005",
                trackingNumber = "VN-88488-CT",
                senderName = "Alex Nguyễn",
                senderAddress = "120 Hoàng Hoa Thám, Ba Đình, Hà Nội",
                recipientName = "Lê Hoàng Long",
                recipientAddress = "12 Đại Lộ Hòa Bình, Ninh Kiều, Cần Thơ",
                statusName = OrderStatus.CANCELLED.name,
                serviceType = "Tiết kiệm liên tỉnh",
                feeVnd = 35000L,
                codVnd = 0L,
                createdAt = "09:00 • 07/11/2026",
                estimatedDelivery = "Khách hủy do đổi lịch",
                weightKg = 0.5f,
                timestampMillis = now - 5000L
            )
        )
    }

    fun defaultServices(): List<ServiceEntity> = listOf(
        ServiceEntity(
            id = "SRV-01",
            name = "Hỏa tốc Nội thành 2H",
            description = "Giao nhận siêu tốc bằng xe máy trong 2 giờ, cam kết đúng giờ và bảo hiểm 100% hàng hóa giá trị.",
            basePriceVnd = 25000L,
            deliveryTime = "Tối đa 2 giờ",
            popularTag = "Phổ biến nhất"
        ),
        ServiceEntity(
            id = "SRV-02",
            name = "Tiết kiệm Liên tỉnh",
            description = "Mạng lưới kết nối 63 tỉnh thành, tối ưu chi phí cho các shop online và hàng hóa cồng kềnh vừa phải.",
            basePriceVnd = 18000L,
            deliveryTime = "1 - 3 ngày",
            popularTag = "Tiết kiệm cước"
        ),
        ServiceEntity(
            id = "SRV-03",
            name = "Xe tải & Hàng cồng kềnh",
            description = "Đội ngũ xe tải từ 500kg đến 5 tấn, hỗ trợ tài xế bốc xếp tận nơi, đóng pallet chuyên nghiệp.",
            basePriceVnd = 250000L,
            deliveryTime = "Trong ngày",
            popularTag = "Bốc xếp trọn gói"
        ),
        ServiceEntity(
            id = "SRV-04",
            name = "Vận chuyển Quốc tế Express",
            description = "Hợp tác mạng lưới hàng không toàn cầu, hải quan thông quan nhanh chóng đến hơn 220 quốc gia.",
            basePriceVnd = 380000L,
            deliveryTime = "3 - 5 ngày",
            popularTag = "Toàn cầu"
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
        PaymentShareEntity("Thu hộ khi nhận hàng (COD)", 45, 15590000L, 0),
        PaymentShareEntity("Ví GoShip & Ví điện tử", 35, 12120000L, 1),
        PaymentShareEntity("Chuyển khoản VietQR 24/7", 20, 6940000L, 2)
    )

    fun defaultDriver(): CourierDriver = CourierDriver(
        name = "Trần Văn Mạnh",
        phone = "0982 123 456",
        licensePlate = "29E1-884.23",
        vehicleModel = "Honda AirBlade 160",
        rating = 4.9f,
        completedTrips = 1420,
        currentDistanceKm = 1.8f,
        etaMinutes = 18
    )

    fun defaultTrackingSteps(): List<TrackingStep> = listOf(
        TrackingStep(
            title = "Đã tiếp nhận đơn hàng",
            description = "Người gửi đã khởi tạo đơn hàng thành công qua GoShip app.",
            timestamp = "08:15",
            isDone = true,
            isCurrent = false
        ),
        TrackingStep(
            title = "Tài xế đã lấy kiện hàng",
            description = "Tài xế Trần Văn Mạnh đã kiểm tra niêm phong kiện hàng tại địa chỉ gửi.",
            timestamp = "09:30",
            isDone = true,
            isCurrent = false
        ),
        TrackingStep(
            title = "Đến bưu cục phân loại Ba Đình",
            description = "Kiện hàng được quét mã vạch và phân luồng tuyến đường nhanh nhất.",
            timestamp = "10:15",
            isDone = true,
            isCurrent = false
        ),
        TrackingStep(
            title = "Đang giao tới người nhận",
            description = "Tài xế đang di chuyển cách bạn 1.8 km. Vui lòng giữ máy.",
            timestamp = "11:00",
            isDone = false,
            isCurrent = true
        ),
        TrackingStep(
            title = "Giao hàng hoàn tất",
            description = "Ký nhận và hoàn tất đơn hàng.",
            timestamp = "Dự kiến 11:25",
            isDone = false,
            isCurrent = false
        )
    )

    /**
     * Sample JSON batch for 1-click or custom Database Import in the UI.
     */
    const val SAMPLE_IMPORT_JSON = """{
  "orders": [
    {
      "id": "ORD-IMP-101",
      "trackingNumber": "VN-99201-HN",
      "senderName": "Kho Điện Máy Xanh",
      "senderAddress": "85 Nguyễn Chí Thanh, Đống Đa, Hà Nội",
      "recipientName": "Đặng Quốc Việt",
      "recipientAddress": "Khu đô thị Times City, Hai Bà Trưng, Hà Nội",
      "status": "DELIVERING",
      "serviceType": "Hỏa tốc 2H",
      "feeVnd": 48000,
      "codVnd": 4500000,
      "createdAt": "Vừa nhập DB",
      "estimatedDelivery": "Trong 90 phút",
      "weightKg": 3.4
    },
    {
      "id": "ORD-IMP-102",
      "trackingNumber": "VN-99202-SG",
      "senderName": "Shop Thời Trang Vera",
      "senderAddress": "210 Cầu Giấy, Hà Nội",
      "recipientName": "Vũ Ngọc Ánh",
      "recipientAddress": "102 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh",
      "status": "PENDING",
      "serviceType": "Tiết kiệm liên tỉnh",
      "feeVnd": 29000,
      "codVnd": 780000,
      "createdAt": "Vừa nhập DB",
      "estimatedDelivery": "Dự kiến 2 ngày",
      "weightKg": 0.9
    },
    {
      "id": "ORD-IMP-103",
      "trackingNumber": "VN-99203-BD",
      "senderName": "Nhà Phân Phối Minh Phát",
      "senderAddress": "KCN Sóng Thần, Dĩ An, Bình Dương",
      "recipientName": "Công ty TNHH Hoàng Gia",
      "recipientAddress": "350 Võ Văn Kiệt, Quận 1, TP.HCM",
      "status": "COMPLETED",
      "serviceType": "Xe tải & Hàng cồng kềnh",
      "feeVnd": 320000,
      "codVnd": 12500000,
      "createdAt": "Vừa nhập DB",
      "estimatedDelivery": "Đã giao thành công",
      "weightKg": 45.0
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
VN-77501-HN,Alex Nguyễn,120 Hoàng Hoa Thám Hà Nội,Lê Văn Tùng,54 Liễu Giai Ba Đình Hà Nội,DELIVERING,Hỏa tốc 2H,38000,950000,1.2
VN-77502-DN,Kho Công Nghệ Số,18 Thái Hà Đống Đa Hà Nội,Bùi Bích Phương,90 Nguyễn Văn Linh Đà Nẵng,PENDING,Tiết kiệm liên tỉnh,42000,2150000,2.0
VN-77503-HP,Alex Nguyễn,120 Hoàng Hoa Thám Hà Nội,Ngô Thanh Vân,22 Điện Biên Phủ Hải Phòng,COMPLETED,Tiết kiệm liên tỉnh,35000,620000,0.7"""
}
