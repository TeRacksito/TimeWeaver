import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FullLayoutComponent } from './components/layouts/full-layout/full-layout.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, FullLayoutComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';
}
