package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.OrderItemDto;
import com.vanrin05.app.model.orderpayment.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "shippingAddress", source = "order.shippingAddress")
    @Mapping(target = "paymentMethod", source = "order.paymentMethod")
    @Mapping(target = "orderId", source = "order.id")
    OrderItemDto orderItemToOrderItemDto(OrderItem orderItem);
}
