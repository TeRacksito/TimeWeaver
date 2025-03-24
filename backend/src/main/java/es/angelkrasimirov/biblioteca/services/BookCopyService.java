package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.models.Book;
import es.angelkrasimirov.biblioteca.models.BookCopy;
import es.angelkrasimirov.biblioteca.repositories.BookCopyRepository;
import es.angelkrasimirov.biblioteca.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class BookCopyService {

	@Autowired
	private BookCopyRepository bookCopyRepository;

	@Autowired
	private BookRepository bookRepository;

	public List<BookCopy> getAllBookCopies() {
		return bookCopyRepository.findAll();
	}

	public Optional<BookCopy> getBookCopyById(Long bookCopyId) {
		return bookCopyRepository.findById(bookCopyId);
	}

	public BookCopy saveBookCopy(BookCopy bookCopy) {
		return bookCopyRepository.save(bookCopy);
	}

	public BookCopy saveBookCopy(Long bookId, BookCopy bookCopy) throws NoResourceFoundException {
		Book book = bookRepository.findById(bookId).orElseThrow(
				() -> new NoResourceFoundException(HttpMethod.POST, "No book found with id " + bookId)
		);

		bookCopy.setBook(book);
		return saveBookCopy(bookCopy);
	}

	public BookCopy updateBookCopy(Long bookCopyId, BookCopy bookCopy) throws NoResourceFoundException {
		if (!bookCopyRepository.existsById(bookCopyId)) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No book copy found with id " + bookCopyId);
		}
		bookCopy.setId(bookCopyId);
		return bookCopyRepository.save(bookCopy);
	}

	public void deleteBookCopy(Long bookCopyId) throws NoResourceFoundException {
		if (!bookCopyRepository.existsById(bookCopyId)) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No book copy found with id " + bookCopyId);
		}
		bookCopyRepository.deleteById(bookCopyId);
	}


	public List<BookCopy> getBookCopiesByBook(Long bookId) throws NoResourceFoundException {
		if (!bookRepository.existsById(bookId)) {
			throw new NoResourceFoundException(HttpMethod.GET, "No book found with id " + bookId);
		}
		return bookCopyRepository.findByBookId(bookId);
	}

	public List<BookCopy> getFreeBookCopiesByBook(Long bookId) throws NoResourceFoundException {
		if (!bookRepository.existsById(bookId)) {
			throw new NoResourceFoundException(HttpMethod.GET, "No book found with id " + bookId);
		}
		return bookCopyRepository.findByBookIdAndLoanIdIsNull(bookId);
	}
}
