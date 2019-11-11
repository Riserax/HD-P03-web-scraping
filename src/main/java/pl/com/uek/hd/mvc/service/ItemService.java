package pl.com.uek.hd.mvc.service;

import org.springframework.stereotype.Service;
import pl.com.uek.hd.mvc.model.Item;

import java.util.ArrayList;

@Service
public interface ItemService {
    void reduceTransformedItems();

    Iterable getExtractedItems();

    Iterable getTransformedItems(int itemsAmount);

    Iterable getLoadedItems();

    Iterable getExtractedTransformedAndLoadedItems(int itemsAmount);

    void saveItem(int itemsAmount);

    void deleteAllItems();


}
