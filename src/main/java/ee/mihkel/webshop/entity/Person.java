package ee.mihkel.webshop.entity;

// kui oleks pannud User klassi nimeks
// ja Entity() oleks postgres olnud error
// Tabel("")

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person {
    @Id
    private String personCode;
    private String email;
    private String firstName;
    private String lastName;
    private String telephone;
    private String address;
}


