package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import pl.com.uek.hd.mvc.model.Book;
import pl.com.uek.hd.mvc.model.Item;
import pl.com.uek.hd.mvc.model.Opinion;
import pl.com.uek.hd.mvc.model.Review;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemCreator implements Serializable {
    private List<HtmlElement> htmlItems;


    public List<HtmlElement> getHtmlItems() {
        return htmlItems;
    }

    public void setHtmlItems(List<HtmlElement> htmlItems) {
        this.htmlItems = htmlItems;
    }

    public List<Item> createItems(int itemsAmount) {
        try {
            List<Item> items = new ArrayList<>();
            WebClient wc = new WebClient();
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setJavaScriptEnabled(false);

            if (!htmlItems.isEmpty()) {
                int count = 0;
                for (HtmlElement htmlItem : htmlItems) {
                    if (count < itemsAmount) {
                        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                        String itemUrl = "https:" + itemAnchor.getHrefAttribute();
                        HtmlPage itemHtmlPage = wc.getPage(itemUrl);

                        // book and eBook prices
                        HtmlElement bookPriceRegular = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/span");
                        HtmlElement bookPriceOld = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/del");
                        HtmlElement bookPriceSale = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/ins");
                        HtmlElement ebookPriceRegular = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/span");
                        HtmlElement ebookPriceOld = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/del");
                        HtmlElement ebookPriceSale = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/ins");

                        // ITEM
                        List<HtmlElement> itemTags = itemHtmlPage.getByXPath("//ul[@class='tags']/li");
                        HtmlElement itemDescription = itemHtmlPage.getFirstByXPath(".//div[@class='book-description']/div[@class='text']");
                        HtmlElement itemAboutAuthor = itemHtmlPage.getFirstByXPath(".//div[@class='author-info-book-page']");
                        HtmlElement itemOpinionsNumber = itemHtmlPage.getFirstByXPath(".//h2[@class='votes-header-two']/span");
                        HtmlElement itemOverallRate = itemHtmlPage.getFirstByXPath(".//span[@class='ocena']");
                        List<HtmlElement> itemRates = itemHtmlPage.getByXPath("//ul[@class='oceny']/li");
                        HtmlElement reviewsNumber = itemHtmlPage.getFirstByXPath(".//div[@id='section5']/h2");


                        boolean bookSale = bookPriceOld != null && bookPriceSale != null;
                        boolean bookAvailable = (bookPriceRegular != null && !bookPriceRegular.asText().equals("niedostępna"))
                                || bookSale;

                        String bPriceOld = null;
                        String bPrice = null;
                        if (bookAvailable) {
                            if (bookSale) {
                                bPriceOld = StringUtils.substring(bookPriceOld.asText(), 0, bookPriceOld.asText().length() - 3)
                                        .replace(",", ".");
                                bPrice = StringUtils.substring(bookPriceSale.asText(), 0, bookPriceSale.asText().length() - 3)
                                        .replace(",", ".");
                            } else {
                                bPrice = StringUtils.substring(bookPriceRegular.asText(), 0, bookPriceRegular.asText().length() - 3)
                                        .replace(",", ".");
                            }
                        }

                        boolean ebookSale = ebookPriceOld != null && ebookPriceSale != null;
                        boolean ebookAvailable = ebookPriceRegular != null || ebookSale;

                        String ebPriceOld = null;
                        String ebPrice = null;
                        if (ebookAvailable) {
                            if (ebookSale) {
                                ebPriceOld = StringUtils.substring(ebookPriceOld.asText(), 0, ebookPriceOld.asText().length() - 3)
                                        .replace(",", ".");
                                ebPrice = StringUtils.substring(ebookPriceSale.asText(), 0, ebookPriceSale.asText().length() - 3)
                                        .replace(",", ".");
                            } else {
                                ebPrice = StringUtils.substring(ebookPriceRegular.asText(), 0, ebookPriceRegular.asText().length() - 3)
                                        .replace(",", ".");
                            }
                        }

                        List<String> tagsList = new ArrayList<>();
                        for (HtmlElement itemTag : itemTags) {
                            if (!tagsList.toString().contains(itemTag.asText())) {
                                if (itemTag.getAttribute("class").contains("promotion"))
                                    tagsList.add(StringUtils.substring(itemTag.asText(), 0, 8));
                                else
                                    tagsList.add(itemTag.asText());
                            }
                        }

                        boolean opinionsAvailable = itemOpinionsNumber != null;

                        List<String> ratesList = new ArrayList<>();
                        for (HtmlElement itemRate : itemRates) {
                            ratesList.add(itemRate.asText().replace("\r\n", " - "));
                        }

                        TransformatorFactory tf = new TransformatorFactory();

                        // BOOK z factory
                        Book book = (Book) tf.createTransformator("Book").transform(htmlItem);

                        // OPINION z factory
                        List<Opinion> opinionsList = (List<Opinion>) tf.createTransformator("Opinion").transform(htmlItem);


                        // REVIEW z factory
                        List<Review> reviewsList = (List<Review>) tf.createTransformator("Review").transform(htmlItem);


                        // ITEM
                        Item item = new Item();
                        item.setItemId(book.getISBN());
                        item.setBook(book);
                        item.setBookAvailable(bookAvailable);
                        item.setBookPriceOld(bookSale ? new BigDecimal(bPriceOld) : null);
                        item.setBookPrice(bookAvailable ? new BigDecimal(bPrice) : null);
                        item.seteBookAvailable(ebookAvailable);
                        item.seteBookPriceOld(ebookAvailable && ebookSale ? new BigDecimal(ebPriceOld) : null);
                        item.seteBookPrice(ebookAvailable ? new BigDecimal(ebPrice) : null);
                        item.setTags(tagsList.isEmpty() ? null : tagsList);
                        item.setDescription(itemDescription == null ? null :
                                itemDescription.asText().replace("\r\n"," ")
                                        .replace("\r"," ").replace("\n"," "));
                        item.setAboutAuthor(itemAboutAuthor == null ? null : itemAboutAuthor.asText());
                        item.setOpinionsNumber(!opinionsAvailable ? null : new Integer(StringUtils.substring(itemOpinionsNumber.asText(),
                                1,(itemOpinionsNumber.asText().length()-1))));
                        item.setOverallRate(itemOverallRate == null ? null : new BigDecimal(itemOverallRate.asText()));
                        item.setRates(ratesList.isEmpty() ? null : ratesList);
                        item.setOpinions(opinionsList);
                        item.setReviewsNumber(reviewsNumber == null ? null : new Integer(reviewsNumber.asText().replaceAll("\\D+","")));
                        item.setReviews(reviewsList);

                        items.add(item);
                        count++;

                    }
                }
                System.out.println(items.size());
                try {
                    ObjectMapper om = new ObjectMapper();
                    FileWriter file = new FileWriter("fiveItems.json");
                    for(Item item : items){
                        String jsonString = om.writeValueAsString(item);
                        file.append(jsonString + "\n");
                        System.out.println("Successfully Copied JSON Object to File...");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return items;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();}
}
