	package es.angelkrasimirov.biblioteca.controllers;

	import es.angelkrasimirov.biblioteca.models.Book;
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
	public class BookController {

		@Autowired
		private BookService bookService;

		@GetMapping("/books")
		public ResponseEntity<List<Book>> getBooks(@RequestParam(name = "free", required = false) Boolean free) {
			List<Book> books;

			if (free != null && free) {
				books = bookService.getFreeBooks();
			} else {
				books = bookService.getAllBooks();
			}

			return ResponseEntity.ok(books);
		}

		@GetMapping("/books/{bookId}")
		public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
			Book book = bookService.getBookById(bookId);

			if (book == null) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok(book);
		}

		@GetMapping("/authors/{authorId}/books")
		public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
			return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
		}

		@PreAuthorize("hasRole('ADMIN')")
		@PostMapping("/authors/{authorId}/books")
		public ResponseEntity<Book> createBook(@PathVariable Long authorId, @Valid @RequestBody Book book) {
			try {
				Book newBook = bookService.saveBook(book, authorId);
				return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
			} catch (NoResourceFoundException e) {
				return ResponseEntity.notFound().build();
			}
		}

		@PreAuthorize("hasRole('ADMIN')")
		@PutMapping("/books/{bookId}")
		public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @Valid @RequestBody Book book) {
			try {
				Book updatedBook = bookService.updateBook(bookId, book);
				return ResponseEntity.ok(updatedBook);

			} catch (NoResourceFoundException e) {
				return ResponseEntity.notFound().build();
			}
		}

		@PreAuthorize("hasRole('ADMIN')")
		@DeleteMapping("/books/{bookId}")
		public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
			try {
				bookService.deleteBook(bookId);
				return ResponseEntity.ok().build();
			} catch (NoResourceFoundException e) {
				return ResponseEntity.noContent().build();
			}
		}

		@PreAuthorize("hasRole('ADMIN')")
		@PutMapping("/books/{bookId}/book-genres/{genreId}")
		public ResponseEntity<Book> addGenreToBook(@PathVariable Long bookId, @PathVariable Long genreId) {
			try {
				Book updatedBook = bookService.addGenreToBook(bookId, genreId);
				return ResponseEntity.ok(updatedBook);
			} catch (NoResourceFoundException e) {
				return ResponseEntity.notFound().build();
			}
		}
	}