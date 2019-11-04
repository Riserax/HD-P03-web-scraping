package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transformator {
    private List<HtmlElement> htmlItems;
    private Set<Item> itemsPojo;
    private List<String> transformedItems;

    public Transformator(List<HtmlElement> htmlItems){
        this.htmlItems = htmlItems;
        this.itemsPojo = new HashSet<>();
        this.transformedItems = new ArrayList<>();
    }

    public void transform(){
//        try {
//            for (HtmlElement htmlItem : htmlItems) {
//                HtmlAnchor bookName = htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
//                HtmlAnchor bookAuthor = htmlItem.getFirstByXPath(".//p[@class='author']/a");
//                HtmlElement bookPrice = htmlItem.getFirstByXPath(".//p[@class='book-price']/span");
//                HtmlAnchor bookPublisher = htmlItem.getFirstByXPath(".//div[@class='select_druk']/dd/a");
//                HtmlElement bookPages = htmlItem.getFirstByXPath(".//dd[@class='select_druk select_bundle']");
//                HtmlElement bookCover = htmlItem.getFirstByXPath(".//dl[@class='info']/dd[last()]");
//                HtmlAnchor bookOrigTitle = htmlItem.getFirstByXPath(".//div[@id='section6']/dl/dd[1]/a");
//
//                boolean bookAvailable = !bookPrice.asText().equals("niedostępna");
//                String price = bookAvailable ? "0.0" : StringUtils.substring(bookPrice.asText(), 0, bookPrice.asText().length() - 3);
//
//                Book book = new Book();
//                book.setTitle(bookName.asText());
//                book.setAuthor(bookAuthor.asText());
//                book.setPublisher(bookPublisher.asText());
//                book.setNumberOfPages(new Integer(bookPages.asText()));
//                book.setCover(bookAvailable ? bookCover.asText() : null);
//                book.setOriginalTitle(bookOrigTitle.asText());
//
//                Item item = new Item();
//                item.setBook(book);
//                item.setBookPrice(new BigDecimal(price));
//                itemsPojo.add(item);
//
//                ObjectMapper om = new ObjectMapper();
//                String jsonString = om.writeValueAsString(item);
//                transformedItems.add(jsonString);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
