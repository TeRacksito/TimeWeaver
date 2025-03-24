package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.Author;
import es.angelkrasimirov.biblioteca.services.AuthorService;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorControllerTest {

	private MockMvc mockMvc;

	@Mock
	private AuthorService authorService;

	@Mock
	private BookService bookService;

	@InjectMocks
	private AuthorController authorController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
	}

	@Test
	void shouldReturnAllAuthors() throws Exception {
		Author author1 = new Author();
		author1.setId(1L);
		author1.setName("Author 1");

		Author author2 = new Author();
		author2.setId(2L);
		author2.setName("Author 2");

		when(authorService.getAllAuthors()).thenReturn(Arrays.asList(author1, author2));

		mockMvc.perform(get("/api/v1/authors"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Author 1"))
				.andExpect(jsonPath("$[1].name").value("Author 2"));
	}

	@Test
	void shouldReturnAuthorByIdWhenExists() throws Exception {
		Author author = new Author();
		author.setId(1L);
		author.setName("Author 1");

		when(authorService.getAuthorById(1L)).thenReturn(author);

		mockMvc.perform(get("/api/v1/authors/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Author 1"));
	}

	@Test
	void shouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
		when(authorService.getAuthorById(999L)).thenReturn(null);

		mockMvc.perform(get("/api/v1/authors/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldSaveAuthor() throws Exception {
		Author author = new Author();
		author.setName("New Author");

		Author savedAuthor = new Author();
		savedAuthor.setId(1L);
		savedAuthor.setName("New Author");

		when(authorService.saveAuthor(any(Author.class))).thenReturn(savedAuthor);

		mockMvc.perform(post("/api/v1/authors")
						.contentType("application/json")
						.content("{\"name\":\"New Author\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("New Author"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldUpdateAuthor() throws Exception {
		Author updatedAuthor = new Author();
		updatedAuthor.setId(1L);
		updatedAuthor.setName("Updated Author");

		when(authorService.updateAuthor(eq(1L), any(Author.class))).thenReturn(updatedAuthor);

		mockMvc.perform(put("/api/v1/authors/1")
						.contentType("application/json")
						.content("{\"name\":\"Updated Author\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Author"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNotFoundWhenUpdatingNonExistingAuthor() throws Exception {
		when(authorService.updateAuthor(eq(999L), any(Author.class))).thenThrow(new NoResourceFoundException(HttpMethod.PUT, "Author not found"));

		mockMvc.perform(put("/api/v1/authors/999")
						.contentType("application/json")
						.content("{\"name\":\"Non-Existing Author\"}"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldDeleteAuthor() throws Exception {
		doNothing().when(authorService).deleteAuthor(1L);

		mockMvc.perform(delete("/api/v1/authors/1"))
				.andExpect(status().isOk());

		verify(authorService, times(1)).deleteAuthor(1L);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNoContentWhenDeletingNonExistingAuthor() throws Exception {
		doThrow(new NoResourceFoundException(HttpMethod.DELETE, "Author not found")).when(authorService).deleteAuthor(999L);

		mockMvc.perform(delete("/api/v1/authors/999"))
				.andExpect(status().isNoContent());
	}
}
