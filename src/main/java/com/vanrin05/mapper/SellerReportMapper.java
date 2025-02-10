package com.vanrin05.mapper;

import com.vanrin05.dto.request.UpdateSellerReportRequest;
import com.vanrin05.model.SellerReport;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerReportMapper {
    void toUpdateSellerReport(@MappingTarget SellerReport sellerReport, UpdateSellerReportRequest updateSellerReportRequest);
}
