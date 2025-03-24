package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.models.Book;
import es.angelkrasimirov.biblioteca.models.BookCopy;
import es.angelkrasimirov.biblioteca.models.BookLoanHistory;
import es.angelkrasimirov.biblioteca.models.User;
import es.angelkrasimirov.biblioteca.repositories.BookCopyRepository;
import es.angelkrasimirov.biblioteca.repositories.BookLoanHistoryRepository;
import es.angelkrasimirov.biblioteca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookLoanHistoryService {

	@Autowired
	private BookLoanHistoryRepository bookLoanHistoryRepository;

	@Autowired
	private BookCopyRepository bookCopyRepository;

	@Autowired
	private UserRepository userRepository;

	public List<BookLoanHistory> getBookLoansByUserId(Long userId) {
		return bookLoanHistoryRepository.findByUserId(userId);
	}

	public List<BookLoanHistory> getOngoingBookLoansByUserId(Long userId) {
		return bookLoanHistoryRepository.findByUserIdAndActualReturnDateIsNull(userId);
	}


	public BookLoanHistory startBookLoanToUser(Long userId, Long bookCopyId, BookLoanHistory bookLoanHistory) throws NoResourceFoundException {
		User user = userRepository.findById(userId).orElseThrow(
				() -> new NoResourceFoundException(HttpMethod.GET, "No user found with id " + userId)
		);
		BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).orElseThrow(
				() -> new NoResourceFoundException(HttpMethod.GET, "No book found with id " + bookCopyId)
		);

		BookLoanHistory existingLoan = bookCopy.getLoan();

		if (existingLoan == null) {
			bookLoanHistory.setLoanDate(LocalDateTime.now());
			bookCopy.setLoan(bookLoanHistory);
		} else {
			if (user != existingLoan.getUser()) {
				throw new DataIntegrityViolationException("Book copy with id " + bookCopyId + " is already loaned to another user");
			}

			bookLoanHistory.setId(existingLoan.getId());
			bookLoanHistory.setLoanDate(existingLoan.getLoanDate());
		}

		if (bookLoanHistory.getDueDate().isBefore(bookLoanHistory.getLoanDate())) {
			throw new DataIntegrityViolationException("Due date cannot be before loan date");
		}

		bookLoanHistory.setUser(user);
		bookLoanHistory.setBook(bookCopy.getBook());

		return bookLoanHistoryRepository.save(bookLoanHistory);
	}

	public BookLoanHistory endBookLoanToUser(BookLoanHistory bookLoanHistory) throws NoResourceFoundException {
		BookCopy bookCopy = bookCopyRepository.findByLoanId(bookLoanHistory.getId()).orElseThrow(
				() -> new NoResourceFoundException(HttpMethod.GET, "No book copy found with loan id " + bookLoanHistory.getId())
		);
		bookCopy.setLoan(null);
		bookLoanHistory.setActualReturnDate(LocalDateTime.now());
		return bookLoanHistoryRepository.save(bookLoanHistory);
	}

	public BookLoanHistory endBookLoanToUser(Long bookCopyId) throws NoResourceFoundException {
		BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).orElseThrow(
				() -> new NoResourceFoundException(HttpMethod.GET, "No book copy found with id " + bookCopyId)
		);

		BookLoanHistory bookLoanHistory = bookCopy.getLoan();

		if (bookLoanHistory == null) {
			throw new NoResourceFoundException(HttpMethod.GET, "No book loan found for book copy with id " + bookCopyId);
		}

		return endBookLoanToUser(bookLoanHistory);
	}
}
