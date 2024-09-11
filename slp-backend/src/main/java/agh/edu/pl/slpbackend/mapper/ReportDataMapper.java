package agh.edu.pl.slpbackend.mapper;

import agh.edu.pl.slpbackend.dto.ReportDataDto;
import agh.edu.pl.slpbackend.model.ReportData;

public interface ReportDataMapper {

    default ReportDataDto toDto(final ReportData model) {
        return ReportDataDto.builder()
                .id(model.getId())
                .manufacturerName(model.getManufacturerName())
                .manufacturerAddress(model.getManufacturerAddress())
                .manufacturerCountry(model.getManufacturerCountry())
                .supplierName(model.getSupplierName())
                .supplierAddress(model.getSupplierAddress())
                .sellerName(model.getSellerName())
                .sellerAddress(model.getSellerAddress())
                .recipientName(model.getRecipientName())
                .recipientAddress(model.getRecipientAddress())
                .productionDate(model.getProductionDate())
                .batchNumber(model.getBatchNumber())
                .batchSizeProd(model.getBatchSizeProd())
                .batchSizeStorehouse(model.getBatchSizeStorehouse())
                .samplePacking(model.getSamplePacking())
                .sampleCollectionSite(model.getSampleCollectionSite())
                .sampleCollector(model.getSampleCollector())
                .jobNumber(model.getJobNumber())
                .mechanism(model.getMechanism())
                .deliveryMethod(model.getDeliveryMethod())
                .build();
    }

    default ReportData toModel(final ReportDataDto dto) {
        return ReportData.builder()
                .id(dto.getId())
                .manufacturerName(dto.getManufacturerName())
                .manufacturerAddress(dto.getManufacturerAddress())
                .manufacturerCountry(dto.getManufacturerCountry())
                .supplierName(dto.getSupplierName())
                .supplierAddress(dto.getSupplierAddress())
                .sellerName(dto.getSellerName())
                .sellerAddress(dto.getSellerAddress())
                .recipientName(dto.getRecipientName())
                .recipientAddress(dto.getRecipientAddress())
                .productionDate(dto.getProductionDate())
                .batchNumber(dto.getBatchNumber())
                .batchSizeProd(dto.getBatchSizeProd())
                .batchSizeStorehouse(dto.getBatchSizeStorehouse())
                .samplePacking(dto.getSamplePacking())
                .sampleCollectionSite(dto.getSampleCollectionSite())
                .sampleCollector(dto.getSampleCollector())
                .jobNumber(dto.getJobNumber())
                .mechanism(dto.getMechanism())
                .deliveryMethod(dto.getDeliveryMethod())
                .build();
    }

}
