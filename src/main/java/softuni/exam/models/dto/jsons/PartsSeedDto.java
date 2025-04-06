package softuni.exam.models.dto.jsons;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;
import java.io.Serializable;

public class PartsSeedDto implements Serializable {

    @Expose
    @Size(min = 2, max = 19)
    @NotNull
    private String partName;

    @Expose
    @Min(10)
    @Max(2000)
    @NotNull
    private double price;

    @Expose
    @Positive
    @NotNull
    private int quantity;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
