package ee.mihkel.webshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String image;
    private boolean active; // isActive ei saa     getId(), getName(), getPrice(), isActive();

    @ManyToOne
    private Category category; // "Liha- ja kalatooted   Liha-ja kalatooted
}

// @OneToOne
// RANGELT ÜHELT TOOTEL ÜKS KATEGOORIA
// JA ÜHEL KATEGOORIAL ÜKS TOODE
