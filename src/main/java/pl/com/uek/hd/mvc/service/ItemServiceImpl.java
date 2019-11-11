package pl.com.uek.hd.mvc.service;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.uek.hd.mvc.model.Item;
import pl.com.uek.hd.mvc.repository.ItemRepository;
import pl.com.uek.hd.webscraper.Extractor;
import pl.com.uek.hd.webscraper.ItemCreator;

import java.sql.SQLIntegrityConstraintViolationException;
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
    List<String> extractedItemsNodes = new ArrayList<>();
    List<HtmlElement> extractedItems = new ArrayList<>();
    List<Item> transformedItems = new ArrayList<>();
    ItemCreator itemCreator = new ItemCreator();

    public void saveItem(int itemsAmount){
        this.extractor.extract();
        this.itemCreator.setHtmlItems(this.extractor.getHtmlItems());
        List<Item> item = this.itemCreator.createItems(itemsAmount);
        System.out.println(item.size());
        itemRepository.save(item.get(1));
    }

    @Override
    public void deleteAllItems() {
        itemRepository.deleteAll();
    }


    //todo zrobic, tak zeby usuwalo te itemy, ktore juz sa w BD, jak narazie nie dziala
    @Override
    public void reduceTransformedItems() {
        Iterable<Item> itemsInDB = itemRepository.findAll();
        for(Item itemInDb : transformedItems) {
            System.out.println(itemInDb.getBook().getISBN());
            for (Item transformedItem : itemsInDB) {
                System.out.println(transformedItem.getBook().getISBN());
                if (itemInDb.getBook().getISBN() == transformedItem.getBook().getISBN()) {
                    this.transformedItems.remove(transformedItem.getItemId());
                    System.out.println("rozmiar: " + this.transformedItems.size());
                }
            }
        }
    }

    @Override
    public Iterable getExtractedItems() {
        this.extractor.extract();
        this.extractedItemsNodes = this.extractor.getExtractedItemsNodes();
        this.extractedItems = this.extractor.getHtmlItems();
        return extractedItemsNodes;
    }

    @Override
    public Iterable getTransformedItems(int itemsAmount) {
        this.itemCreator.setHtmlItems(this.extractedItems);
        this.transformedItems = this.itemCreator.createItems(itemsAmount);
        return transformedItems;
    }

    @Override
    public Iterable getLoadedItems() {
        reduceTransformedItems();
        itemRepository.saveAll(transformedItems);
        return itemRepository.findAll();
    }

    @Override
    public Iterable getExtractedTransformedAndLoadedItems(int itemsAmount) {
        this.extractor.extract();
        this.extractedItemsNodes = this.extractor.getExtractedItemsNodes();
        this.extractedItems = this.extractor.getHtmlItems();
        this.itemCreator.setHtmlItems(this.extractedItems);
        this.transformedItems = this.itemCreator.createItems(itemsAmount);
        itemRepository.saveAll(transformedItems);
        return itemRepository.findAll();
    }


}
