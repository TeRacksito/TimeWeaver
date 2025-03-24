package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.models.BookGenre;
import es.angelkrasimirov.biblioteca.repositories.BookGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Service
public class BookGenreService {

	@Autowired
	private BookGenreRepository bookGenreRepository;

	public List<BookGenre> getAllBookGenres() {
		return bookGenreRepository.findAll();
	}

	public BookGenre getBookGenreById(Long bookGenreId) {
		return bookGenreRepository.findById(bookGenreId).orElse(null);
	}

	public BookGenre saveBookGenre(BookGenre bookGenre) {
		return bookGenreRepository.save(bookGenre);
	}

	public BookGenre updateBookGenre(Long bookGenreId, BookGenre bookGenre) throws NoResourceFoundException {
		BookGenre existingBookGenre = getBookGenreById(bookGenreId);
		if (existingBookGenre == null) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No book genre found with id " + bookGenreId);
		}

		bookGenre.setId(bookGenreId);

		return saveBookGenre(bookGenre);
	}

	public void deleteBookGenre(Long bookGenreId) throws NoResourceFoundException {
		if (!bookGenreRepository.existsById(bookGenreId)) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No book genre found with id " + bookGenreId);
		}
		bookGenreRepository.deleteById(bookGenreId);
	}
}
