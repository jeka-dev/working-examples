package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "coffeeshops", path = "coffeeshops")
public interface CoffeeShopRepository extends CrudRepository<CoffeeShopModel, Long> {
}