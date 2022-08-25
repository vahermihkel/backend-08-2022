package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.PersonRepository;
import ee.mihkel.webshop.repository.ProductRepository;
import ee.mihkel.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@RestController
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    OrderService orderService;

    @GetMapping("orders/{personCode}")
    public List<Order> getPersonOrders(@PathVariable String personCode) {
        Person person = personRepository.findById(personCode).get();
        return orderRepository.findAllByPerson(person);
    }

    @PostMapping("orders/{personCode}")
    public String addNewOrder(@PathVariable String personCode, @RequestBody List<Product> products) {

        List<Product> originalProducts = orderService.findOriginalProducts(products);

        double totalSum = orderService.calculateTotalSum(originalProducts);

        Person person = personRepository.findById(personCode).get();
        Order order = orderService.saveOrder(person,originalProducts,totalSum);

        return orderService.getLinkFromEveryPay(order);
    }

    @GetMapping("payment-completed")
    public String checkIfPaid(
            @PathParam("order_reference") String order_reference,
            @PathParam("payment_reference") String payment_reference
            ) {
        return "Makse õnnestus: " + order_reference + payment_reference;
    }
}

// client - front-end    server   - back-end
// 4xx -> client error Meie tegime Postmanis vea
// 5xx -> server error Postmanis on kõik õigesti tehtud, aga koodis juhtus viga
