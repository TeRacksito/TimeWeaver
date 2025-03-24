package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.Author;
import es.angelkrasimirov.biblioteca.models.BookGenre;
import es.angelkrasimirov.biblioteca.services.BookGenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookGenreController {

	@Autowired
	private BookGenreService bookGenreService;

	@GetMapping("/book-genres")
	public ResponseEntity<List<BookGenre>> getBookGenres() {
		return ResponseEntity.ok(bookGenreService.getAllBookGenres());
	}

	@GetMapping("/book-genres/{bookGenreId}")
	public ResponseEntity<BookGenre> getBookGenreById(@PathVariable Long bookGenreId) {
		BookGenre bookGenre = bookGenreService.getBookGenreById(bookGenreId);

		if (bookGenre == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(bookGenre);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/book-genres")
	public ResponseEntity<BookGenre> createBookGenre(@Valid @RequestBody BookGenre bookGenre) {
		BookGenre newBookGenre = bookGenreService.saveBookGenre(bookGenre);
		return ResponseEntity.status(HttpStatus.CREATED).body(newBookGenre);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/book-genres/{bookGenreId}")
	public ResponseEntity<BookGenre> updateAuthor(@PathVariable Long bookGenreId, @Valid @RequestBody BookGenre bookGenre) {
		try {
			BookGenre updatedBookGenre = bookGenreService.updateBookGenre(bookGenreId, bookGenre);
			return ResponseEntity.ok(updatedBookGenre);
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/book-genres/{bookGenreId}")
	public ResponseEntity<Void> deleteBookGenre(@PathVariable Long bookGenreId) {
		try {
			bookGenreService.deleteBookGenre(bookGenreId);
			return ResponseEntity.ok().build();
		} catch (NoResourceFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
}
