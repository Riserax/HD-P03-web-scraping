package pl.com.uek.hd.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.uek.hd.mvc.repository.BooksRepository;
import pl.com.uek.hd.webscraper.Extractor;
import pl.com.uek.hd.webscraper.Transformator;

import java.util.ArrayList;

@Service
public class BooksService {
    //TODO
    // ADD PARAMS TO EXTRACTOR, CHANGE GETEXTRACTEDBOOKS, SAVE ALL BOOKS
    @Autowired
    BooksRepository booksRepository;

    private String mainUrl = "https://helion.pl/";
    private String categoriesBooks = "kategorie/ksiazki/";
    private String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};

    private Extractor extractor = new Extractor(mainUrl, categoriesBooks, booksUndercategories);

    private Transformator transformator = new Transformator(extractor.getHtmlItems());

    public Iterable<String> getExtractedBooks(){
        return new ArrayList<>(extractor.getExtractedItemsNodes());
    }

    public Iterable<String> getTransformedBooks(){
        return new ArrayList<>(transformator.getTransformedItems());
    }

    public void getTransformedAndLoad(){

        booksRepository.saveAll(transformator.getItemsPojo());
    }
}
