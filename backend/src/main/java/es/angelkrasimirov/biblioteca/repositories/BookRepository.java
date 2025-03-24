package es.angelkrasimirov.biblioteca.repositories;

import es.angelkrasimirov.biblioteca.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByAuthorId(Long authorId);

	@Query("SELECT b FROM Book b WHERE b.id IN (SELECT bc.book.id FROM BookCopy bc WHERE bc.loan IS NULL)")
	List<Book> findByFreeBookCopies();
}
