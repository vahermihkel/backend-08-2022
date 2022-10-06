package ee.mihkel.webshop.service;

import ee.mihkel.webshop.cache.ProductCache;
import ee.mihkel.webshop.controller.model.CartProduct;
import ee.mihkel.webshop.controller.model.EveryPayData;
import ee.mihkel.webshop.controller.model.EveryPayResponse;
import ee.mihkel.webshop.controller.model.EveryPayState;
import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.CartProductRepository;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductCache productCache;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CartProductRepository cartProductRepository;

    // import org.springframework.beans.factory.annotation.Value;
    @Value("${everypay.username}")
    private String apiUsername;

    @Value("${everypay.account}")
    private String accountName;

    @Value("${everypay.customerurl}")
    private String customerUrl;

    @Value("${everypay.headers}")
    private String everyPayHeaders;

    @Value("${everypay.url}")
    private String everyPayUrl;

    public List<Product> findOriginalProducts(List<Long> products) {
        log.info("Fetching original products");
        return products.stream()
                .map(e -> {
                    try {
                        return productCache.getProduct(e);
                    } catch (ExecutionException executionException) {
                        throw new RuntimeException();
                    }
                })
                .collect(Collectors.toList());
    }

    public double calculateTotalSum(List<CartProduct> cartProducts) {
        String personCode = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Calculating total sum {}", personCode);
        return cartProducts.stream()
//                .filter(Product::isActive)
                .mapToDouble(e -> e.getProduct().getPrice() * e.getQuantity()) // OTSE PÄRINGUST ARVUTATAKSE KOGUSUMMA
                .sum();
    }

    @Transactional
    public Order saveOrder(Person person, List<CartProduct> cartProducts, double totalSum) {

        String personCode = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Starting to save Order {}", personCode);

        cartProductRepository.saveAll(cartProducts);

        Order order = new Order();
        order.setCreationDate(new Date());
        order.setPerson(person);
        order.setPaidState("initial");
        // OTSI ID ALUSEL KÕIKIDELE TOODETELE ORIGINAAL
//        List<Product> originalProducts = new ArrayList<>();
//        for (Product product: products) {
//            Long id = product.getId();
//            Product originalProduct = productRepository.findById(id).get();
//            originalProducts.add(originalProduct);
////            originalProducts.add(productRepository.findById(product.getId()).get());
//            // Robert C. Martin (Uncle Bob) - Clean Code
//        }
        order.setLineItem(cartProducts); // OTSE PÄRINGUST PANNAKSE ANDMEBAASI
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
        order.setTotalSum(totalSum);
        return orderRepository.save(order);
    }

    public EveryPayResponse getLinkFromEveryPay(Order order) {

        // @Autowired

        String url = everyPayUrl + "/payments/oneoff";

        // HttpEntity <- kogub kokku päringuga seotud sisu body: vasakul ja headers: paremal
        EveryPayData data = new EveryPayData();
        data.setApi_username(apiUsername);
        data.setAccount_name(accountName);
        data.setAmount(order.getTotalSum());
        data.setOrder_reference(order.getId().toString());
        data.setNonce(order.getId().toString() + new Date() + Math.random());
        data.setTimestamp(ZonedDateTime.now().toString()); /// <------
        data.setCustomer_url(customerUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", everyPayHeaders);

                                    //  null  ->   data
        HttpEntity<EveryPayData> entity = new HttpEntity<>(data, headers);
        // https://json2csharp.com/code-converters/json-to-pojo

        ResponseEntity<EveryPayResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, EveryPayResponse.class);
        return response.getBody();
    }

    public String checkIfOrderIsPaid(String payment_reference) {
        String url = everyPayUrl + "/payments/" + payment_reference + "?api_username=" + apiUsername;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", everyPayHeaders);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<EveryPayState> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, EveryPayState.class);

        if (response.getBody() != null) {
            String order_reference = response.getBody().order_reference;
            Order order = orderRepository.findById(Long.parseLong(order_reference)).get();
            // ctrl + alt + m
            return getPaymentState(response, order_reference, order);
        } else {
            return "Ühenduse viga!";
        }
    }

    private String getPaymentState(ResponseEntity<EveryPayState> response, String order_reference, Order order) {
        switch (response.getBody().payment_state) {
            case "settled":
                order.setPaidState("settled");
                orderRepository.save(order);
                return "Makse õnnestus: " + order_reference;
            case "failed":
                order.setPaidState("failed");
                orderRepository.save(order);
                return "Makse ebaõnnestus: " + order_reference;
            case "cancelled":
                order.setPaidState("cancelled");
                orderRepository.save(order);
                return "Makse katkestati: " + order_reference;
            default:
                return "Makse ei toiminud";
        }
    }
}

// @ControllerAdvice - hakkame Exceptioneid kinni püüdma / ära vahetama
