package app.LibraryBoot.controllers;


import app.LibraryBoot.models.Book;
import app.LibraryBoot.models.Person;
import app.LibraryBoot.services.BooksService;
import app.LibraryBoot.services.PersonService;
import app.LibraryBoot.utill.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final PersonService personService;
    private final BooksService booksService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(PersonService personService, BooksService booksService, BookValidator bookValidator) {
        this.personService = personService;
        this.booksService = booksService;
        this.bookValidator = bookValidator;
    }

    @GetMapping
    public String getAllBooks(Model model,
                              @RequestParam(value = "sort_by_year", required = false) boolean sort_by_year){

        if (sort_by_year) {
            model.addAttribute("books" ,booksService.findSortedAll());
        }else {
            model.addAttribute("books", booksService.findAll());
        }

        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id ,Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));
        Person bookOwner = booksService.getOwnerBook(id);
        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", personService.findAll());

        return "books/show";
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("book", new Book());
        return "books/search";
    }

    @PatchMapping("/getSearch")
    public String getSearchingBook(Model model, @ModelAttribute Book book) {
        if (!booksService.searchBook(book.getTitle()).isEmpty()){
            model.addAttribute("books", booksService.searchBook(book.getTitle()));
        }else {
            model.addAttribute("notFound", new String());
        }

        return "books/resultOfSearch";
    }

    @GetMapping("/create")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/create";
    }

    @PostMapping
    public String createBook(Model model, @ModelAttribute @Valid Book book,
                            BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/create";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("/unlock/{id}")
    public String unlockBook(Model model, @PathVariable("id") int id) {
        booksService.unlockBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/lock/{bookId}")
    public String lockBook(Model model, @PathVariable("bookId") int bookId,
                           @ModelAttribute Person person) {
        booksService.lockBook(bookId, person);
        return "redirect:/books/" + bookId;
    }



    @GetMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id ,Model model) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String editBook(Model model, @ModelAttribute @Valid Book book,
                           @PathVariable("id") int id,  BindingResult bindingResult) {
        booksService.update(id, book);
        return "redirect:/books/" + id;
    }

    @GetMapping("/deleteAccept/{id}")
    public String deleteAccept(Model model ,@PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id));
        return "books/delete_accept";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(Model model, @PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

}
