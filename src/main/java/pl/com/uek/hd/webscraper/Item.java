package pl.com.uek.hd.webscraper;

import java.math.BigDecimal;
import java.util.List;

public class Item {
   private Book book;
   private List<String> tags;
   private BigDecimal overallRate;
   private BigDecimal price;
   private boolean eBookAvailable;
   private BigDecimal eBookPrice;
   private String url;
   private String description;
   private String aboutAuthor;
   private List<Opinion> opinions;
   private Integer opinionsNumber;
   private List<Review> reviews;
   private Integer reviewsNumber;
   private List<String> categories;
//   private String tableOfContents; // nie wiem czy jestesmy w stanie i czy jest sens

   public Book getBook() {
      return book;
   }

   public void setBook(Book book) {
      this.book = book;
   }

   public List<String> getTags() {
      return tags;
   }

   public void setTags(List<String> tags) {
      this.tags = tags;
   }

   public BigDecimal getOverallRate() {
      return overallRate;
   }

   public void setOverallRate(BigDecimal overallRate) {
      this.overallRate = overallRate;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public boolean iseBookAvailable() {
      return eBookAvailable;
   }

   public void seteBookAvailable(boolean eBookAvailable) {
      this.eBookAvailable = eBookAvailable;
   }

   public BigDecimal geteBookPrice() {
      return eBookPrice;
   }

   public void seteBookPrice(BigDecimal eBookPrice) {
      this.eBookPrice = eBookPrice;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getAboutAuthor() {
      return aboutAuthor;
   }

   public void setAboutAuthor(String aboutAuthor) {
      this.aboutAuthor = aboutAuthor;
   }

   public List<Opinion> getOpinions() {
      return opinions;
   }

   public void setOpinions(List<Opinion> opinions) {
      this.opinions = opinions;
   }

   public Integer getOpinionsNumber() {
      return opinionsNumber;
   }

   public void setOpinionsNumber(Integer opinionsNumber) {
      this.opinionsNumber = opinionsNumber;
   }

   public List<Review> getReviews() {
      return reviews;
   }

   public void setReviews(List<Review> reviews) {
      this.reviews = reviews;
   }

   public Integer getReviewsNumber() {
      return reviewsNumber;
   }

   public void setReviewsNumber(Integer reviewsNumber) {
      this.reviewsNumber = reviewsNumber;
   }

   public List<String> getCategories() {
      return categories;
   }

   public void setCategories(List<String> categories) {
      this.categories = categories;
   }
}
