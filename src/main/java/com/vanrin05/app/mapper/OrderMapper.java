package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.OrderDto;
import com.vanrin05.app.model.orderpayment.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
