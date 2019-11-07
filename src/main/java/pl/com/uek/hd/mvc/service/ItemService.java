package pl.com.uek.hd.mvc.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface ItemService {
    public Iterable<String> getExtractedBooks();

    public Iterable<String> getTransformedBooks();

    public Iterable getTransformedAndLoad();

    public void saveItem();
}
