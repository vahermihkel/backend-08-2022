package ee.mihkel.webshop.service;

import ee.mihkel.webshop.cache.ProductCache;
import ee.mihkel.webshop.entity.Order;
import ee.mihkel.webshop.entity.Person;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.OrderRepository;
import ee.mihkel.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void saveOrder(Person person, List<Product> originalProducts, double totalSum) {
        Order order = new Order();
        order.setCreationDate(new Date());
        order.setPerson(person);
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
        orderRepository.save(order);
    }
}
