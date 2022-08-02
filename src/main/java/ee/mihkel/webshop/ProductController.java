package ee.mihkel.webshop;

import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    List<Product> products = new ArrayList<>();

    @GetMapping("products")
    public List<Product> getProducts() {
        return products;
    }

    // localhost:8080/products?id=1&name=Toode&price=30&image=s&active=true
//    @GetMapping("products?id={id}&name={name}&price={price}&image={image}&active={active}")
    @GetMapping("add-product2")
    public List<Product> addProduct2(
            @PathParam("id") Long id,
            @PathParam("name") String name,
            @PathParam("price") double price,
            @PathParam("image") String image,
            @PathParam("active") boolean active) {
        Product product = new Product(id, name, price, image, active);
        products.add(product);
        return products;
    }

    // GET / POST / DELETE / PUT
    // info front-endile, mis seal sees on toimumas
    @PostMapping("add-product")
    public List<Product> addProduct(@RequestBody Product product) {
        products.add(product);
        return products;
    }
    // Random@12312.1312321

    // 400 - Body on kaasa panemata, .... ,.... ÜLDISED VEAD
    // 404 - vale aadress
    // 405 - Method not allowed VALE method
    // 415 - ei saada JSON kuju

    // 500 - Backend võtab errori enda peale, minul juhtus viga
}
