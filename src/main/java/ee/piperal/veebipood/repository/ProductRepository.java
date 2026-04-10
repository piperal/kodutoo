package ee.piperal.veebipood.repository;
import ee.piperal.veebipood.entity.Product;
import jdk.jfr.Category;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//repository -> andmebaasiga suhtlemiseks

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findAllByCategoryId(Pageable pageable, Long categoryId);
}
