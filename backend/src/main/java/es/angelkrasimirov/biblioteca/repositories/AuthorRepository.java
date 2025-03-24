package es.angelkrasimirov.biblioteca.repositories;

import es.angelkrasimirov.biblioteca.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
//	Page<Author> findAll(Pageable pageable);
}
