package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import lombok.Getter;
//import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@Setter
//@Getter
public class Extractor {
    private WebClient webClient;
    private String mainUrl;
    private String categoriesBooks;
    private String[] booksUnderCategories;
    private String finalUrl;
    private List<HtmlElement> htmlItems;
    private List<String> extractedItemsNodes;


    private String createFinalUrl(){
        return this.finalUrl = mainUrl + categoriesBooks + booksUnderCategories[0];
    }

    public void extract(){
        webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        try {
            HtmlPage htmlPage = webClient.getPage(createFinalUrl());
            htmlItems = htmlPage.getByXPath("//li[@class='classPresale']");
            if (!htmlItems.isEmpty()) {
                for (HtmlElement htmlItem : htmlItems) {
                    System.out.println(htmlItem.asXml());
                    TemporaryItem temporaryItem = new TemporaryItem();
                    temporaryItem.setNode(htmlItem.asXml());

                    ObjectMapper objectMapper = new ObjectMapper();

                    extractedItemsNodes.add(objectMapper.writeValueAsString(temporaryItem));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printExtractedBooks(){
        for(String node : extractedItemsNodes){
            System.out.println(node);
        }

    }

    public Extractor(String mainUrl, String categoriesBooks, String[] booksUnderCategories){
        this.mainUrl = mainUrl;
        this.categoriesBooks = categoriesBooks;
        this.booksUnderCategories = booksUnderCategories;
        this.htmlItems = new ArrayList<>();
        this.extractedItemsNodes = new ArrayList<>();

    }

    private class TemporaryItem{
        private String node;

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }
    }

}
