package pl.com.uek.hd.mvc.service;

import org.springframework.stereotype.Service;
import pl.com.uek.hd.mvc.model.Item;

import java.util.ArrayList;
import java.util.Optional;

@Service
public interface ItemService {

    Optional getItemById(long id);

    Iterable getBooks();

    Iterable getExtractedItems();

    Iterable getTransformedItems(int itemsAmount);

    Iterable getLoadedItems();

    Iterable getExtractedTransformedAndLoadedItems(int itemsAmount);

    void deleteAllItems();


}
