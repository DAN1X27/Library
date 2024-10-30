package app.LibraryBoot.controllers;

import app.LibraryBoot.models.Person;
import app.LibraryBoot.services.BooksService;
import app.LibraryBoot.services.PersonService;
import app.LibraryBoot.utill.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonService personService;
    private final BooksService booksService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonService personService, BooksService booksService,
                            PersonValidator personValidator) {
        this.personService = personService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String getPeople(Model model) {
        model.addAttribute("people", personService.findAll());
        return "people/people";
    }

    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model) {

        model.addAttribute("books", personService.findPersonBooks(id));
        model.addAttribute("person" ,personService.findById(id));

        return "people/show";
    }


    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("person", new Person());
        return "people/search";
    }

    @PatchMapping("/getSearch")
    public String getSearchingPerson(Model model, @ModelAttribute Person person) {
        if (!personService.searchByName(person.getName()).isEmpty()) {
            model.addAttribute("people", personService.searchByName(person.getName()));
        }else {
            model.addAttribute("notFound", new String());
        }

        return "people/resultOfSearch";
    }

    @GetMapping("/new")
    public String createPerson(Model model) {


        model.addAttribute("person", new Person());
        return "people/create";
    }

    @PostMapping
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult, Model model) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "people/create";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person" ,personService.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               @PathVariable("id") int id,  BindingResult bindingResult, Model model) {
        personService.update(id, person);
        return "redirect:/people";
    }
    @GetMapping("/deleteAccept/{id}")
    public String deleteAccept(Model model ,@PathVariable("id") int id) {
        model.addAttribute("person", personService.findById(id));
        return "people/delete_accept";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }
}
