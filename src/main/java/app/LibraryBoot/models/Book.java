package app.LibraryBoot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Book")
@Component
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Book title cannot be empty")
    @Size(min = 2, max = 100, message = "Book titles must be between 2 and 100 characters")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Author name cannot be empty")
    private String author;

    @Column(name = "year")
    @Min(value = 1000, message = "The year of publication of the book must be greater than 1000")
    @Max(value = 2024, message = "The book's publication year cannot be greater than 2024")
    private int year;


    @Column(name = "date_of_get_book")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfGetBook;

    @Transient
    public boolean expired;

    @Column(name = "date_before_expired")
    @Temporal(TemporalType.DATE)
    private Date dateBeforeExpired;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    public Person owner;

    public Book(String title, String author, int year, Person owner) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.owner = owner;
    }

    public Book(){
    }

    public Date getDateOfGetBook() {
        return dateOfGetBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(dateOfGetBook, book.dateOfGetBook) && Objects.equals(owner, book.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, year, dateOfGetBook, owner);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }

    public Date getDateBeforeExpired() {
        return dateBeforeExpired;
    }

    public void setDateBeforeExpired(Date dateBeforeExpired) {
        this.dateBeforeExpired = dateBeforeExpired;
    }

    public void setDateOfGetBook(Date dateOfGetBook) {
        this.dateOfGetBook = dateOfGetBook;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
