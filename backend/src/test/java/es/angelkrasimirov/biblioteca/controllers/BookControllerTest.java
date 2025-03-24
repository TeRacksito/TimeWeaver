package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.Book;
import es.angelkrasimirov.biblioteca.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

	private MockMvc mockMvc;

	@Mock
	private BookService bookService;

	@InjectMocks
	private BookController bookController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	void shouldReturnAllBooks() throws Exception {
		Book book1 = new Book();
		book1.setId(1L);
		book1.setTitle("Book One");
		book1.setUniqueCode("BOOK001");

		Book book2 = new Book();
		book2.setId(2L);
		book2.setTitle("Book Two");
		book2.setUniqueCode("BOOK002");

		when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

		mockMvc.perform(get("/api/v1/books"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Book One"))
				.andExpect(jsonPath("$[1].title").value("Book Two"));
	}

	@Test
	void shouldReturnBookByIdWhenExists() throws Exception {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Book One");
		book.setUniqueCode("BOOK001");

		when(bookService.getBookById(1L)).thenReturn(book);

		mockMvc.perform(get("/api/v1/books/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Book One"))
				.andExpect(jsonPath("$.uniqueCode").value("BOOK001"));
	}

	@Test
	void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
		when(bookService.getBookById(999L)).thenReturn(null);

		mockMvc.perform(get("/api/v1/books/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldCreateBook() throws Exception {
		Book newBook = new Book();
		newBook.setTitle("New Book");
		newBook.setUniqueCode("NEWBOOK001");

		Book savedBook = new Book();
		savedBook.setId(1L);
		savedBook.setTitle("New Book");
		savedBook.setUniqueCode("NEWBOOK001");

		when(bookService.saveBook(any(Book.class), anyLong())).thenReturn(savedBook);

		mockMvc.perform(post("/api/v1/authors/1/books")
						.contentType("application/json")
						.content("{\"title\":\"New Book\", \"uniqueCode\":\"NEWBOOK001\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("New Book"))
				.andExpect(jsonPath("$.uniqueCode").value("NEWBOOK001"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldUpdateBook() throws Exception {
		Book updatedBook = new Book();
		updatedBook.setId(1L);
		updatedBook.setTitle("Updated Book");
		updatedBook.setUniqueCode("UPDATEDBOOK001");

		when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

		mockMvc.perform(put("/api/v1/books/1")
						.contentType("application/json")
						.content("{\"title\":\"Updated Book\", \"uniqueCode\":\"UPDATEDBOOK001\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated Book"))
				.andExpect(jsonPath("$.uniqueCode").value("UPDATEDBOOK001"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNotFoundWhenUpdatingNonExistingBook() throws Exception {
		when(bookService.updateBook(eq(999L), any(Book.class))).thenThrow(new NoResourceFoundException(HttpMethod.PUT, "Book not found"));

		mockMvc.perform(put("/api/v1/books/999")
						.contentType("application/json")
						.content("{\"title\":\"Non-Existing Book\", \"uniqueCode\":\"NONEXISTENTBOOK001\"}"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldDeleteBook() throws Exception {
		doNothing().when(bookService).deleteBook(1L);

		mockMvc.perform(delete("/api/v1/books/1"))
				.andExpect(status().isOk());

		verify(bookService, times(1)).deleteBook(1L);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNoContentWhenDeletingNonExistingBook() throws Exception {
		doThrow(new NoResourceFoundException(HttpMethod.DELETE, "Book not found")).when(bookService).deleteBook(999L);

		mockMvc.perform(delete("/api/v1/books/999"))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldAddGenreToBook() throws Exception {
		Book updatedBook = new Book();
		updatedBook.setId(1L);
		updatedBook.setTitle("Book with Genre");
		updatedBook.setUniqueCode("BOOKWITHGENRE001");

		when(bookService.addGenreToBook(eq(1L), eq(1L))).thenReturn(updatedBook);

		mockMvc.perform(put("/api/v1/books/1/book-genres/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Book with Genre"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNotFoundWhenAddingGenreToNonExistingBook() throws Exception {
		when(bookService.addGenreToBook(eq(999L), eq(1L))).thenThrow(new NoResourceFoundException(HttpMethod.PUT, "Book not found"));

		mockMvc.perform(put("/api/v1/books/999/book-genres/1"))
				.andExpect(status().isNotFound());
	}
}
