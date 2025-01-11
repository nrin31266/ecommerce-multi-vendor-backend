package com.vanrin05.model;

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

    @OneToOne
    Seller seller;

    Long totalEarnings;

    Long totalSales;

    Long totalRefunds;

    Long totalTax;

    Long netEarnings;
    Integer totalOrders;
    Integer canceledOrders;
    Integer totalTransactions;

    @PrePersist
    protected void onCreate() {
        this.totalEarnings = 0L;
        this.totalRefunds = 0L;
        this.totalTax = 0L;
        this.netEarnings = 0L;
        this.totalOrders = 0;
        this.canceledOrders = 0;
        this.totalTransactions = 0;
    }
}
