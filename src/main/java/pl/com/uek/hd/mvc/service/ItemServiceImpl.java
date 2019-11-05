package pl.com.uek.hd.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.uek.hd.mvc.repository.ItemRepository;
import pl.com.uek.hd.webscraper.Extractor;
import pl.com.uek.hd.webscraper.TransformatorImpl;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    Extractor extractor = new Extractor("hwd");
    TransformatorImpl transformatorImpl = new TransformatorImpl(extractor.getHtmlItems());

    @Override
    public Iterable<String> getExtractedBooks() {
        return extractor.getExtractedItemsNodes();
    }

    @Override
    public Iterable<String> getTransformedBooks() {
        return transformatorImpl.getTransformedItems();
    }

    @Override
    public Iterable getTransformedAndLoad() {
        itemRepository.saveAll(transformatorImpl.getItemsPojo());
        return getTransformedBooks();
    }
}
