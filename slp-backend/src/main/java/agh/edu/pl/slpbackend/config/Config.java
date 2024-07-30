package agh.edu.pl.slpbackend.config;

import agh.edu.pl.slpbackend.enums.RoleEnum;
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
            final SampleRepository sampleRepository,
            final CodeRepository codeRepository,
            final ClientRepository clientRepository,
            final InspectionRepository inspectionRepository,
            final SamplingStandardRepository samplingStandardRepository,
            final IndicationRepository indicationRepository,
            final ProductGroupRepository groupRepository,
            final ExaminationRepository examinationRepository,
            final UserRepository userRepository) {
        return args -> {
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

                //indication
                Indication indication1 = Indication.builder()
                        .name("Wilgotność")
                        .norm("PN-EN ISO 712:2012")
                        .unit("g/100g")
                        .laboratory("FCh")
                        .build();

                Indication indication2 = Indication.builder()
                        .name("Ocena organoleptyczna - mąka i kasza")
                        .norm("PN-A-74013:1964")
                        .unit(null)
                        .laboratory("FCh")
                        .build();

                Indication indication3 = Indication.builder()
                        .name("Liczba opadania")
                        .norm("PN-EN ISO 3093:2007")
                        .unit(null)
                        .laboratory("PCR")
                        .build();

                indicationRepository.saveAll(List.of(indication1, indication2, indication3));

                //group
                ProductGroup group1 = ProductGroup.builder()
                        .name("Przetwory zbożowe")
                        .samplingStandards(List.of(samplingStandard1, samplingStandard2, samplingStandard3))
                        .indications(List.of(indication1, indication2))
                        .build();

                ProductGroup group2 = ProductGroup.builder()
                        .name("Świeże owoce i warzywa")
                        .samplingStandards(List.of(samplingStandard1, samplingStandard2))
                        .indications(List.of(indication1, indication3))
                        .build();

                groupRepository.saveAll(List.of(group1, group2));

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
                        .assortment("Kasza")
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusYears(3))
                        .expirationComment("Po otwarciu spożyć w ciągu 5 dni")
                        .examinationEndDate(LocalDate.now().plusMonths(3))
                        .size("500g")
                        .state("Bez zastrzeżeń")
                        .analysis(true)
                        .inspection(inspection1)
                        .group(group1)
                        .samplingStandard(samplingStandard2)
                        .reportData(reportData1)
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
                        .assortment("Winogrona")
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusDays(10))
                        .expirationComment(null)
                        .examinationEndDate(LocalDate.now().plusMonths(2))
                        .size("700g")
                        .state("Bez zastrzeżeń")
                        .analysis(true)
                        .inspection(inspection2)
                        .group(group2)
                        .samplingStandard(samplingStandard1)
                        .reportData(reportData2)
                        .build();

                Sample sample3 = Sample.builder()
                        .code(code3)
                        .client(client2)
                        .assortment("Pomidory")
                        .admissionDate(LocalDate.now())
                        .expirationDate(LocalDate.now().plusDays(7))
                        .expirationComment(null)
                        .examinationEndDate(LocalDate.now().plusMonths(1))
                        .size("1kg")
                        .state("Bez zastrzeżeń")
                        .analysis(false)
                        .inspection(inspection3)
                        .group(group2)
                        .samplingStandard(samplingStandard2)
                        .reportData(null)
                        .build();

                sampleRepository.saveAll(List.of(sample1, sample2, sample3));

                //examination
                Examination examination1 = Examination.builder()
                        .sample(sample1)
                        .indication(indication1)
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
                        .indication(indication2)
                        .signage("oznakowanie")
                        .nutritionalValue("wartość odżywcza")
                        .specification("specyfikacja")
                        .regulation("rozporządzenie")
                        .samplesNumber(4)
                        .build();

                examinationRepository.saveAll(List.of(examination1, examination2));

                User user = User.builder()
                        .name("user1")
                        .email("user1@emali.com")
                        .password("123456")
                        .role(RoleEnum.WORKER)
                        .build();

                userRepository.save(user);
            }
        };
    }
}
