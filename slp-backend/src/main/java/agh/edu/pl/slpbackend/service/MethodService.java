package agh.edu.pl.slpbackend.service;

import agh.edu.pl.slpbackend.model.Assortment;
import agh.edu.pl.slpbackend.model.Indication;
import agh.edu.pl.slpbackend.model.ProductGroup;
import agh.edu.pl.slpbackend.model.SamplingStandard;
import agh.edu.pl.slpbackend.repository.AssortmentRepository;
import agh.edu.pl.slpbackend.repository.ProductGroupRepository;
import agh.edu.pl.slpbackend.repository.SamplingStandardRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class MethodService {

    private static final int SAMPLING_STANDARD_COLUMN = 99;

    private final ProductGroupRepository groupRepository;
    private final AssortmentRepository assortmentRepository;
    private final SamplingStandardRepository samplingStandardRepository;

    @Transactional
    public void importMethods(InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet groupListSheet = workbook.getSheet("grupy");

            for (int i = 2; ; i++) {
                Row groupRow = groupListSheet.getRow(i);
                if (groupRow == null || isCellEmpty(groupRow.getCell(0))) break;

                String groupId = groupRow.getCell(0).getStringCellValue();
                Sheet groupSheet = workbook.getSheet(groupId);
                if (groupSheet == null) continue;

                Cell groupNameCell = groupRow.getCell(1);
                if (isCellEmpty(groupNameCell)) continue;

                String groupName = groupNameCell.getStringCellValue();

                ProductGroup group = groupRepository.findByName(groupName)
                        .orElse(ProductGroup.builder()
                                .name(groupName)
                                .assortments(new ArrayList<>())
                                .samplingStandards(new ArrayList<>())
                                .build());
                groupRepository.save(group);

                Row headerRow = groupSheet.getRow(0);

                for (int j = 0; ; j += 4) {
                    Cell assortmentCell = headerRow.getCell(j);
                    if (isCellEmpty(assortmentCell)) break;

                    String assortmentName = assortmentCell.getStringCellValue();
                    Assortment assortment = group.getAssortments().stream()
                            .filter(a -> a.getName().equals(assortmentName))
                            .findAny()
                            .orElse(Assortment.builder()
                                    .name(assortmentName)
                                    .group(group)
                                    .indications(new ArrayList<>())
                                    .build());

                    for (int k = 1; ; k++) {
                        Row indicationRow = groupSheet.getRow(k);
                        if (indicationRow == null || isCellEmpty(indicationRow.getCell(j))) break;

                        String indicationName = indicationRow.getCell(j).getStringCellValue();
                        if (assortment.getIndications().stream()
                                .anyMatch(ind -> ind.getName().equals(indicationName))) continue;

                        Indication indication = Indication.builder()
                                .name(indicationName)
                                .method(getCellValue(indicationRow, j + 1))
                                .unit(getCellValue(indicationRow, j + 2))
                                .laboratory(getCellValue(indicationRow, j + 3))
                                .build();

                        assortment.getIndications().add(indication);
                    }

                    assortmentRepository.save(assortment);
                }

                for (int j = 1; ; j++) {
                    Row samplingStandardRow = groupSheet.getRow(j);
                    if (samplingStandardRow == null || isCellEmpty(samplingStandardRow.getCell(SAMPLING_STANDARD_COLUMN)))
                        break;

                    String samplingStandardName = samplingStandardRow.getCell(SAMPLING_STANDARD_COLUMN).getStringCellValue();

                    if (group.getSamplingStandards().stream()
                            .anyMatch(ss -> ss.getName().equals(samplingStandardName))) continue;

                    SamplingStandard samplingStandard = samplingStandardRepository.findByName(samplingStandardName)
                            .orElse(SamplingStandard.builder()
                                    .name(samplingStandardName)
                                    .groups(new ArrayList<>())
                                    .build());

                    samplingStandard.getGroups().add(group);
                    samplingStandardRepository.save(samplingStandard);

                    group.getSamplingStandards().add(samplingStandard);
                    groupRepository.save(group);
                }
            }
        }
    }

    private boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK;
    }

    private String getCellValue(Row row, int i) {
        Cell cell = row.getCell(i);
        if (isCellEmpty(cell)) return null;
        return cell.getStringCellValue();
    }
}
