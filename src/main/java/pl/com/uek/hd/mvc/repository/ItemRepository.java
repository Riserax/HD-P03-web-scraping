package pl.com.uek.hd.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.uek.hd.mvc.model.Book;
import pl.com.uek.hd.mvc.model.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}
