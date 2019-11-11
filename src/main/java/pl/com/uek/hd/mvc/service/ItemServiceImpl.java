package pl.com.uek.hd.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.uek.hd.mvc.model.Item;
import pl.com.uek.hd.mvc.repository.ItemRepository;
import pl.com.uek.hd.webscraper.Extractor;
import pl.com.uek.hd.webscraper.ItemCreator;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;
    String mainUrl = "https://helion.pl/";
    String categoriesBooks = "kategorie/ksiazki/";
    String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};
    Extractor extractor = new Extractor(mainUrl, categoriesBooks, booksUndercategories);
    ItemCreator itemCreator = new ItemCreator();

    public void saveItem(int itemsAmount){
        this.extractor.extract();
        this.itemCreator.setHtmlItems(this.extractor.getHtmlItems());
        List<Item> item = this.itemCreator.createItems(itemsAmount);
        System.out.println(item.size());
        itemRepository.save(item.get(1));
    }

    @Override
    public Iterable getExtractedItems() {
        this.extractor.extract();
        return this.extractor.getExtractedItemsNodes();
    }

    @Override
    public Iterable getTransformedItems(int itemsAmount) {
        this.extractor.extract();
        this.itemCreator.setHtmlItems(this.extractor.getHtmlItems());
        return this.itemCreator.createItems(itemsAmount);
    }

    @Override
    public Iterable getTransformedAndLoad(int itemsAmount) {
        this.extractor.extract();
        itemRepository.saveAll(this.itemCreator.createItems(itemsAmount));
        return itemRepository.findAll();
    }
}
