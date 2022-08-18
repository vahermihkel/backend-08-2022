package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PersonRepository personRepository;

    @GetMapping("orders/{personCode}")
    public List<Order> getPersonOrders(@PathVariable String personCode) {
        Person person = personRepository.findById(personCode).get();
        return orderRepository.findAllByPerson(person);
    }

    @PostMapping("orders/{personCode}")
    public List<Order> addNewOrder(@PathVariable String personCode, @RequestBody List<Product> products) {
        Person person = personRepository.findById(personCode).get();
        Order order = new Order();
        order.setCreationDate(new Date());
        order.setPerson(person);
        order.setProducts(products);
//        double totalSum = 0;
//        for (Product product: products) {
////            totalSum = totalSum + product.getPrice();
//            if (product.isActive()) {
//                totalSum += product.getPrice();
//            }
//        }
                            // map on asendus -> asenda iga Product Double väärtusega
                            // element (e) product (p)
                            // Product seest võetud hinnaga
        double totalSum = products.stream()
//                .filter(Product::isActive)
                .mapToDouble(Product::getPrice)
                .sum();

        order.setTotalSum(totalSum);
        orderRepository.save(order);
        return orderRepository.findAllByPerson(person);
    }
}

// client - front-end    server   - back-end
// 4xx -> client error Meie tegime Postmanis vea
// 5xx -> server error Postmanis on kõik õigesti tehtud, aga koodis juhtus viga
