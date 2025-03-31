package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.UpdateSellerReportRequest;
import com.vanrin05.app.model.SellerReport;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerReportMapper {
    void toUpdateSellerReport(@MappingTarget SellerReport sellerReport, UpdateSellerReportRequest updateSellerReportRequest);
}
