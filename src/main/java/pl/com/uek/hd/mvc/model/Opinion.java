package pl.com.uek.hd.mvc.model;

import javax.persistence.*;

@Entity
@Table(name = "opinions")
public class Opinion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long opinionId;

    private Integer rate;
    private String author;
    private String date;
    @Lob
    private String text;

    public long getOpinionId() { return opinionId; }

    public void setOpinionId(long opinionId) { this.opinionId = opinionId; }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
