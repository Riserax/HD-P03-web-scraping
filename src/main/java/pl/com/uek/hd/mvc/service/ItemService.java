package pl.com.uek.hd.mvc.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface ItemService {
    public Iterable getExtractedItems();

    public Iterable getTransformedItems(int itemsAmount);

    public Iterable getTransformedAndLoad(int itemsAmount);

    public void saveItem(int itemsAmount);
}
