package pl.com.uek.hd.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.uek.hd.mvc.service.BooksService;

@RestController
public class BooksController {
    @Autowired
    BooksService booksService;

    @GetMapping("/getExtractedBooks")
    public Iterable getExtractedBooks(){
        return booksService.getExtractedBooks();
    }

    @GetMapping("/getTransformedBooks")
    public Iterable getTransformedBooks(){
        return booksService.getTransformedBooks();
    }

    @GetMapping("/getTransformedAndLoad")
    public Iterable getTransformedAndLoad(){
        return booksService.getTransformedAndLoad();
    }
}
