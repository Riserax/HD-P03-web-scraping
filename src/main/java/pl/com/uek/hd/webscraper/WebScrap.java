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
//import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                int count = 0; // TODO zrobić na parametr do wyboru użytkownikowi
                for (HtmlElement htmlItem : items) {
                    if (count < 15) {
                        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                        String itemUrl = "https:" + itemAnchor.getHrefAttribute();
                        HtmlPage itemHtmlPage = wc.getPage(itemUrl);

                        //BOOK
                        HtmlElement bookTitle = itemHtmlPage.getFirstByXPath(".//div[@class='title-group']/h1/span[1]");
                        HtmlAnchor bookAuthor = itemHtmlPage.getFirstByXPath(".//dl[@class='author']/dd/a");
                        HtmlAnchor bookPublisher = itemHtmlPage.getFirstByXPath(".//div[@class='select_druk']/dd/a");
                        HtmlElement bookPages = itemHtmlPage.getFirstByXPath(".//dl[@class='info']/dd[2]");
                        HtmlElement bookCover = itemHtmlPage.getFirstByXPath(".//dl[@class='info']/dd[last()-1]");
                        List<HtmlElement> detailsTitles = itemHtmlPage.getByXPath("//div[@id='section6']/dl/dt");
                        List<HtmlElement> detailsTexts = itemHtmlPage.getByXPath("//div[@id='section6']/dl/dd");

                        // book and eBook prices
                        HtmlElement bookPriceRegular = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/span");
                        HtmlElement bookPriceOld = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/del");
                        HtmlElement bookPriceSale = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_druk']/p[@class='book-price']/ins");
                        HtmlElement ebookPriceRegular = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/span");
                        HtmlElement ebookPriceOld = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/del");
                        HtmlElement ebookPriceSale = itemHtmlPage.getFirstByXPath(".//fieldset[@id='box_ebook']/p[@class='book-price']/ins");

                        // OPINION
                        List<HtmlElement> itemOpinions = itemHtmlPage.getByXPath("//div[@id='section4']/div[@class='list']/ol[@class='list']/li");
                        // REVIEW
                        List<HtmlElement> itemReviews = itemHtmlPage.getByXPath("//div[@id='section5']/ol[@class='list']/li");
                        HtmlElement reviewsNumber = itemHtmlPage.getFirstByXPath(".//div[@id='section5']/h2");

                        // ITEM
                        List<HtmlElement> itemTags = itemHtmlPage.getByXPath("//ul[@class='tags']/li");
                        HtmlElement itemDescription = itemHtmlPage.getFirstByXPath(".//div[@class='book-description']/div[@class='text']");
                        HtmlElement itemAboutAuthor = itemHtmlPage.getFirstByXPath(".//div[@class='author-info-book-page']");
                        HtmlElement itemOpinionsNumber = itemHtmlPage.getFirstByXPath(".//h2[@class='votes-header-two']/span");
                        HtmlElement itemOverallRate = itemHtmlPage.getFirstByXPath(".//span[@class='ocena']");
                        List<HtmlElement> itemRates = itemHtmlPage.getByXPath("//ul[@class='oceny']/li");

                        // book details
                        Map<Integer,String> detTitles = new HashMap<>();
                        for (int i = 0; i < detailsTitles.size(); i++) {
                            detTitles.put(i, detailsTitles.get(i).asText());
                        }

                        Map<Integer,String> detTexts = new HashMap<>();
                        for (int i = 0; i < detailsTexts.size(); i++) {
                            detTexts.put(i, detailsTexts.get(i).asText());
                        }

                        String bookOrigTitle = null;
                        String bookTranslator = null;
                        String bookISBN = null;
                        String bookPublishDate = null;
                        String bookFormat = null;
                        String bookCatalogNr = null;

                        for (int i = 0; i < detTitles.size(); i++) {
                            if (detTexts.containsKey(i)) {
                                switch (detTitles.get(i)) {
                                    case "Tytuł oryginału:":
                                        bookOrigTitle = detTexts.get(i);
                                        break;
                                    case "Tłumaczenie:":
                                        bookTranslator = detTexts.get(i);
                                        break;
                                    case "ISBN Książki drukowanej:":
                                        bookISBN = detTexts.get(i);
                                        break;
                                    case "Data wydania książki drukowanej:":
                                        bookPublishDate = detTexts.get(i);
                                        break;
                                    case "Format:":
                                        bookFormat = detTexts.get(i);
                                        break;
                                    case "Numer z katalogu:":
                                        bookCatalogNr = detTexts.get(i);
                                        break;
                                }
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

                        // BOOK
                        Book book = new Book();
                        book.setTitle(bookTitle.asText());
                        book.setAuthor(bookAuthor.asText());
                        book.setPublisher(bookPublisher.asText());
                        book.setNumberOfPages(bookPages == null ? null : new Integer(bookPages.asText()));
                        book.setCover(bookAvailable ? bookCover.asText() : null);
                        book.setOriginalTitle(bookOrigTitle);
                        book.setTranslator(bookTranslator);
                        book.setISBN(bookISBN);
                        book.setPublishingDate(bookPublishDate);
                        book.setFormat(bookFormat);
                        book.setCatalogNumber(bookCatalogNr);

                        // OPINION
                        List<Opinion> opinionsList = new ArrayList<>();
                        int opinionsCount = 0; // TODO usunąć na produkcji
                        for (HtmlElement itemOpinion : itemOpinions) {
                            if (opinionsCount < 10) {
                                Opinion opinion = new Opinion();
                                HtmlElement opinionRate = itemOpinion.getFirstByXPath(".//p[@class='author']/strong");
                                HtmlElement opinionAuthorAdnDate = itemOpinion.getFirstByXPath(".//p[@class='author']");
                                HtmlElement opinionText = itemOpinion.getFirstByXPath(".//blockquote/p");

                                String authorString = StringUtils.substring(opinionAuthorAdnDate.asText(), 18, opinionAuthorAdnDate.asText().length() - 11);
                                String author;
                                if (!authorString.isEmpty() && authorString.substring(authorString.length() - 1).equals(","))
                                    author = StringUtils.substring(authorString, 0, authorString.length() - 1);
                                else
                                    author = authorString;

                                String opinionDate = StringUtils.substring(opinionAuthorAdnDate.asText(),
                                        opinionAuthorAdnDate.asText().length() - 10, opinionAuthorAdnDate.asText().length());

                                opinion.setRate(!opinionsAvailable ? null : new Integer(opinionRate.asText()));
                                opinion.setAuthor(authorString.isEmpty() ? null : author);
                                opinion.setDate(opinionDate.isEmpty() ? null : opinionDate);
                                opinion.setText(opinionText == null ? null : opinionText.asText());

                                opinionsList.add(opinion);
                                opinionsCount++;
                            }
                        }

                        // REVIEW
                        List<Review> reviewsList = new ArrayList<>();
                        int rewiewsCount = 0; // TODO usunąć na produkcji
                        for (HtmlElement itemReview : itemReviews) {
                            if (rewiewsCount < 2) {
                                Review review = new Review();
                                HtmlAnchor reviewOrganization = itemReview.getFirstByXPath(".//p[@class='author']/a");
                                HtmlElement reviewAuthorAndDate = itemReview.getFirstByXPath(".//p[@class='author']");
                                HtmlElement opinionText = itemReview.getFirstByXPath(".//blockquote");

                                String organization = reviewOrganization == null ? null : reviewOrganization.asText();
                                String author = organization != null ?
                                        StringUtils.substring(reviewAuthorAndDate.asText(),organization.length()+1,reviewAuthorAndDate.asText().length()-12)
                                        : StringUtils.substring(reviewAuthorAndDate.asText(),1,reviewAuthorAndDate.asText().length()-12);
                                String date = StringUtils.substring(reviewAuthorAndDate.asText(),
                                        reviewAuthorAndDate.asText().length()-10, reviewAuthorAndDate.asText().length());

                                review.setOrganization(organization);
                                review.setAuthor(author);
                                review.setDate(date);
                                review.setText(opinionText.asText().replace("\r\n"," ")
                                        .replace("\r"," ").replace("\n"," "));

                                reviewsList.add(review);
                                rewiewsCount++;
                            }
                        }

                        // ITEM
                        Item item = new Item();
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