package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.util.List;

public class WebScrap {
    public static void main(String[] args) {
        helionExample();
    }

    private static void helionExample() {
        String mainUrl = "https://helion.pl/";
        String categoriesBooks = "kategorie/ksiazki/";
        String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};
        String finalUrl = mainUrl + categoriesBooks + booksUndercategories[0];

        WebClient wc = new WebClient();
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setJavaScriptEnabled(false);

        try {
            HtmlPage htmlPage = wc.getPage(finalUrl);

            List<HtmlElement> items = htmlPage.getByXPath("//li[@class='classPresale']");
            if (!items.isEmpty()) {
                for (HtmlElement htmlItem : items) {
                    HtmlAnchor itemAnchor = (HtmlAnchor) htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                    HtmlAnchor itemAuthor = (HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='author']/a");
                    HtmlElement spanPrice = (HtmlElement) htmlItem.getFirstByXPath(".//p[@class='price price-add']/a/span");

                    String itemPrice = spanPrice.asText().replace(" zł", "");

                    Item item = new Item();
                    item.setName(itemAnchor.asText());
                    item.setAuthor(itemAuthor.asText());
                    item.setPrice(new BigDecimal(itemPrice));
                    item.setUrl(itemAnchor.getHrefAttribute());

                    ObjectMapper om = new ObjectMapper();
                    String jsonString = om.writeValueAsString(item);

                    System.out.println(jsonString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // example
    private static void newyorkCraiglistExample() {
        String searchQuery = "Iphone 6";
        String baseUrl = "https://newyork.craigslist.org/";
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = baseUrl + "search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery, "UTF-8");
            HtmlPage htmlPage = webClient.getPage(searchUrl);

            List<HtmlElement> items = htmlPage.getByXPath("//li[@class='result-row']");
            if (!items.isEmpty()) {
                for (HtmlElement htmlItem : items) {
                    HtmlAnchor itemAnchor = (HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a");
                    HtmlElement spanPrice = (HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']");

                    String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

                    Item item = new Item();
                    item.setName(itemAnchor.asText());
                    item.setPrice(new BigDecimal(itemPrice.replace("$", "")));
                    item.setUrl(baseUrl + itemAnchor.getHrefAttribute());

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writeValueAsString(item);

                    System.out.println(jsonString);
                }
            } else {
                System.out.println("No items found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}