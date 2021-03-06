package pl.com.uek.hd.mvc.service;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.uek.hd.mvc.model.Book;
import pl.com.uek.hd.mvc.model.Item;
import pl.com.uek.hd.mvc.repository.ItemRepository;
import pl.com.uek.hd.webscraper.Extractor;
import pl.com.uek.hd.webscraper.ItemCreator;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public void deleteAllItems() {
        itemRepository.deleteAll();
    }

    @Override
    public Optional<Item> getItemById(long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Iterable getBooks() {
        List<Book> books = new ArrayList<>();
        Iterable<Item> items = itemRepository.findAll();
        for (Item item : items){
            books.add(item.getBook());
        }

        return books;
    }

    @Override
    public Iterable getExtractedItems() {
        if(extractedItemsNodes.isEmpty()){
            this.extractor.extract();
            this.extractedItemsNodes.addAll(this.extractor.getExtractedItemsNodes());
            this.extractedItems.addAll(this.extractor.getHtmlItems());
        }
        return extractedItemsNodes;
    }

    @Override
    public Iterable getTransformedItems(int itemsAmount) {
        this.itemCreator.setHtmlItems(this.extractedItems);
        this.transformedItems.addAll(this.itemCreator.createItems(itemsAmount));
        return transformedItems;
    }

    @Override
    public Iterable getLoadedItems() {
        itemRepository.saveAll(transformedItems);
        this.extractedItemsNodes.removeAll(extractedItemsNodes);
        this.extractedItems.removeAll(extractedItems);
        this.transformedItems.removeAll(transformedItems);
        return itemRepository.findAll();
    }

    @Override
    public Iterable getExtractedTransformedAndLoadedItems(int itemsAmount) {
        this.extractor.extract();
        this.extractedItemsNodes.addAll(this.extractor.getExtractedItemsNodes());
        this.extractedItems.addAll(this.extractor.getHtmlItems());
        this.itemCreator.setHtmlItems(this.extractedItems);
        this.transformedItems = this.itemCreator.createItems(itemsAmount);
        itemRepository.saveAll(transformedItems);
        this.extractedItemsNodes.removeAll(extractedItemsNodes);
        this.extractedItems.removeAll(extractedItems);
        this.transformedItems.removeAll(transformedItems);
        return itemRepository.findAll();
    }

}
