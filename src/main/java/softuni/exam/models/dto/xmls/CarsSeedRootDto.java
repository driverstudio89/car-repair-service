package softuni.exam.models.dto.xmls;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarsSeedRootDto implements Serializable {

    @XmlElement(name = "car")
    private List<CarsSeedDto> carsSeedDtoList;

    public List<CarsSeedDto> getCarsSeedDtoList() {
        return carsSeedDtoList;
    }

    public void setCarsSeedDtoList(List<CarsSeedDto> carsSeedDtoList) {
        this.carsSeedDtoList = carsSeedDtoList;
    }
}
