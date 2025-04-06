package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xmls.CarsSeedDto;
import softuni.exam.models.dto.xmls.CarsSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.validation.ValidationUtil;
import softuni.exam.util.xmlParser.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    private static String CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml";

    private final CarRepository carRepository;

    private final XmlParser xmlParser;

    private final ValidationUtil validationUtil;

    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepository carRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return new String(Files.readAllBytes(Path.of(CARS_FILE_PATH)));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        CarsSeedRootDto carsSeedRootDto = this.xmlParser.parse(CarsSeedRootDto.class, CARS_FILE_PATH);

        for (CarsSeedDto carsSeedDto : carsSeedRootDto.getCarsSeedDtoList()) {
            Optional<Car> optionalCar = carRepository.findAllByPlateNumber(carsSeedDto.getPlateNumber());
            if (!validationUtil.isValid(carsSeedDto) || optionalCar.isPresent()) {
                sb.append("Invalid car\n");
                continue;
            }
            Car car = modelMapper.map(carsSeedDto, Car.class);
            carRepository.saveAndFlush(car);
            sb.append(String.format("Successfully imported car %s - %s%n", car.getCarMake(), car.getCarModel()));
        }

        return sb.toString();
    }
}
