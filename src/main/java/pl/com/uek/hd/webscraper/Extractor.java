package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Getter;
import lombok.Setter;
//import lombok.Getter;
//import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Extractor {
    private WebClient webClient;
    private String mainUrl;
    private String categoriesBooks;
    private String[] booksUnderCategories;
    private String finalUrl;
    private List<HtmlElement> htmlItems;
    private List<String> extractedItemsNodes;

    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public String getCategoriesBooks() {
        return categoriesBooks;
    }

    public void setCategoriesBooks(String categoriesBooks) {
        this.categoriesBooks = categoriesBooks;
    }

    public String[] getBooksUnderCategories() {
        return booksUnderCategories;
    }

    public void setBooksUnderCategories(String[] booksUnderCategories) {
        this.booksUnderCategories = booksUnderCategories;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }

    public List<HtmlElement> getHtmlItems() {
        return htmlItems;
    }

    public void setHtmlItems(List<HtmlElement> htmlItems) {
        this.htmlItems = htmlItems;
    }

    public List<String> getExtractedItemsNodes() {
        return extractedItemsNodes;
    }

    public void setExtractedItemsNodes(List<String> extractedItemsNodes) {
        this.extractedItemsNodes = extractedItemsNodes;
    }

    private String createFinalUrl(){
        return this.finalUrl = mainUrl + categoriesBooks + booksUnderCategories[0];
    }

    public void extract(){


        try {
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

    private void initializeWebClient(){
        webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            HtmlPage htmlPage = webClient.getPage(finalUrl);
            htmlItems = htmlPage.getByXPath("//li[@class='classPresale']");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Extractor(String mainUrl, String categoriesBooks, String[] booksUnderCategories){
        this.mainUrl = mainUrl;
        this.categoriesBooks = categoriesBooks;
        this.booksUnderCategories = booksUnderCategories;
        createFinalUrl();
        this.htmlItems = new ArrayList<>();
        this.extractedItemsNodes = new ArrayList<>();
        initializeWebClient();

    }

    public Extractor(String finalUrl){

        this.finalUrl = finalUrl;
        this.htmlItems = new ArrayList<>();
        this.extractedItemsNodes = new ArrayList<>();
        initializeWebClient();

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
