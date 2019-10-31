package pl.com.uek.hd.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BooksRepository extends CrudRepository<Book, Long> {

}
