package pl.com.uek.hd.webscraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import pl.com.uek.hd.mvc.model.Book;
import pl.com.uek.hd.mvc.model.Opinion;
import pl.com.uek.hd.mvc.model.Review;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformatorFactory {

    public Transformator createTransformator(String transformatorType){
        if (transformatorType.equals("Book")) {
            return new BookTransformator();
        } else if (transformatorType.equals("Opinion")) {
            return new OpinionTransformator();
        } else if (transformatorType.equals("Review")) {
            return new ReviewTransformator();
        } else {
            throw new IllegalArgumentException("Unknown transformator." + transformatorType);
        }
    }

    private class BookTransformator implements Transformator<Book>{


        @Override
        public Book transform(HtmlElement htmlElement){
            try {
                WebClient wc = new WebClient();
                wc.getOptions().setCssEnabled(false);
                wc.getOptions().setJavaScriptEnabled(false);

                if (htmlElement != null) {

                            HtmlAnchor itemAnchor = htmlElement.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
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

                            // ITEM
                            List<HtmlElement> itemTags = itemHtmlPage.getByXPath("//ul[@class='tags']/li");
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



                            List<String> tagsList = new ArrayList<>();
                            for (HtmlElement itemTag : itemTags) {
                                if (!tagsList.toString().contains(itemTag.asText())) {
                                    if (itemTag.getAttribute("class").contains("promotion"))
                                        tagsList.add(StringUtils.substring(itemTag.asText(), 0, 8));
                                    else
                                        tagsList.add(itemTag.asText());
                                }
                            }


                            List<String> ratesList = new ArrayList<>();
                            for (HtmlElement itemRate : itemRates) {
                                ratesList.add(itemRate.asText().replace("\r\n", " - "));
                            }

                            Book book = new Book();
                            // BOOK
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

                            ObjectMapper om = new ObjectMapper();
                            String jsonString = om.writeValueAsString(book);

                            System.out.println(jsonString);
                            return book;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return new Book();}
    }

    private class OpinionTransformator implements Transformator<List<Opinion>>{

        @Override
        public List<Opinion> transform(HtmlElement htmlElement) {
            try {
                WebClient wc = new WebClient();
                wc.getOptions().setCssEnabled(false);
                wc.getOptions().setJavaScriptEnabled(false);
                if (htmlElement != null) {
                    HtmlAnchor itemAnchor = htmlElement.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                    String itemUrl = "https:" + itemAnchor.getHrefAttribute();
                    HtmlPage itemHtmlPage = wc.getPage(itemUrl);


                    // OPINION
                    List<HtmlElement> itemOpinions = itemHtmlPage.getByXPath("//div[@id='section4']/div[@class='list']/ol[@class='list']/li");
                    HtmlElement itemOpinionsNumber = itemHtmlPage.getFirstByXPath(".//h2[@class='votes-header-two']/span");
                    List<HtmlElement> itemRates = itemHtmlPage.getByXPath("//ul[@class='oceny']/li");


                    boolean opinionsAvailable = itemOpinionsNumber != null;

                    List<String> ratesList = new ArrayList<>();
                    for (HtmlElement itemRate : itemRates) {
                        ratesList.add(itemRate.asText().replace("\r\n", " - "));
                    }


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


                    ObjectMapper om = new ObjectMapper();
                    String jsonString = om.writeValueAsString(opinionsList);

                    System.out.println(jsonString);
                    return opinionsList;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return new ArrayList<>();}
    }

    private class ReviewTransformator implements Transformator<List<Review>>{

        @Override
        public List<Review> transform(HtmlElement htmlElement) {
            try {
                WebClient wc = new WebClient();
                wc.getOptions().setCssEnabled(false);
                wc.getOptions().setJavaScriptEnabled(false);

                if (htmlElement != null) {

                    HtmlAnchor itemAnchor = htmlElement.getFirstByXPath(".//div[@class='book-info-middle']/h3/a");
                    String itemUrl = "https:" + itemAnchor.getHrefAttribute();
                    HtmlPage itemHtmlPage = wc.getPage(itemUrl);

                    // REVIEW
                    List<HtmlElement> itemReviews = itemHtmlPage.getByXPath("//div[@id='section5']/ol[@class='list']/li");




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


                    ObjectMapper om = new ObjectMapper();
                    String jsonString = om.writeValueAsString(reviewsList);

                    System.out.println(jsonString);
                    return reviewsList; }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return new ArrayList<>(); }
    }


}
