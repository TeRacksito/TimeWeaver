import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css',
  imports: [TranslatePipe],
})
export class FooterComponent {
  currentYear: number = new Date().getFullYear();
}
