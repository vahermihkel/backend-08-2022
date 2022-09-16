package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.controller.model.EveryPayResponse;
import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.PersonRepository;
import ee.mihkel.webshop.repository.ProductRepository;
import ee.mihkel.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @GetMapping("orders/{personCode}")
    public ResponseEntity<List<Order>> getPersonOrders(@PathVariable String personCode) {
        Person person = personRepository.findById(personCode).get();
        return new ResponseEntity<>(orderRepository.findAllByPerson(person), HttpStatus.OK);
    }

    @PostMapping("orders/{personCode}")
    public ResponseEntity<EveryPayResponse> addNewOrder(@PathVariable String personCode, @RequestBody List<Product> products) {

        List<Product> originalProducts = orderService.findOriginalProducts(products);

        double totalSum = orderService.calculateTotalSum(originalProducts);

        Person person = personRepository.findById(personCode).get();
        Order order = orderService.saveOrder(person,originalProducts,totalSum);

        return new ResponseEntity<>(orderService.getLinkFromEveryPay(order), HttpStatus.CREATED) ;
    }

    @GetMapping("payment-completed")
    public ResponseEntity<String> checkIfPaid(
            @PathParam("payment_reference") String payment_reference
            ) {

        return new ResponseEntity<>(orderService.checkIfOrderIsPaid(payment_reference), HttpStatus.OK) ;
    }

    @GetMapping("orders-by-product/{productID}")
    public List<Long> getOrdersByProduct(@PathVariable Long productID) {
        Product product = productRepository.findById(productID).get();
        List<Long> ids = orderRepository.findAllByProductsOrderByIdAsc(product).stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        return ids;
    }
}

// client - front-end    server   - back-end
// 4xx -> client error Meie tegime Postmanis vea
// 5xx -> server error Postmanis on kõik õigesti tehtud, aga koodis juhtus viga

// Klient tuleb frontendis vajutab "Maksa"
// tuleb backendi päring "Alusta makset"

// Klient suunatakse backendist tuleva lingi abil frontendis EveryPay süsteemi

// Kui on edukas --> raha laekub meile
// Kui on mitteedukas --> raha ei laeku meile

// https://mihkel-webshop.herokuapp.com/payment-completed?order_reference=312312&payment_reference=31312
// Kas õnnestus?


// 1. Ise välja mõeldud projekt
// 2. Proovitöö järgi tehtud projekt
// 3. Tööprojekt
// 4. Udemy/Youtube järgi tehtud
// ainukene nõue on Springi kasutamine
//    ei ole nõueteks front-endi kasutamine, sisselogimine/registreerumine
