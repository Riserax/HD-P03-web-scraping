package pl.com.uek.hd.mvc.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
   @Id
   @Column(name = "isbn", length = 14)
   private String itemISBN;
   private boolean bookAvailable;
   private BigDecimal bookPriceOld;
   private BigDecimal bookPrice;
   private boolean eBookAvailable;
   private BigDecimal eBookPriceOld;
   private BigDecimal eBookPrice;
   private Integer opinionsNumber;
   private BigDecimal overallRate;
   private Integer reviewsNumber;
   @Lob
   private String description;
   @Lob
   private String aboutAuthor;

   public Item() {
   }

   public Item(boolean bookAvailable, BigDecimal bookPriceOld, BigDecimal bookPrice, boolean eBookAvailable, BigDecimal eBookPriceOld, BigDecimal eBookPrice, Integer opinionsNumber, BigDecimal overallRate, Integer reviewsNumber, String description, String aboutAuthor, List<String> rates, List<String> tags, Book book, List<Opinion> opinions, List<Review> reviews) {
      this.bookAvailable = bookAvailable;
      this.bookPriceOld = bookPriceOld;
      this.bookPrice = bookPrice;
      this.eBookAvailable = eBookAvailable;
      this.eBookPriceOld = eBookPriceOld;
      this.eBookPrice = eBookPrice;
      this.opinionsNumber = opinionsNumber;
      this.overallRate = overallRate;
      this.reviewsNumber = reviewsNumber;
      this.description = description;
      this.aboutAuthor = aboutAuthor;
      this.rates = rates;
      this.tags = tags;
      this.book = book;
      this.opinions = opinions;
      this.reviews = reviews;
   }

   @Override
   public String toString() {
      return "Item{" +
              "bookAvailable=" + bookAvailable +
              ", bookPriceOld=" + bookPriceOld +
              ", bookPrice=" + bookPrice +
              ", eBookAvailable=" + eBookAvailable +
              ", eBookPriceOld=" + eBookPriceOld +
              ", eBookPrice=" + eBookPrice +
//              ", tags=" + tags +
              ", description='" + description + '\'' +
              ", aboutAuthor='" + aboutAuthor + '\'' +
              ", opinionsNumber=" + opinionsNumber +
              ", overallRate=" + overallRate +
//              ", rates=" + rates +
              ", reviewsNumber=" + reviewsNumber +
              ", book=" + book +
              ", opinions=" + opinions +
              ", reviews=" + reviews +
              '}';
   }

   public Book getBook() {
      return book;
   }

   public String getItemId() { return itemISBN; }

   public void setItemId(String itemId) { this.itemISBN = itemId; }

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

   @ElementCollection
   @CollectionTable(name ="rates")
   private List<String> rates;

   @ElementCollection
   @CollectionTable(name ="tags")
   private List<String> tags;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "books_isbn")
   private Book book;

   @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
   @JoinColumn(name = "items_isbn")
   private List<Opinion> opinions;

   @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
   @JoinColumn(name = "items_isbn")
   private List<Review> reviews;

}
