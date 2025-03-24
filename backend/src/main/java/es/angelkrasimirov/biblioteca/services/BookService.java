package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.models.Author;
import es.angelkrasimirov.biblioteca.models.Book;
import es.angelkrasimirov.biblioteca.models.BookGenre;
import es.angelkrasimirov.biblioteca.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private BookGenreService bookGenreService;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public List<Book> getFreeBooks() {
		return bookRepository.findByFreeBookCopies();
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	public Book saveBook(Book book, Long authorId) throws NoResourceFoundException {
		Author author = authorService.getAuthorById(authorId);

		if (author == null) {
			throw new NoResourceFoundException(HttpMethod.POST, "No author found with id " + authorId);
		}

		book.setAuthor(author);
		return saveBook(book);
	}

	public Book updateBook(Long id, Book book) throws NoResourceFoundException {
		Book existingBook = getBookById(id);
		if (existingBook == null) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No book found with id " + id);
		}

		book.setId(id);
		book.setAuthor(existingBook.getAuthor());

		return saveBook(book);
	}

	public void deleteBook(Long id) throws NoResourceFoundException {
		if (!bookRepository.existsById(id)) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No book found with id " + id);
		}

		bookRepository.deleteById(id);
	}

	public List<Book> getBooksByAuthor(Long authorId) {
		return bookRepository.findByAuthorId(authorId);
	}

	public Book addGenreToBook(Long bookId, Long genreId) throws NoResourceFoundException {
		Book book = getBookById(bookId);

		if (book == null) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No book found with id " + bookId);
		}

		BookGenre genre = bookGenreService.getBookGenreById(genreId);

		if (genre == null) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No genre found with id " + genreId);
		}

		book.addGenre(genre);
		return saveBook(book);
	}

	public void removeGenreFromBook(Long bookId, Long genreId) throws NoResourceFoundException {
		Book book = getBookById(bookId);

		if (book == null) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No book found with id " + bookId);
		}

		BookGenre genre = bookGenreService.getBookGenreById(genreId);

		if (genre == null) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No genre found with id " + genreId);
		}

		book.removeGenre(genre);
		saveBook(book);
	}
}
