package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.jsons.MechanicsSeedDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.validation.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class MechanicServiceImpl implements MechanicService {

    private static String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";

    private final MechanicRepository mechanicRepository;

    private final Gson gson;

    private final ModelMapper modelMapper;

    private final ValidationUtil validationUtil;

    public MechanicServiceImpl(MechanicRepository mechanicRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.mechanicRepository = mechanicRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return new String(Files.readAllBytes(Path.of(MECHANICS_FILE_PATH)));
    }

    @Override
    public String importMechanics() throws IOException {
        StringBuilder sb = new StringBuilder();

        MechanicsSeedDto[] mechanicsSeedDtos = this.gson.fromJson(readMechanicsFromFile(), MechanicsSeedDto[].class);

        for (MechanicsSeedDto mechanicsSeedDto : mechanicsSeedDtos) {
            Optional<Mechanic> optionalMechanic = mechanicRepository.findAllByEmail(mechanicsSeedDto.getEmail());
            if (!validationUtil.isValid(mechanicsSeedDto) || optionalMechanic.isPresent()) {
                sb.append("Invalid mechanic\n");
                continue;
            }
            Mechanic mechanic = modelMapper.map(mechanicsSeedDto, Mechanic.class);
            mechanicRepository.saveAndFlush(mechanic);
            sb.append(String.format("Successfully imported mechanic %s %s%n", mechanic.getFirstName(), mechanic.getLastName()));
        }

        return sb.toString();
    }
}
