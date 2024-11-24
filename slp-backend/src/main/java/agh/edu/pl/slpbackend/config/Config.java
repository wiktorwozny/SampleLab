package agh.edu.pl.slpbackend.config;

import agh.edu.pl.slpbackend.enums.ProgressStatusEnum;
import agh.edu.pl.slpbackend.enums.RoleEnum;
import agh.edu.pl.slpbackend.model.*;
import agh.edu.pl.slpbackend.repository.*;
import agh.edu.pl.slpbackend.service.MethodService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class Config {
    @Bean
    CommandLineRunner commandLineRunner(
            final SampleRepository sampleRepository,
            final CodeRepository codeRepository,
            final ClientRepository clientRepository,
            final InspectionRepository inspectionRepository,
            final SamplingStandardRepository samplingStandardRepository,
            final ProductGroupRepository groupRepository,
            final ExaminationRepository examinationRepository,
            final UserRepository userRepository,
            final AssortmentRepository assortmentRepository,
            final MethodService methodService) {
        return args -> {
            if (groupRepository.count() == 0) {
                try (InputStream methodStream = getClass().getResourceAsStream("/metody_v2.xlsm")){
                    methodService.importMethods(methodStream);
                }
            }

            if (sampleRepository.count() == 0) {
                //code
                Code code1 = Code.builder()
                        .id("Kp")
                        .name("Kontrolne")
                        .build();

                Code code2 = Code.builder()
                        .id("Kd")
                        .name("Kontrolne")
                        .build();

                Code code3 = Code.builder()
                        .id("O")
                        .name("Ocena Jakości Handlowej")
                        .build();

                Code code4 = Code.builder()
                        .id("KW")
                        .name("Kontrola wewnętrzna")
                        .build();

                Code code5 = Code.builder()
                        .id("IH")
                        .name("Inspekcja Handlowa")
                        .build();

                codeRepository.saveAll(List.of(code1, code2, code3, code4, code5));

                //client
                Client client1 = Client.builder()
                        .name("WIJHARS Kielce")
                        .wijharsCode("ki")
                        .address(Address.builder()
                                .street("Aleja IX Wieków Kielc 3")
                                .zipCode("25-516")
                                .city("Kielce")
                                .build())
                        .build();

                Client client2 = Client.builder()
                        .name("WIJHARS Kraków")
                        .wijharsCode("kr")
                        .address(Address.builder()
                                .street("Ujastek 7")
                                .zipCode("31-752")
                                .city("Kraków")
                                .build())
                        .build();

                clientRepository.saveAll(List.of(client1, client2));

                //inspection
                Inspection inspection1 = Inspection.builder()
                        .name("Kontrola planowa")
                        .build();

                Inspection inspection2 = Inspection.builder()
                        .name("Kontrola dorażna GI")
                        .build();

                Inspection inspection3 = Inspection.builder()
                        .name("Kontrola doraźna WIJHARS")
                        .build();

                Inspection inspection4 = Inspection.builder()
                        .name("W ramach skargi")
                        .build();
                Inspection inspection5 = Inspection.builder()
                        .name("RASFF")
                        .build();
                Inspection inspection6 = Inspection.builder()
                        .name("Kontrola graniczna")
                        .build();

                inspectionRepository.saveAll(List.of(inspection1, inspection2, inspection3, inspection4, inspection5, inspection6));

                //sampling standard
                SamplingStandard samplingStandard1 = SamplingStandard.builder()
                        .name("PN-72/A-74001")
                        .build();

                SamplingStandard samplingStandard2 = SamplingStandard.builder()
                        .name("PN-EN ISO 542")
                        .build();

                SamplingStandard samplingStandard3 = SamplingStandard.builder()
                        .name("PN-EN ISO 24333:2012")
                        .build();
                samplingStandardRepository.saveAll(List.of(samplingStandard1, samplingStandard2, samplingStandard3));

                //group
                ProductGroup group1 = ProductGroup.builder()
                        .name("Przetwory zbożowe")
                        .samplingStandards(List.of(samplingStandard1, samplingStandard2, samplingStandard3))
                        .build();

                ProductGroup group2 = ProductGroup.builder()
                        .name("Świeże owoce i warzywa")
                        .samplingStandards(List.of(samplingStandard1, samplingStandard2))
                        .build();

                groupRepository.saveAll(List.of(group1, group2));

                //assortment
                var indication11 = Indication.builder()
                        .name("Wilgotność")
                        .method("PN-EN ISO 712:2012")
                        .unit("g/100g")
                        .laboratory("FCh")
                        .build();

                var indication12 = Indication.builder()
                        .name("Ocena organoleptyczna - mąka i kasza")
                        .method("PN-A-74013:1964")
                        .unit(null)
                        .laboratory("FCh")
                        .build();

                var indication13 = Indication.builder()
                        .name("Barwa")
                        .isOrganoleptic(true)
                        .unit(null)
                        .laboratory("PCR")
                        .build();

                Assortment assortment1 = Assortment.builder()
                        .name("Kasza")
                        .group(group1)
                        .indications(List.of(indication11, indication12, indication13))
                        .organolepticMethod("ASD-ZXC")
                        .build();

                var indication21 = Indication.builder()
                        .name("Wilgotność")
                        .method("PN-EN ISO 665:2020-09")
                        .unit("g/100g")
                        .laboratory("FCh")
                        .build();

                var indication22 = Indication.builder()
                        .name("Liczba opadania")
                        .method("PN-EN ISO 3093:2007")
                        .unit(null)
                        .laboratory("PCR")
                        .build();

                Assortment assortment2 = Assortment.builder()
                        .name("Winogrona")
                        .group(group2)
                        .indications(List.of(indication21, indication22))
                        .build();

                var indication31 = Indication.builder()
                        .name("Wilgotność")
                        .method("PN-A-74108:1996 pkt 3.3.2")
                        .unit("g/100g")
                        .laboratory("FCh")
                        .build();

                Assortment assortment3 = Assortment.builder()
                        .name("Pomidory")
                        .group(group2)
                        .indications(List.of(indication31))
                        .build();

                assortmentRepository.saveAll(List.of(assortment1, assortment2, assortment3));

                //sample
                Address address1 = Address.builder()
                        .street("Kawiory 21")
                        .zipCode("30-055")
                        .city("Kraków")
                        .build();

                ReportData reportData1 = ReportData.builder()
                        .manufacturerName("manufacturer1")
                        .manufacturerAddress(address1)
                        .supplierName("supplier1")
                        .supplierAddress(address1)
                        .sellerName("seller1")
                        .sellerAddress(address1)
                        .recipientName("recipient1")
                        .recipientAddress(address1)
                        .jobNumber(11)
                        .mechanism("mechanism1")
                        .deliveryMethod("deliveryMethod1")
                        .build();

                Sample sample1 = Sample.builder()
                        .code(code1)
                        .client(client1)
                        .assortment(assortment1)
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusYears(3))
                        .expirationComment("Po otwarciu spożyć w ciągu 5 dni")
                        .examinationExpectedEndDate(LocalDate.now().plusMonths(3))
                        .size("500g")
                        .state("Bez zastrzeżeń")
                        .analysis(true)
                        .inspection(inspection1)
                        .samplingStandard(samplingStandard2)
                        .reportData(reportData1)
                        .progressStatus(ProgressStatusEnum.TODO)
                        .build();

                Address address2 = Address.builder()
                        .street("Sobieskiego 9")
                        .zipCode("31-136")
                        .city("Kraków")
                        .build();

                ReportData reportData2 = ReportData.builder()
                        .manufacturerName("manufacturer2")
                        .manufacturerAddress(address2)
                        .supplierName("supplier2")
                        .supplierAddress(address2)
                        .sellerName("seller2")
                        .sellerAddress(address2)
                        .recipientName("recipient2")
                        .recipientAddress(address2)
                        .jobNumber(12)
                        .mechanism("mechanism2")
                        .deliveryMethod("deliveryMethod2")
                        .build();

                Sample sample2 = Sample.builder()
                        .code(code2)
                        .client(client2)
                        .assortment(assortment2)
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusDays(10))
                        .expirationComment(null)
                        .examinationExpectedEndDate(LocalDate.now().plusMonths(2))
                        .size("700g")
                        .state("Bez zastrzeżeń")
                        .analysis(true)
                        .inspection(inspection2)
                        .samplingStandard(samplingStandard1)
                        .reportData(reportData2)
                        .progressStatus(ProgressStatusEnum.TODO)
                        .build();

                Sample sample3 = Sample.builder()
                        .code(code3)
                        .client(client2)
                        .assortment(assortment3)
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusDays(7))
                        .expirationComment(null)
                        .examinationExpectedEndDate(LocalDate.now().plusMonths(1))
                        .size("1kg")
                        .state("Bez zastrzeżeń")
                        .analysis(false)
                        .inspection(inspection3)
                        .samplingStandard(samplingStandard2)
                        .reportData(null)
                        .progressStatus(ProgressStatusEnum.TODO)
                        .build();

                sampleRepository.saveAll(List.of(sample1, sample2, sample3));

                //examination
                Examination examination1 = Examination.builder()
                        .sample(sample1)
                        .indication(indication11)
                        .signage("oznakowanie")
                        .nutritionalValue("wartość odżywcza")
                        .specification("specyfikacja")
                        .regulation("rozporządzenie")
                        .samplesNumber(4)
                        .result("wynik badania")
                        .startDate(LocalDate.now().plusMonths(2))
                        .endDate(LocalDate.now().plusMonths(3))
                        .methodStatus("(A)")
                        .uncertainty(0.01F)
                        .lod(0.24F)
                        .loq(0.5F)
                        .build();

                Examination examination2 = Examination.builder()
                        .sample(sample1)
                        .indication(indication12)
                        .signage("oznakowanie")
                        .nutritionalValue("wartość odżywcza")
                        .specification("specyfikacja")
                        .regulation("rozporządzenie")
                        .samplesNumber(4)
                        .build();

                examinationRepository.saveAll(List.of(examination1, examination2));
            }

            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .password("admin")
                        .role(RoleEnum.ADMIN)
                        .build();

                User worker = User.builder()
                        .name("worker")
                        .email("worker@gmail.com")
                        .password("worker")
                        .role(RoleEnum.WORKER)
                        .build();


                userRepository.saveAll(List.of(admin, worker));
            }
        };
    }
}
