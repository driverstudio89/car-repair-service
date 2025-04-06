package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.jsons.PartsSeedDto;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.validation.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PartServiceImpl implements PartService {

    private static String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";

    private final PartRepository partRepository;

    private final Gson gson;

    private final ModelMapper modelMapper;

    private final ValidationUtil validationUtil;

    public PartServiceImpl(PartRepository partRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return new String(Files.readAllBytes(Path.of(PARTS_FILE_PATH)));
    }

    @Override
    public String importParts() throws IOException {
        StringBuilder sb = new StringBuilder();

        PartsSeedDto[] partsSeedDtos = this.gson.fromJson(readPartsFileContent(), PartsSeedDto[].class);

        for (PartsSeedDto partsSeedDto : partsSeedDtos) {
            Optional<Part> optionalPart = partRepository.findAllByPartName(partsSeedDto.getPartName());

            if (!validationUtil.isValid(partsSeedDto) || optionalPart.isPresent()) {
                sb.append("Invalid part\n");
                continue;
            }
            Part part = modelMapper.map(partsSeedDto, Part.class);
            partRepository.saveAndFlush(part);
            sb.append(String.format("Successfully imported part %s - %.2f%n", part.getPartName(), part.getPrice()));
        }

        return sb.toString();
    }
}
