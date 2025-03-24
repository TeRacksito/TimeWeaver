package es.angelkrasimirov.biblioteca.repositories;

import es.angelkrasimirov.biblioteca.models.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
}
