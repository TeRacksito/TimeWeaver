package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.BookGenre;
import es.angelkrasimirov.biblioteca.services.BookGenreService;
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

class BookGenreControllerTest {

	private MockMvc mockMvc;

	@Mock
	private BookGenreService bookGenreService;

	@InjectMocks
	private BookGenreController bookGenreController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookGenreController).build();
	}

	@Test
	void shouldReturnAllBookGenres() throws Exception {
		BookGenre bookGenre1 = new BookGenre();
		bookGenre1.setId(1L);
		bookGenre1.setName("Fiction");
		bookGenre1.setDescription("A genre of imaginative storytelling.");

		BookGenre bookGenre2 = new BookGenre();
		bookGenre2.setId(2L);
		bookGenre2.setName("Non-Fiction");
		bookGenre2.setDescription("A genre based on facts and real events.");

		when(bookGenreService.getAllBookGenres()).thenReturn(Arrays.asList(bookGenre1, bookGenre2));

		mockMvc.perform(get("/api/v1/book-genres"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Fiction"))
				.andExpect(jsonPath("$[0].description").value("A genre of imaginative storytelling."))
				.andExpect(jsonPath("$[1].name").value("Non-Fiction"))
				.andExpect(jsonPath("$[1].description").value("A genre based on facts and real events."));
	}

	@Test
	void shouldReturnBookGenreByIdWhenExists() throws Exception {
		BookGenre bookGenre = new BookGenre();
		bookGenre.setId(1L);
		bookGenre.setName("Fiction");
		bookGenre.setDescription("A genre of imaginative storytelling.");

		when(bookGenreService.getBookGenreById(1L)).thenReturn(bookGenre);

		mockMvc.perform(get("/api/v1/book-genres/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Fiction"))
				.andExpect(jsonPath("$.description").value("A genre of imaginative storytelling."));
	}

	@Test
	void shouldReturnNotFoundWhenBookGenreDoesNotExist() throws Exception {
		when(bookGenreService.getBookGenreById(999L)).thenReturn(null);

		mockMvc.perform(get("/api/v1/book-genres/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldSaveBookGenre() throws Exception {
		BookGenre bookGenre = new BookGenre();
		bookGenre.setName("Fiction");
		bookGenre.setDescription("A genre of imaginative storytelling.");

		BookGenre savedBookGenre = new BookGenre();
		savedBookGenre.setId(1L);
		savedBookGenre.setName("Fiction");
		savedBookGenre.setDescription("A genre of imaginative storytelling.");

		when(bookGenreService.saveBookGenre(any(BookGenre.class))).thenReturn(savedBookGenre);

		mockMvc.perform(post("/api/v1/book-genres")
						.contentType("application/json")
						.content("{\"name\":\"Fiction\",\"description\":\"A genre of imaginative storytelling.\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Fiction"))
				.andExpect(jsonPath("$.description").value("A genre of imaginative storytelling."));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldUpdateBookGenre() throws Exception {
		BookGenre updatedBookGenre = new BookGenre();
		updatedBookGenre.setId(1L);
		updatedBookGenre.setName("Updated Fiction");
		updatedBookGenre.setDescription("Updated description.");

		when(bookGenreService.updateBookGenre(eq(1L), any(BookGenre.class))).thenReturn(updatedBookGenre);

		mockMvc.perform(put("/api/v1/book-genres/1")
						.contentType("application/json")
						.content("{\"name\":\"Updated Fiction\",\"description\":\"Updated description.\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Fiction"))
				.andExpect(jsonPath("$.description").value("Updated description."));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNotFoundWhenUpdatingNonExistingBookGenre() throws Exception {
		when(bookGenreService.updateBookGenre(eq(999L), any(BookGenre.class))).thenThrow(new NoResourceFoundException(HttpMethod.PUT, "Book genre not found"));

		mockMvc.perform(put("/api/v1/book-genres/999")
						.contentType("application/json")
						.content("{\"name\":\"Non-Existing Genre\",\"description\":\"This genre does not exist.\"}"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldDeleteBookGenre() throws Exception {
		doNothing().when(bookGenreService).deleteBookGenre(1L);

		mockMvc.perform(delete("/api/v1/book-genres/1"))
				.andExpect(status().isOk());

		verify(bookGenreService, times(1)).deleteBookGenre(1L);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldReturnNoContentWhenDeletingNonExistingBookGenre() throws Exception {
		doThrow(new NoResourceFoundException(HttpMethod.DELETE, "Book genre not found")).when(bookGenreService).deleteBookGenre(999L);

		mockMvc.perform(delete("/api/v1/book-genres/999"))
				.andExpect(status().isNoContent());
	}
}