package agh.edu.pl.slpbackend.config;

import agh.edu.pl.slpbackend.model.*;
import agh.edu.pl.slpbackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(
            final AddressRepository addressRepository,
            final ReportDataRepository raportDataRepository,
            final SampleRepository sampleRepository,
            final CodeRepository codeRepository,
            final ClientRepository clientRepository,
            final InspectionRepository inspectionRepository,
            final SamplingStandardRepository samplingStandardRepository,
            final IndicationRepository indicationRepository,
            final ProductGroupRepository groupRepository) {
        return args -> {
            if (sampleRepository.count() == 0) {
                Code code = Code.builder()
                        .id("K")
                        .name("Kontrolne")
                        .build();
                codeRepository.save(code);

                Client client = Client.builder()
                        .name("WIJHARS Kielce")
                        .wijharsCode("ki")
                        .address(Address.builder()
                                .street("Aleja IX Wieków Kielc 3")
                                .zipCode("25-516")
                                .city("Kielce")
                                .build())
                        .build();
                clientRepository.save(client);

                Inspection inspection = Inspection.builder()
                        .name("Kontrola planowa")
                        .build();
                inspectionRepository.save(inspection);

                SamplingStandard samplingStandard = SamplingStandard.builder()
                        .name("PN-72/A-74001")
                        .build();
                samplingStandardRepository.save(samplingStandard);

                Indication indication = Indication.builder()
                        .name("Wilgotność")
                        .norm("PN-EN ISO 712:2012")
                        .unit("g/100g")
                        .laboratory("FCh")
                        .build();
                indicationRepository.save(indication);

                ProductGroup group = ProductGroup.builder()
                        .name("Przetwory zbożowe")
                        .samplingStandards(List.of(samplingStandard))
                        .indications(List.of(indication))
                        .build();
                groupRepository.save(group);

                Sample sample = Sample.builder()
                        .code(code)
                        .client(client)
                        .assortment("Kasza")
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusYears(3))
                        .expirationComment("Po otwarciu spożyć w ciągu 5 dni")
                        .examinationEndDate(LocalDate.now().plusMonths(3))
                        .size("500g")
                        .state("Bez zastrzeżeń")
                        .analysis(true)
                        .inspection(inspection)
                        .group(group)
                        .samplingStandard(samplingStandard)
                        .reportData(null)
                        .build();
                sampleRepository.save(sample);
            }
            if (raportDataRepository.count() == 0) {
                Address address = Address.builder()
                        .street("Sezam Street")
                        .zipCode("39-120")
                        .city("Dziuralala")
                        .build();
                addressRepository.save(address);

                ReportData reportData = ReportData.builder()
                        .manufacturerName("manufacture")
                        .manufacturerAddress(address)
                        .supplierName("supplier")
                        .supplierAddress(address)
                        .sellerName("seller")
                        .sellerAddress(address)
                        .recipientName("recipient")
                        .recipientAddress(address)
                        .jobNumber(11)
                        .mechanism("mechanism")
                        .deliveryMethod("deliveryMethod")
                        .build();

                raportDataRepository.save(reportData);

            }

        };
    }
}
