package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.response.UserOrderHistoryResponse;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerOrderMapper {
    @Mapping(target = "customer", source = "order.user")
    @Mapping(target = "shippingAddress", source = "order.shippingAddress")
    @Mapping(target = "paymentMethod", source = "order.paymentMethod")
    UserOrderHistoryResponse toUserOrderHistoryResponse(SellerOrder sellerOrder);
}
