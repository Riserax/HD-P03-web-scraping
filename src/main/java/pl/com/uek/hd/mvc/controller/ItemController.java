package pl.com.uek.hd.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.uek.hd.mvc.service.ItemService;


@RestController
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping
    public void saveItem(){
        itemService.saveItem();
    }

    @GetMapping("/getExtractedBooks")
    public Iterable getExtractedBooks(){
        return itemService.getExtractedBooks();
    }

    @GetMapping("/getTransformedBooks")
    public Iterable getTransformedBooks(){
        return itemService.getTransformedBooks();
    }

    @GetMapping("/getTransformedAndLoad")
    public Iterable getTransformedAndLoad(){
        return itemService.getTransformedAndLoad();
    }
}
