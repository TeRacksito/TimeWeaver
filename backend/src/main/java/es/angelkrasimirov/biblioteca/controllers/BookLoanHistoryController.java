package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.BookLoanHistory;
import es.angelkrasimirov.biblioteca.services.BookLoanHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookLoanHistoryController {

	@Autowired
	private BookLoanHistoryService bookLoanHistoryService;

	@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
	@GetMapping("/users/{userId}/book-copies")
	public ResponseEntity<List<BookLoanHistory>> getBookLoansByUserId(
			@PathVariable Long userId,
			@RequestParam(name = "ongoing", required = false, defaultValue = "false") boolean ongoing
	) {
		List<BookLoanHistory> bookLoanHistories;

		if (ongoing) {
			bookLoanHistories = bookLoanHistoryService.getOngoingBookLoansByUserId(userId);
		} else {

			bookLoanHistories = bookLoanHistoryService.getBookLoansByUserId(userId);
		}

		return ResponseEntity.ok(bookLoanHistories);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/users/{userId}/book-copies/{bookCopyId}")
	public ResponseEntity<BookLoanHistory> startBookLoanToUser(
			@PathVariable Long userId,
			@PathVariable Long bookCopyId,
			@Valid @RequestBody BookLoanHistory bookLoanHistory
	) {
		try {
			return ResponseEntity.ok(bookLoanHistoryService.startBookLoanToUser(userId, bookCopyId, bookLoanHistory));
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/users/{userId}/book-copies/{bookCopyId}")
	public ResponseEntity<BookLoanHistory> endBookLoanToUser(
			@PathVariable Long userId,
			@PathVariable Long bookCopyId
	) {
		try {
			return ResponseEntity.ok(bookLoanHistoryService.endBookLoanToUser(bookCopyId));
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
