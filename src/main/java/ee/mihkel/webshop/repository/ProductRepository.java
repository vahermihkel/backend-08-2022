package ee.mihkel.webshop.repository;

import ee.mihkel.webshop.entity.Category;
import ee.mihkel.webshop.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // tagasta kõik tooted kellel
    //              kogus on suurem kui (int stock)
    //                              ja aktiivus on võrdne (boolean active)
    //                                                    sorteeri kasvavas järjekorras
    List<Product> findAllByOrderById();

    List<Product> findAllByStockGreaterThanAndActiveEqualsOrderByIdAsc(int stock, boolean active);

    List<Product> findAllByStockGreaterThanAndActiveEqualsOrderByIdAsc(int stock, boolean active, Pageable pageable);

    List<Product> findAllByCategoryOrderByIdAsc(Category category);
}
