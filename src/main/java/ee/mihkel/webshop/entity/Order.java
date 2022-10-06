package ee.mihkel.webshop.entity;

import ee.mihkel.webshop.controller.model.CartProduct;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

// PostgreSQL reserveerib kindlad tabelid endale ära:
// Order
// User

@Data
@Entity
@SequenceGenerator(name="seq", initialValue=230000, allocationSize=1)
@Table(name = "orders") // vahetame tabeli nime PostgreSQL-s
public class Order { // by default lähevad klassi nimed tabelite nimeks
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq")
    private Long id;

    private Date creationDate;

    private double totalSum;

    private String paidState;

    @OneToMany
    private List<CartProduct> lineItem;

    @ManyToOne
    private Person person;
}

// 1,2,3,4,5,6  <--- Identity
// 100000,100001,100002 jne  <--- Sequence
