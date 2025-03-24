package es.angelkrasimirov.biblioteca.repositories;

import es.angelkrasimirov.biblioteca.models.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
	List<BookCopy> findByBookId(Long bookId);
	List<BookCopy> findByBookIdAndLoanIdIsNull(Long bookId);

	Optional<BookCopy> findByLoanId(Long loanId);
}
