package pl.com.uek.hd.webscraper;

import java.math.BigDecimal;
import java.util.Date;

public class Opinion {
    private BigDecimal rate;
    private String author;
    private Date date;
    private String text;

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
