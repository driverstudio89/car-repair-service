package softuni.exam.models.dto.xmls;

import softuni.exam.util.Adapter.LocalDateTimeAdapter;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskSeedDto implements Serializable {

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime date;

    @XmlElement(name = "price")
    @Positive
    private BigDecimal price;

    @XmlElement(name = "car")
    private CarDto carDto;

    @XmlElement(name = "mechanic")
    private MechanicDto mechanicDto;

    @XmlElement(name = "part")
    private PartDto pratDto;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CarDto getCarDto() {
        return carDto;
    }

    public void setCarDto(CarDto carDto) {
        this.carDto = carDto;
    }

    public MechanicDto getMechanicDto() {
        return mechanicDto;
    }

    public void setMechanicDto(MechanicDto mechanicDto) {
        this.mechanicDto = mechanicDto;
    }

    public PartDto getPratDto() {
        return pratDto;
    }

    public void setPratDto(PartDto pratDto) {
        this.pratDto = pratDto;
    }
}
