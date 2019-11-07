package pl.com.uek.hd.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.uek.hd.mvc.model.Item;
import pl.com.uek.hd.mvc.repository.ItemRepository;
import pl.com.uek.hd.webscraper.Extractor;
import pl.com.uek.hd.webscraper.ItemCreator;
import pl.com.uek.hd.webscraper.TransformatorImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    public void saveItem(){
        String mainUrl = "https://helion.pl/";
        String categoriesBooks = "kategorie/ksiazki/";
        String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};
        Extractor extractor = new Extractor(mainUrl, categoriesBooks, booksUndercategories);
        extractor.extract();

        ItemCreator itemCreator = new ItemCreator();
        itemCreator.setHtmlItems(extractor.getHtmlItems());
        List<Item> item = itemCreator.createItem();
        System.out.println(item.size());
        itemRepository.save(item.get(1));
    }

    @Override
    public Iterable<String> getExtractedBooks() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<String> getTransformedBooks() {
        return new ArrayList<>();
    }

    @Override
    public Iterable getTransformedAndLoad() {
        return new ArrayList<>();
    }
}
