package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.controller.model.CartProduct;
import ee.mihkel.webshop.controller.model.EveryPayResponse;
import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.CartProductRepository;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.PersonRepository;
import ee.mihkel.webshop.repository.ProductRepository;
import ee.mihkel.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    CartProductRepository cartProductRepository;

    @GetMapping("orders")
    public ResponseEntity<List<Order>> getPersonOrders() {
        String personCode = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Person person = personRepository.findById(personCode).get();
        return new ResponseEntity<>(orderRepository.findAllByPerson(person), HttpStatus.OK);
    }

    @PostMapping("orders")                                         // [{productId: Product, quantity: 8}]
    public ResponseEntity<EveryPayResponse> addNewOrder(@RequestBody List<CartProduct> cartProducts) {
        String personCode = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        // List   {product: Product, quantity: int}
//        List<Product> originalProducts = orderService.findOriginalProducts(ids);

        double totalSum = orderService.calculateTotalSum(cartProducts);

        Person person = personRepository.findById(personCode).get();
        Order order = orderService.saveOrder(person,cartProducts,totalSum);

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
        List<Long> ids = cartProductRepository.findAllByProductOrderByIdAsc(product).stream()
                .map(CartProduct::getId)
                .collect(Collectors.toList());
        return ids;
    }
}

// client - front-end    server   - back-end
// 4xx -> client error Meie tegime Postmanis vea
// 5xx -> server error Postmanis on k??ik ??igesti tehtud, aga koodis juhtus viga

// Klient tuleb frontendis vajutab "Maksa"
// tuleb backendi p??ring "Alusta makset"

// Klient suunatakse backendist tuleva lingi abil frontendis EveryPay s??steemi

// Kui on edukas --> raha laekub meile
// Kui on mitteedukas --> raha ei laeku meile

// https://mihkel-webshop.herokuapp.com/payment-completed?order_reference=312312&payment_reference=31312
// Kas ??nnestus?


// 1. Ise v??lja m??eldud projekt
// 2. Proovit???? j??rgi tehtud projekt
// 3. T????projekt
// 4. Udemy/Youtube j??rgi tehtud
// ainukene n??ue on Springi kasutamine
//    ei ole n??ueteks front-endi kasutamine, sisselogimine/registreerumine
