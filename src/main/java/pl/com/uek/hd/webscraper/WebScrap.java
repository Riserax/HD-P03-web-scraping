package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.css.StyleElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
//@Component
public class WebScrap {
    public static void main(String[] args) {
        helionExample();
    }

    private static void helionExample() {
        String mainUrl = "https://helion.pl/";
        String categoriesBooks = "kategorie/ksiazki/";
        String[] booksUndercategories = {"programowanie","bazy-danych","elektronika"};
//        Extractor extractor = new Extractor(mainUrl, categoriesBooks, booksUndercategories);
//        extractor.extract();

        try {
            WebClient wc = new WebClient();
            wc.getOptions().setCssEnabled(false);
            wc.getOptions().setJavaScriptEnabled(false);
            String finalUrl = mainUrl + categoriesBooks + booksUndercategories[0];
            HtmlPage htmlPage = wc.getPage(finalUrl);
            List<HtmlElement> items = htmlPage.getByXPath("//li[@class='classPresale']");
            if (!items.isEmpty()) {
                int count = 0;
                for (HtmlElement htmlItem : items) {
                    if (count < 15) {
                        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                        String itemUrl = "https:" + itemAnchor.getHrefAttribute();
                        HtmlPage itemHtmlPage = wc.getPage(itemUrl);

                        HtmlElement bookTitle = itemHtmlPage.getFirstByXPath(".//div[@class='title-group']/h1/span[1]");
                        HtmlAnchor bookAuthor = itemHtmlPage.getFirstByXPath(".//dl[@class='author']/dd/a");
                        HtmlAnchor bookPublisher = itemHtmlPage.getFirstByXPath(".//div[@class='select_druk']/dd/a");
                        HtmlElement bookPages = itemHtmlPage.getFirstByXPath(".//dl[@class='info']/dd[2]");
                        HtmlElement bookCover = itemHtmlPage.getFirstByXPath(".//dl[@class='info']/dd[last()-1]");
                        HtmlAnchor bookOrigTitle = itemHtmlPage.getFirstByXPath(".//div[@id='section6']/dl/dd[1]/a");
                        // TODO book details

                        HtmlElement bookPriceRegular = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/span");
                        HtmlElement bookPriceOld = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/del");
                        HtmlElement bookPriceSale = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/ins");

                        HtmlElement ebookPriceRegular = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/span");
                        HtmlElement ebookPriceOld = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/del");
                        HtmlElement ebookPriceSale = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/ins");

                        List<HtmlElement> itemTags = itemHtmlPage.getByXPath("//ul[@class='tags']/li");
                        List<String> tagsList = new ArrayList<>();
                        for (HtmlElement itemTag : itemTags) {
                            if (!tagsList.toString().contains(itemTag.asText())) {
                                if (itemTag.getAttribute("class").contains("promotion"))
                                    tagsList.add(StringUtils.substring(itemTag.asText(), 0, 8));
                                else
                                    tagsList.add(itemTag.asText());
                            }
                        }

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

                        Book book = new Book();
                        book.setTitle(bookTitle.asText());
                        book.setAuthor(bookAuthor.asText());
                        book.setPublisher(bookPublisher.asText());
                        book.setNumberOfPages(bookPages == null ? null : new Integer(bookPages.asText()));
                        book.setCover(bookAvailable ? bookCover.asText() : null);
                        book.setOriginalTitle(bookOrigTitle == null ? null : bookOrigTitle.asText());

                        Item item = new Item();
                        item.setBook(book);
                        item.setBookAvailable(bookAvailable);
                        item.setBookPriceOld(bookSale ? new BigDecimal(bPriceOld) : null);
                        item.setBookPrice(bookAvailable ? new BigDecimal(bPrice) : null);
                        item.setEBookAvailable(ebookAvailable);
                        item.setEBookPriceOld(ebookAvailable && ebookSale ? new BigDecimal(ebPriceOld) : null);
                        item.setEBookPrice(ebookAvailable ? new BigDecimal(ebPrice) : null);
                        item.setTags(tagsList.isEmpty() ? null : tagsList);

                        ObjectMapper om = new ObjectMapper();
                        String jsonString = om.writeValueAsString(item);

                        System.out.println(jsonString);
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}