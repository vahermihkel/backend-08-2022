package ee.mihkel.webshop.repository;

import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//HIBERNATE

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByPerson(Person person);

//    List<Order> findAllByPersonOrderById(Person person);
//    List<Order> findAllByOrderById();
}

// SELECT * FROM orders WHERE orders.person_person_code = person.getId();


