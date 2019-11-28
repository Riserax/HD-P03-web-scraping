package pl.com.uek.hd.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
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
    @GetMapping(value = "/getSingleBookJSON/{id}", produces = "text/json")
    public void getSingleBookJSON(@PathVariable("id") long id, HttpServletResponse response){
        Item item = (Item)itemService.getItemById(id).get();
        String fileName = "item.json";

        response.setContentType("text/json");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");
        ObjectMapper om = new ObjectMapper();

        String jsonString = null;
        try {
            jsonString = om.writeValueAsString(item);
            response.getWriter().write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    @GetMapping(value = "/getSingleBookCSV/{id}", produces = "text/csv")
    public void getSingleBookCSV(@PathVariable("id") long bookId, HttpServletResponse response){
        String fileName = "item.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");


        try {
            StatefulBeanToCsv<Book> csvWriter = new StatefulBeanToCsvBuilder<Book>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .withOrderedResults(false)
                    .build();
            Item item = (Item)itemService.getItemById(bookId).get();
            csvWriter.write(item.getBook());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
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
