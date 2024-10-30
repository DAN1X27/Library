package app.LibraryBoot.repositories;

import app.LibraryBoot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    Book findByTitle(String title);
    List<Book> findAllByOrderByYearAsc();

    List<Book> findBooksByTitleStartingWithIgnoreCase(String title);

    List<Book> findBooksByDateOfGetBookBefore(Date date);

}
