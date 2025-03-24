package es.angelkrasimirov.biblioteca.repositories;

import es.angelkrasimirov.biblioteca.models.BookLoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookLoanHistoryRepository extends JpaRepository<BookLoanHistory, Long> {
	BookLoanHistory findByBookId(Long bookId);
	List<BookLoanHistory> findByUserId(Long userId);

	List<BookLoanHistory> findByUserIdAndActualReturnDateIsNull(Long userId);
}
