package app.LibraryBoot.utill;

import app.LibraryBoot.models.Book;
import app.LibraryBoot.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidator(final BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if(booksService.findByTitle(book.getTitle()) != null){
            errors.rejectValue("title", "", "There is already a book with the same title");
        }
    }
}
