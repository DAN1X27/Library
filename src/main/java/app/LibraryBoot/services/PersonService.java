package app.LibraryBoot.services;


import app.LibraryBoot.models.Book;
import app.LibraryBoot.models.Person;
import app.LibraryBoot.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final BooksService booksService;
    @Autowired
    public PersonService(PersonRepository personRepository,BooksService booksService) {
        this.personRepository = personRepository;
        this.booksService = booksService;
    }


    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(int id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    public Optional<Person> findByName(String name) {
        return personRepository.findByName(name);
    }

    public List<Person> searchByName(String name) {
       return personRepository.findPeopleByNameStartingWithIgnoreCase(name);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(int id ,Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public List<Book> findPersonBooks(int person_id){
        Optional<Person> person = personRepository.findById(person_id);

        if(person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                if(booksService.findExpiredBooks().contains(book)){
                    book.setExpired(true);
                }
            });


            return person.get().getBooks();
        }else {
            return Collections.emptyList();
        }

    }


}
