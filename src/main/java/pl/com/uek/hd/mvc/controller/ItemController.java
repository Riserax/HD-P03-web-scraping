package pl.com.uek.hd.mvc.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import pl.com.uek.hd.mvc.model.Book;
import pl.com.uek.hd.mvc.model.Item;
import pl.com.uek.hd.mvc.service.ItemService;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;


@RestController
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/saveItem/{amount}")
    public void saveItem(@PathVariable("amount") int amount){
        itemService.saveItem(amount);
    }

    @GetMapping("/getExtractedItems")
    public Iterable getExtractedBooks(){
        return itemService.getExtractedItems();
    }

    @GetMapping("/getTransformedItems/{amount}")
    public Iterable getTransformedBooks(@PathVariable("amount") int amount){
        return itemService.getTransformedItems(amount);
    }

    @GetMapping("/getLoadedItems")
    public Iterable getLoadedItems(){
        return itemService.getLoadedItems();
    }


    @GetMapping("/getExtractedTransformedAndLoadedItems/{amount}")
    public Iterable getExtractedTransformedAndLoadedItems(@PathVariable("amount") int amount){
        return itemService.getExtractedTransformedAndLoadedItems(amount);
    }

    @GetMapping("/deleteAllItems")
    public void deleteAllItems(){
        itemService.deleteAllItems();
    }

    @GetMapping(value = "/getBooksCSV", produces = "text/csv")
    public void getBooksCSV(HttpServletResponse response){
        String fileName = "items.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");



        try {
            StatefulBeanToCsv<Item> csvWriter = new StatefulBeanToCsvBuilder<Item>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .withOrderedResults(false)
                    .build();
            csvWriter.write((List)itemService.getBooks());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }


    }
}
