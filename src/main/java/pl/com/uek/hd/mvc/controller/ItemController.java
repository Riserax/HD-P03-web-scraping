package pl.com.uek.hd.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.com.uek.hd.mvc.service.ItemService;

import javax.websocket.server.PathParam;


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
}
