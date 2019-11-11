package pl.com.uek.hd.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/getTransformedAndLoad/{amount}")
    public Iterable getTransformedAndLoad(@PathVariable("amount") int amount){
        return itemService.getTransformedAndLoad(amount);
    }
}
