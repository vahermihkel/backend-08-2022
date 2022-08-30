package ee.mihkel.webshop.service;

import ee.mihkel.webshop.cache.ProductCache;
import ee.mihkel.webshop.controller.model.EveryPayData;
import ee.mihkel.webshop.controller.model.EveryPayResponse;
import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductCache productCache;

    private String apiUsername = "92ddcfab96e34a5f";
    private String accountName = "EUR3D1";
    private String customerUrl = "https://mihkel-webshop.herokuapp.com/payment-completed";

    public List<Product> findOriginalProducts(List<Product> products) {
        return products.stream()
                .map(e -> {
                    try {
                        return productCache.getProduct(e.getId());
                    } catch (ExecutionException executionException) {
                        throw new RuntimeException();
                    }
                })
                .collect(Collectors.toList());
    }

    public double calculateTotalSum(List<Product> originalProducts) {
        return originalProducts.stream()
//                .filter(Product::isActive)
                .mapToDouble(Product::getPrice) // OTSE PÄRINGUST ARVUTATAKSE KOGUSUMMA
                .sum();
    }


    public Order saveOrder(Person person, List<Product> originalProducts, double totalSum) {
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
        order.setProducts(originalProducts); // OTSE PÄRINGUST PANNAKSE ANDMEBAASI
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

    public String getLinkFromEveryPay(Order order) {

        // @Autowired
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://igw-demo.every-pay.com/api/v4/payments/oneoff";

        System.out.println(new Date());
        System.out.println(ZonedDateTime.now());
        System.out.println("EXPECTED: 2022-08-25T18:20:15+03:00");

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
        headers.set("Authorization", "Basic OTJkZGNmYWI5NmUzNGE1Zjo4Y2QxOWU5OWU5YzJjMjA4ZWU1NjNhYmY3ZDBlNGRhZA==");

                                    //  null  ->   data
        HttpEntity<EveryPayData> entity = new HttpEntity<>(data, headers);

        // https://json2csharp.com/code-converters/json-to-pojo

        ResponseEntity<EveryPayResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, EveryPayResponse.class);

        return response.getBody().payment_link;
    }
}
