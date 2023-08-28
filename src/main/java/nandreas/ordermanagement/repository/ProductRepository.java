package nandreas.ordermanagement.repository;

import nandreas.ordermanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>
{
    List<Product> findBySkuIn(List<String> skuList);
}
