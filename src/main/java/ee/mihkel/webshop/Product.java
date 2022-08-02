package ee.mihkel.webshop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// {id: Long, name: string}
// {id: 1L, name: "TooteNimi"}
//@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private double price;
    private String image;
    private boolean active; // isActive ei saa     getId(), getName(), getPrice(), isActive();

    public Product() {

    }

    public Product(Long id, String name, double price, String image, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.active = active;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
