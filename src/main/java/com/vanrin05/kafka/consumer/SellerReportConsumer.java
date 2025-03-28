package com.vanrin05.kafka.consumer;

import com.event.SellerReportEvent;
import com.vanrin05.model.Order;
import com.vanrin05.model.Seller;
import com.vanrin05.model.SellerReport;
import com.vanrin05.service.SellerReportService;
import com.vanrin05.service.TransactionService;
import com.vanrin05.service.impl.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SellerReportConsumer {
    SellerService sellerService;
    SellerReportService sellerReportService;
    TransactionService transactionService;

    @KafkaListener(topics = "update-seller-report", groupId = "main")
    @Transactional
    public void handleSellerReportUpdate(SellerReportEvent event) {
            Map<Long, Seller> sellers = sellerService.getSellersByIds(
                            event.getOrders().stream().map(Order::getSellerId).collect(Collectors.toSet())).stream().collect(Collectors.toMap(Seller::getId, seller -> seller));

            Map<Seller, SellerReport> sellerReports = sellerReportService.findSellerReportBySellers(sellers.values().stream().toList()).stream().collect(Collectors.toMap(SellerReport::getSeller, sellerReport -> sellerReport));

            for (Order order : event.getOrders()) {
                transactionService.createTransaction(order);

                Seller seller = sellers.get(order.getSellerId());
                SellerReport sellerReport = sellerReports.get(seller);

                sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales() + order.getOrderItems().size());
            }

            sellerReportService.updateSellerReports(sellerReports.values().stream().toList());
    }
}
