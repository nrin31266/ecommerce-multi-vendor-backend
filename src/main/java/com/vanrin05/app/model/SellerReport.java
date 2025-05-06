package com.vanrin05.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Người bán được thống kê
    @OneToOne
    Seller seller;

    // Tổng doanh thu trước hoàn tiền và thuế
    Long grossEarnings;      // VD: 1.000.000đ

    // Tổng giá trị hàng đã bán (tổng giá niêm yết x số lượng)
    //Tổng giá trị hàng theo giá gốc/niêm yết, chưa áp dụng mã giảm giá
    Long totalSales;         // VD: 1.200.000đ

    // Tổng số tiền bị hoàn lại cho khách
    Long totalRefunds;       // VD: 100.000đ

    // Tổng thuế và phí nền tảng đã bị trừ
    Long totalTax;           // VD: 50.000đ

    // Doanh thu thực nhận = gross - refund - tax
    Long netEarnings;        // VD: 850.000đ

    // Số đơn đã tạo (kể cả hủy)
    Integer totalOrders;     // VD: 150

    // Số đơn bị hủy
    Integer canceledOrders;  // VD: 10

    // Số đơn hoàn tất giao hàng và thanh toán
    Integer completedOrders; // VD: 135

    // Khởi tạo mặc định về 0
    @PrePersist
    protected void onCreate() {
        this.grossEarnings = 0L;
        this.totalSales = 0L;
        this.totalRefunds = 0L;
        this.totalTax = 0L;
        this.netEarnings = 0L;
        this.totalOrders = 0;
        this.canceledOrders = 0;
        this.completedOrders = 0;
    }
}
