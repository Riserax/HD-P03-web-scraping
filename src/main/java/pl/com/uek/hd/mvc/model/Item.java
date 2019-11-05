package pl.com.uek.hd.mvc.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {
   @Id
   @Column(name = "id")
   private long itemId;
   private boolean bookAvailable;
   private BigDecimal bookPriceOld;
   private BigDecimal bookPrice;
   private boolean eBookAvailable;
   private BigDecimal eBookPriceOld;
   private BigDecimal eBookPrice;
   private List<String> tags;
   private String description;
   private String aboutAuthor;
   private Integer opinionsNumber;
   private BigDecimal overallRate;
   private List<String> rates;

   private Integer reviewsNumber;

   @Override
   public String toString() {
      return "Item{" +
              "bookAvailable=" + bookAvailable +
              ", bookPriceOld=" + bookPriceOld +
              ", bookPrice=" + bookPrice +
              ", eBookAvailable=" + eBookAvailable +
              ", eBookPriceOld=" + eBookPriceOld +
              ", eBookPrice=" + eBookPrice +
              ", tags=" + tags +
              ", description='" + description + '\'' +
              ", aboutAuthor='" + aboutAuthor + '\'' +
              ", opinionsNumber=" + opinionsNumber +
              ", overallRate=" + overallRate +
              ", rates=" + rates +
              ", reviewsNumber=" + reviewsNumber +
              ", book=" + book +
              ", opinions=" + opinions +
              ", reviews=" + reviews +
              '}';
   }

   public Book getBook() {
      return book;
   }

   public void setBook(Book book) {
      this.book = book;
   }

   public boolean isBookAvailable() {
      return bookAvailable;
   }

   public void setBookAvailable(boolean bookAvailable) {
      this.bookAvailable = bookAvailable;
   }

   public BigDecimal getBookPriceOld() {
      return bookPriceOld;
   }

   public void setBookPriceOld(BigDecimal bookPriceOld) {
      this.bookPriceOld = bookPriceOld;
   }

   public BigDecimal getBookPrice() {
      return bookPrice;
   }

   public void setBookPrice(BigDecimal bookPrice) {
      this.bookPrice = bookPrice;
   }

   public boolean iseBookAvailable() {
      return eBookAvailable;
   }

   public void seteBookAvailable(boolean eBookAvailable) {
      this.eBookAvailable = eBookAvailable;
   }

   public BigDecimal geteBookPriceOld() {
      return eBookPriceOld;
   }

   public void seteBookPriceOld(BigDecimal eBookPriceOld) {
      this.eBookPriceOld = eBookPriceOld;
   }

   public BigDecimal geteBookPrice() {
      return eBookPrice;
   }

   public void seteBookPrice(BigDecimal eBookPrice) {
      this.eBookPrice = eBookPrice;
   }

   public List<String> getTags() {
      return tags;
   }

   public void setTags(List<String> tags) {
      this.tags = tags;
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

   public Integer getOpinionsNumber() {
      return opinionsNumber;
   }

   public void setOpinionsNumber(Integer opinionsNumber) {
      this.opinionsNumber = opinionsNumber;
   }

   public BigDecimal getOverallRate() {
      return overallRate;
   }

   public void setOverallRate(BigDecimal overallRate) {
      this.overallRate = overallRate;
   }

   public List<String> getRates() {
      return rates;
   }

   public void setRates(List<String> rates) {
      this.rates = rates;
   }

   public List<Opinion> getOpinions() {
      return opinions;
   }

   public void setOpinions(List<Opinion> opinions) {
      this.opinions = opinions;
   }

   public Integer getReviewsNumber() {
      return reviewsNumber;
   }

   public void setReviewsNumber(Integer reviewsNumber) {
      this.reviewsNumber = reviewsNumber;
   }

   public List<Review> getReviews() {
      return reviews;
   }

   public void setReviews(List<Review> reviews) {
      this.reviews = reviews;
   }

   @OneToOne
   @JoinColumn(name = "book_id")
   private Book book;

   @OneToMany(fetch = FetchType.LAZY)
   @JoinColumn(name = "opinion_id")
   private List<Opinion> opinions;

   @OneToMany(fetch = FetchType.LAZY)
   @JoinColumn(name = "review_id")
   private List<Review> reviews;

}
