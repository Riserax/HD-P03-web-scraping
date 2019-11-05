package pl.com.uek.hd.webscraper;

import pl.com.uek.hd.mvc.model.Item;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class WebScrapObjVer {
    public static void main(String[] args){
        String mainUrl = "https://helion.pl/";
        String categoriesBooks = "kategorie/ksiazki/";
        String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};
        Extractor extractor = new Extractor(mainUrl, categoriesBooks, booksUndercategories);
        extractor.extract();



        ItemCreator itemCreator = new ItemCreator();
        itemCreator.setHtmlItems(extractor.getHtmlItems());
        List<Item> item = itemCreator.createItem();


    }
}
