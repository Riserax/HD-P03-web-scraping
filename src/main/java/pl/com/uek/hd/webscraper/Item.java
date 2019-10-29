package pl.com.uek.hd.webscraper;

import java.math.BigDecimal;

public class Item {
   private String name;
   private String author;
   private BigDecimal price;
   private String url;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(final String author) {
      this.author = author;
   }

   public BigDecimal getPrice() {
      return this.price;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
