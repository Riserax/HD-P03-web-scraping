package pl.com.uek.hd.mvc.model;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    private String organization;
    private String author;
    private String date;
    @Lob
    private String text;

    public long getReviewId() { return reviewId; }

    public void setReviewId(long reviewId) { this.reviewId = reviewId; }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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
