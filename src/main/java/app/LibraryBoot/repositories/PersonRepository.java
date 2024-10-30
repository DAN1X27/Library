package app.LibraryBoot.repositories;

import app.LibraryBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByName(String name);

    List<Person> findPeopleByNameStartingWithIgnoreCase(String name);
}
