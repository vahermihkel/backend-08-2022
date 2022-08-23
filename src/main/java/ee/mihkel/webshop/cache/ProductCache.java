package ee.mihkel.webshop.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ee.mihkel.webshop.entity.Product;
import ee.mihkel.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class ProductCache {

    @Autowired
    ProductRepository productRepository;

    LoadingCache<Long, Product> productLoadingCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(50)
            .build(new CacheLoader<Long, Product>() {
                @Override
                public Product load(Long id) {
                    return productRepository.findById(id).get();
                }
            });

    public Product getProduct(Long id) throws ExecutionException {
        return productLoadingCache.get(id);
    }

//    public void editProduct(Product product) throws ExecutionException {
//         productLoadingCache.put(product.getId(), product);
//    }

    public void emptyCache() {
        productLoadingCache.invalidateAll();
    }
}
