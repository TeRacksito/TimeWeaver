import { Component, OnInit, Renderer2, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  TranslateDirective,
  TranslatePipe,
  TranslateService,
} from '@ngx-translate/core';
import { DOCUMENT } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  imports: [CommonModule, RouterModule, TranslatePipe, TranslateDirective],
  standalone: true,
  host: {
    class: 'sticky top-0 z-50',
  },
})
export class HeaderComponent implements OnInit {
  currentLang: string = 'en';
  isDarkTheme: boolean = false;

  constructor(
    private translate: TranslateService,
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document
  ) {}

  ngOnInit() {
    this.currentLang =
      this.translate.currentLang || this.translate.defaultLang || 'en';

    this.translate.onLangChange.subscribe((event) => {
      this.currentLang = event.lang;
    });

    this.initializeTheme();
  }

  switchLanguage(lang: string, event?: Event) {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    this.translate.use(lang);
    this.currentLang = lang;
  }

  initializeTheme() {
    const savedTheme = localStorage.getItem('theme');

    if (savedTheme) {
      this.isDarkTheme = savedTheme === 'dark';
    } else {
      this.isDarkTheme = window.matchMedia(
        '(prefers-color-scheme: dark)'
      ).matches;
    }

    this.applyTheme();

    window
      .matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', (e) => {
        if (!localStorage.getItem('theme')) {
          this.isDarkTheme = e.matches;
          this.applyTheme();
        }
      });
  }

  toggleTheme(event?: Event) {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    this.isDarkTheme = !this.isDarkTheme;
    this.applyTheme();

    localStorage.setItem('theme', this.isDarkTheme ? 'dark' : 'light');
  }

  applyTheme() {
    const theme = this.isDarkTheme ? 'dark' : 'light';
    this.renderer.setAttribute(
      this.document.documentElement,
      'data-theme',
      theme
    );
  }
}
