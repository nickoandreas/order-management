package nandreas.ordermanagement.repository;

import nandreas.ordermanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>
{
    Inventory findFirstByProductSku(String sku);
}
