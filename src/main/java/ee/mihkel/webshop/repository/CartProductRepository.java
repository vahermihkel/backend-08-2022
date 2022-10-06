package ee.mihkel.webshop.repository;

import ee.mihkel.webshop.controller.model.CartProduct;
import ee.mihkel.webshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByProductOrderByIdAsc(Product product);
}
