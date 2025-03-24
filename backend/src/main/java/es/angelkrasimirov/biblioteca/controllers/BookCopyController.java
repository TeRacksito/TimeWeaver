package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.BookCopy;
import es.angelkrasimirov.biblioteca.services.BookCopyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookCopyController {

	@Autowired
	private BookCopyService bookCopyService;

//	@GetMapping("/book-copies")
//	public ResponseEntity<List<BookCopy>> getBookCopies() {
//		return ResponseEntity.ok(bookCopyService.getAllBookCopies());
//	}

	@GetMapping("/book-copies/{bookCopyId}")
	public ResponseEntity<BookCopy> getBookCopyById(@PathVariable Long bookCopyId) {
		try {
			BookCopy bookCopy = bookCopyService.getBookCopyById(bookCopyId).orElseThrow(
					() -> new NoResourceFoundException(HttpMethod.GET, "No book copy found with id " + bookCopyId)
			);
			return ResponseEntity.ok(bookCopy);
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

//	@PostMapping("/book-copies")
//	public ResponseEntity<BookCopy> createBookCopy(@RequestBody BookCopy bookCopy) {
//		BookCopy newBookCopy = bookCopyService.saveBookCopy(bookCopy);
//		return ResponseEntity.ok(newBookCopy);
//	}

	@GetMapping("/books/{bookId}/book-copies")
	public ResponseEntity<List<BookCopy>> getBookCopiesByBook(
			@PathVariable Long bookId,
			@RequestParam(name = "free", required = false) Boolean free) {
		try {
//			List<BookCopy> bookCopies = bookCopyService.getBookCopiesByBook(bookId);
			List<BookCopy> bookCopies;
			if (free != null && free) {
				bookCopies = bookCopyService.getFreeBookCopiesByBook(bookId);
			} else {
				bookCopies = bookCopyService.getBookCopiesByBook(bookId);
			}
			return ResponseEntity.ok(bookCopies);
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/books/{bookId}/book-copies")
	public ResponseEntity<BookCopy> createBookCopy(@PathVariable Long bookId, @Valid @RequestBody BookCopy bookCopy) {
		try {
			BookCopy newBookCopy = bookCopyService.saveBookCopy(bookId, bookCopy);
			return ResponseEntity.ok(newBookCopy);
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/book-copies/{bookCopyId}")
	public ResponseEntity<BookCopy> updateBookCopy(@PathVariable Long bookCopyId, @Valid @RequestBody BookCopy bookCopy) {
		try {
			BookCopy updatedBookCopy = bookCopyService.updateBookCopy(bookCopyId, bookCopy);
			return ResponseEntity.ok(updatedBookCopy);
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/book-copies/{bookCopyId}")
	public ResponseEntity<BookCopy> deleteBookCopy(@PathVariable Long bookCopyId) {
		try {
			bookCopyService.deleteBookCopy(bookCopyId);
			return ResponseEntity.ok().build();
		} catch (NoResourceFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
}
