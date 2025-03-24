import { Component } from '@angular/core';
import { HeaderComponent } from '../../header/header.component';
import { FooterComponent } from '../../footer/footer.component';

@Component({
  selector: 'app-full-layout',
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './full-layout.component.html',
  styleUrl: './full-layout.component.css',
})
export class FullLayoutComponent {}
