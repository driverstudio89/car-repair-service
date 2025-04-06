package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xmls.TaskSeedDto;
import softuni.exam.models.dto.xmls.TaskSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.models.entity.Task;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
import softuni.exam.util.validation.ValidationUtil;
import softuni.exam.util.xmlParser.XmlParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";

    private final TaskRepository taskRepository;

    private final MechanicRepository mechanicRepository;

    private final CarRepository carRepository;

    private final PartRepository partRepository;

    private final ModelMapper modelMapper;

    private final XmlParser xmlParser;

    private final ValidationUtil validationUtil;

    public TaskServiceImpl(TaskRepository taskRepository, MechanicRepository mechanicRepository, CarRepository carRepository, PartRepository partRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.taskRepository = taskRepository;
        this.mechanicRepository = mechanicRepository;
        this.carRepository = carRepository;
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return new String(Files.readAllBytes(Path.of(TASKS_FILE_PATH)));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        TaskSeedRootDto taskSeedRootDto = this.xmlParser.parse(TaskSeedRootDto.class, TASKS_FILE_PATH);


        for (TaskSeedDto taskSeedDto : taskSeedRootDto.getTaskSeedDtoList()) {
            Optional<Mechanic> optionalMechanic = this.mechanicRepository.findAllByFirstName(taskSeedDto.getMechanicDto().getFirstName());

            if (!this.validationUtil.isValid(taskSeedDto) || optionalMechanic.isEmpty()) {
                sb.append("Invalid task\n");
                continue;
            }

            Car car = carRepository.findById(taskSeedDto.getCarDto().getId()).get();
            Part part = partRepository.findById(taskSeedDto.getPratDto().getId()).get();
            Task task = this.modelMapper.map(taskSeedDto, Task.class);
            task.setCars(car);
            task.setParts(part);
            task.setMechanic(optionalMechanic.get());
            System.out.println();

            this.taskRepository.saveAndFlush(task);

            sb.append(String.format("Successfully imported task %.2f%n", task.getPrice()));

        }

        return sb.toString();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        return taskRepository.findAllByPriceDesc()
                .stream()
                .map(t -> String.format(
                        "Car %s %s with %dkm\n" +
                        "-Mechanic: %s %s - task №%d:\n" +
                        " --Engine: %.2f\n" +
                        "---Price: %.2f$\n"
                        ,t.getCars().getCarMake(), t.getCars().getCarModel(), t.getCars().getKilometers()
                        ,t.getMechanic().getFirstName(), t.getMechanic().getLastName(), t.getId()
                        ,t.getCars().getEngine()
                        ,t.getPrice().setScale(2, RoundingMode.HALF_UP)
                )).collect(Collectors.joining());
    }
}
