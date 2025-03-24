package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.Author;
import es.angelkrasimirov.biblioteca.services.AuthorService;
import es.angelkrasimirov.biblioteca.services.BookService;
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
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@Autowired
	private BookService bookService;

	@GetMapping("/authors")
	public ResponseEntity<List<Author>> getAllAuthors() {
		return ResponseEntity.ok(authorService.getAllAuthors());
	}

	@GetMapping("/authors/{authorId}")
	public ResponseEntity<Author> getAuthorById(@PathVariable Long authorId) {
		Author author = authorService.getAuthorById(authorId);

		if (author == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(author);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/authors")
	public ResponseEntity<Author> saveAuthor(@Valid @RequestBody Author author) {
		Author newAuthor = authorService.saveAuthor(author);
		return ResponseEntity.status(HttpStatus.CREATED).body(newAuthor);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/authors/{authorId}")
	public ResponseEntity<Author> updateAuthor(@PathVariable Long authorId, @Valid @RequestBody Author author) {
		try {
			Author updatedAuthor = authorService.updateAuthor(authorId, author);
			return ResponseEntity.ok(updatedAuthor);
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/authors/{authorId}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
		try {
			authorService.deleteAuthor(authorId);
			return ResponseEntity.ok().build();
		} catch (NoResourceFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
}
