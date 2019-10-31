package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
@Component
public class WebScrap {
    public static void main(String[] args) {
        helionExample();
    }

    private static void helionExample() {
        String mainUrl = "https://helion.pl/";
        String categoriesBooks = "kategorie/ksiazki/";
        String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};
        Extractor extractor = new Extractor(mainUrl, categoriesBooks, booksUndercategories);

        extractor.extract();
//        try {
//
//            HtmlPage htmlPage = wc.getPage(finalUrl);
//            List<HtmlElement> items = htmlPage.getByXPath("//li[@class='classPresale']");
//            if (!items.isEmpty()) {
//                for (HtmlElement htmlItem : items) {
//                    System.out.println(htmlItem.asXml());
//
//                    HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
//                    HtmlAnchor itemAuthor = htmlItem.getFirstByXPath(".//p[@class='author']/a");
//                    HtmlElement spanPrice = htmlItem.getFirstByXPath(".//p[@class='price price-add']/a/span");
//
//                    String itemPrice = spanPrice == null ? "0.0" : StringUtils.substring(spanPrice.asText(),0,spanPrice.asText().length()-3);
//
//                    Item item = new Item();
//                    item.setName(itemAnchor.asText());
//                    item.setAuthor(itemAuthor.asText());
//                    item.setPrice(new BigDecimal(itemPrice));
//                    item.setUrl(itemAnchor.getHrefAttribute());
//
//                    ObjectMapper om = new ObjectMapper();
//                    String jsonString = om.writeValueAsString(item);
//
//                    System.out.println(jsonString);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}