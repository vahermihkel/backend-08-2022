package ee.mihkel.webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    List<Product> products = new ArrayList<>();

    @Autowired
    ProductRepository productRepository;

    @GetMapping("products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // localhost:8080/products?id=1&name=Toode&price=30&image=s&active=true
//    @GetMapping("products?id={id}&name={name}&price={price}&image={image}&active={active}")
//    @GetMapping("add-product2")
//    public List<Product> addProduct2(
//            @PathParam("id") Long id,
//            @PathParam("name") String name,
//            @PathParam("price") double price,
//            @PathParam("image") String image,
//            @PathParam("active") boolean active) {
//        Product product = new Product(id, name, price, image, active);
//        products.add(product);
//        return products;
//    }

    // GET / POST / DELETE / PUT
    // info front-endile, mis seal sees on toimumas
    @PostMapping("add-product")
    public List<Product> addProduct(@RequestBody Product product) {
//        products.add(product);
        // sout
//        System.out.println(!productRepository.findById(product.getId()).isPresent());
        if (!productRepository.existsById(product.getId())) {
            productRepository.save(product);
        }
        return productRepository.findAll();
    }

    @PutMapping("edit-product/{index}")  // PUT     localhost:8080/edit-product/1
    // [{id: 1,name:""},{id: 2,name:""}]
    //     tagastus Frondendile (tüüp)      mis andmeid nõuan koos päringuga
                                                //{id: 1,name:""}
    public List<Product> editProduct(@RequestBody Product product, @PathVariable int index) {
//        products.add(product);
//        products.set(index, product);
        if (productRepository.existsById(product.getId())) {
            productRepository.save(product);
        }
        return productRepository.findAll();
    }


    @GetMapping("get-product/{id}") // localhost:8080/get-product/1
    public Product getProduct(@PathVariable Long id) {
        // KAS null VÕI {id: 1 ,name:"Toode"}
        // KAS {id: 1 ,name:"Toode"} VÕI veateade
        return productRepository.findById(id).get();
    }

//    @PatchMapping("edit-product/{index}")  // PUT     localhost:8080/edit-product/1
//    // [{id: 1,name:""},{id: 2,name:""}]
//    //     tagastus Frondendile (tüüp)      mis andmeid nõuan koos päringuga
//    //{id: 1,name:""}
//    public List<Product> editProductOneField(@RequestBody Product newProduct, @PathVariable int index) {
////        products.add(product);
//        Product oldProduct = products.get(index);
//        if (newProduct.getId() != null) {
//            oldProduct.setId(newProduct.getId());
//        }
//        if (newProduct.getName() != null) {
//            oldProduct.setName(newProduct.getName());
//        }
////        (newProduct.getPrice() != null) ? oldProduct.setPrice(newProduct.getPrice()) : oldProduct.setPrice(newProduct.getPrice());
//        products.set(index, oldProduct);
//        return products;
//    }

    @DeleteMapping("delete-product/{id}")  //localhost:8080/delete-product/1
    public List<Product> deleteProduct(@PathVariable Long id) {
//        products.remove(index);
        productRepository.deleteById(id);
        return productRepository.findAll();
    }

//    @DeleteMapping("delete-product-id/{id}")  //localhost:8080/delete-product-id/1
//    public List<Product> deleteProductById(@PathVariable Long id) {
//        if (products.stream().anyMatch(e -> e.getId().equals(id))) {
//            Product product = products.stream().filter(e -> e.getId().equals(id)).findFirst().get();
//            products.remove(product);
//        }
//        return products;
//    }


    // Random@12312.1312321

    // 400 - Body on kaasa panemata, .... ,.... ÜLDISED VEAD
    // 404 - vale aadress
    // 405 - Method not allowed VALE method
    // 415 - ei saada JSON kuju

    // 500 - Backend võtab errori enda peale, minul juhtus viga
}


// VÕTTA - GET
// LISADA - POST
// MUUTA TERVIKUNA - PUT
// MUUTA ÜHTE AINSAT KINDLAT OSA - PATCH
// KUSTUTADA - DELETE
