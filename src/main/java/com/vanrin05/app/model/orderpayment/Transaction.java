package com.vanrin05.app.model.orderpayment;

import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User customer;

    @OneToOne
    Order order;

    @ManyToOne
    Seller seller;

    LocalDateTime date;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }

}
