import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-home',
  imports: [RouterModule, CommonModule, TranslatePipe, TranslateDirective],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  // books: Book[] = [];
  // loading = false;
  // error: string | null = null;
  // constructor(private booksService: BooksService) {}
  // ngOnInit(): void {
  //   this.loadBooks();
  // }
  // loadBooks(): void {
  //   this.loading = true;
  //   this.error = null;
  //   this.booksService.getAllBooks().subscribe({
  //     next: (books) => {
  //       this.books = books;
  //       this.loading = false;
  //     },
  //     error: (err) => {
  //       console.error('Error loading books', err);
  //       this.error = 'Failed to load books. Please try again later.';
  //       this.loading = false;
  //     },
  //   });
  // }
}
