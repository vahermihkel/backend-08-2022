package ee.mihkel.webshop.controller;

import ee.mihkel.webshop.cache.ProductCache;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ProductController {
    List<Product> products = new ArrayList<>();

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCache productCache;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
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
    public ResponseEntity<List<Product>> addProduct(@RequestBody Product product) {
//        products.add(product);
        // sout
//        System.out.println(!productRepository.findById(product.getId()).isPresent());
//        if (!productRepository.existsById(product.getId())) {
            productRepository.save(product);
//        }
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.CREATED);
    }

    @PutMapping("edit-product/{index}")  // PUT     localhost:8080/edit-product/1
    // [{id: 1,name:""},{id: 2,name:""}]
    //     tagastus Frondendile (tüüp)      mis andmeid nõuan koos päringuga
                                                //{id: 1,name:""}
    public ResponseEntity<List<Product>> editProduct(@RequestBody Product product, @PathVariable int index) {
//        products.add(product);
//        products.set(index, product);
        if (productRepository.existsById(product.getId())) {
            productRepository.save(product);
            productCache.emptyCache();
        }
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }


    @GetMapping("get-product/{id}") // localhost:8080/get-product/1
    public ResponseEntity<Product> getProduct(@PathVariable Long id) throws ExecutionException {
        // KAS null VÕI {id: 1 ,name:"Toode"}
        // KAS {id: 1 ,name:"Toode"} VÕI veateade
        return new ResponseEntity<>(productCache.getProduct(id), HttpStatus.OK);
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
    public ResponseEntity<List<Product>> deleteProduct(@PathVariable Long id) {
//        products.remove(index);
        productRepository.deleteById(id);
        productCache.emptyCache();
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    // GET - võtmiseks POST - lisamiseks DELETE - kustutamiseks
    // PUT - tervikuna asendamiseks    PATCH - mingi ühe omaduse asendamine
    @PatchMapping("add-stock")
    public ResponseEntity<List<Product>> addStock(@RequestBody Product product) {
        Product originalProduct = productRepository.findById(product.getId()).get();
//        if (originalProduct.getStock() == null) {
//
//        }
        originalProduct.setStock(originalProduct.getStock()+1);
        productRepository.save(originalProduct);
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @PatchMapping("decrease-stock")
    public ResponseEntity<List<Product>> decreaseStock(@RequestBody Product product) {
        Product originalProduct = productRepository.findById(product.getId()).get();
        if (originalProduct.getStock() > 0) {
            originalProduct.setStock(originalProduct.getStock()-1);
            productRepository.save(originalProduct);
        }
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    // lisame igale tootlee andmebaasi ka koguse
    // API otspunkti kaudu saab kogusele +1 ja -1 panna
    // -1 kaudu ei lase miinusesse

    // võiks teha eraldi API otspunktid aktiivsete ja + kogustega toodete jaoks

    @GetMapping("active-products")
    public ResponseEntity<List<Product>> getAllActiveProducts() {
        return new ResponseEntity<>(
                productRepository.findAllByStockGreaterThanAndActiveEqualsOrderByIdAsc(0,true),
                HttpStatus.OK
        );
    }

    @GetMapping("active-products/{pagenr}")
    public ResponseEntity<List<Product>> getActiveProductsPerPage(@PathVariable int pagenr) {
        Pageable pageRequest = PageRequest.of(pagenr, 3);
        return new ResponseEntity<>(
                productRepository.findAllByStockGreaterThanAndActiveEqualsOrderByIdAsc(0,true, pageRequest),
                HttpStatus.OK
        );
    }

    @GetMapping("products-per-page/{pagenr}")
    public Page<Product> getProducsPerPage(@PathVariable int pagenr) {
        Pageable pageRequest = PageRequest.of(pagenr, 3);
        return productRepository.findAll(pageRequest);
    }


    // Pagination -> võtmine lehekülje kaupa

    // Productis kontrollid, et ei saa ilma nime ja hinnata sisestada

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
