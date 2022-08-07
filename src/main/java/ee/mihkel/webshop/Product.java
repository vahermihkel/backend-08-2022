package ee.mihkel.webshop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

// {id: Long, name: string}
// {id: 1L, name: "TooteNimi"}
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    private Long id;
    private String name;
    private double price;
    private String image;
    private boolean active; // isActive ei saa     getId(), getName(), getPrice(), isActive();
}
