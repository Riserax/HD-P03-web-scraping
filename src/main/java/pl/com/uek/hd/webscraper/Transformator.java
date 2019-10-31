package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Transformator {
    private List<HtmlElement> htmlItems;
    private Set<Item> itemsPojo;
    private List<String> transformedItems;

    public Transformator(List<HtmlElement> htmlItems){
        this.htmlItems = htmlItems;
        this.itemsPojo = new HashSet<>();
        this.transformedItems = new ArrayList<>();
    }


    public void printTransformedItems(){
        for(String node : transformedItems){
            System.out.println(node);
        }

    }

    public void transform(){
        try {
            for (HtmlElement htmlItem : htmlItems) {
                HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                HtmlAnchor itemAuthor = htmlItem.getFirstByXPath(".//p[@class='author']/a");
                HtmlElement spanPrice = htmlItem.getFirstByXPath(".//p[@class='price price-add']/a/span");

                String itemPrice = spanPrice == null ? "0.0" : StringUtils.substring(spanPrice.asText(), 0, spanPrice.asText().length() - 3);

                Item item = new Item();
                item.setName(itemAnchor.asText());
                item.setAuthor(itemAuthor.asText());
                item.setPrice(new BigDecimal(itemPrice));
                item.setUrl(itemAnchor.getHrefAttribute());
                itemsPojo.add(item);

                ObjectMapper om = new ObjectMapper();
                String jsonString = om.writeValueAsString(item);
                transformedItems.add(jsonString);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
