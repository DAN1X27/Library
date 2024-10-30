package app.LibraryBoot.services;

import app.LibraryBoot.models.Book;
import app.LibraryBoot.models.Person;
import app.LibraryBoot.repositories.BooksRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final EntityManager entityManager;

    @Autowired
    public BooksService(BooksRepository booksRepository, EntityManager entityManager) {
        this.booksRepository = booksRepository;
        this.entityManager = entityManager;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }


    public List<Book> findSortedAll(){
        return booksRepository.findAllByOrderByYearAsc();
    }


    public Book findById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    public Book findByTitle(String title) {
        return booksRepository.findByTitle(title);
    }

    public List<Book> searchBook(String title) {
        return booksRepository.findBooksByTitleStartingWithIgnoreCase(title);
    }

    public Person getOwnerBook(int bookId) {
        Session session = entityManager.unwrap(Session.class);

        return session.createQuery("select p from Person p join Book b on b.owner.id=p.id where b.id=:id", Person.class).setParameter("id", bookId)
                .getResultList().stream().findAny().orElse(null);
    }

    public List<Book> findExpiredBooks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -10);
        return booksRepository.findBooksByDateOfGetBookBefore(calendar.getTime());
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id ,Book book) {
         book.setId(id);
         booksRepository.save(book);
    }
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void lockBook(int bookId, Person person) {
       Book book = findById(bookId);
       book.setOwner(person);
       Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.DAY_OF_MONTH, 10);
       book.setDateOfGetBook(new Date());
       book.setDateBeforeExpired(calendar.getTime());
    }

    @Transactional
    public void unlockBook(int bookId) {
        Book book = findById(bookId);
        book.setOwner(null);
        book.setDateOfGetBook(null);
        book.setDateBeforeExpired(null);
    }
}
