package app.LibraryBoot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Table(name = "Person")
@Component
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp = "[А-Я]\\p{L}+ [А-Я]\\p{L}+ [А-Я]\\p{L}+", message =
            "Full name must be correct (First Name Last Name Patronymic)")
    @NotEmpty(message = "Enter a full name")
    @Column(name = "name")
    private String name;

    @Column(name = "year")
    @Min(value = 1920, message = "Year of birth must be greater than 1920")
    @Max(value = 2014, message = "Year of birth must not be greater than 2014")
    private int year;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public Person(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                '}';
    }
}
