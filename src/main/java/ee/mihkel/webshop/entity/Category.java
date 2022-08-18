package ee.mihkel.webshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

// @GeneratedValue == @GeneratedValue(strategy = GenerationType.AUTO)
//Product: 3  6  7
//Category:  1  2  4  5  8

// @GeneratedValue == @GeneratedValue(strategy = GenerationType.IDENTITY)
//Product: 1  2  3
//Category:  1  2  3  4  5
