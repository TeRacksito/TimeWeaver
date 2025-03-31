import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  inject,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subject, Subscription } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { ProjectsService } from '../../services/projects.service';
import { TranslatePipe } from '@ngx-translate/core';
import { getApiError } from '../../utils/api';

@Component({
  selector: 'app-create-project',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslatePipe],
  templateUrl: './create-project.component.html',
  styleUrl: './create-project.component.css',
})
export class CreateProjectComponent implements OnInit, OnDestroy {
  @Input() openModalTrigger!: Subject<void>;
  @Output() projectCreated = new EventEmitter<void>();

  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private projectsService = inject(ProjectsService);

  isOpen = signal(false);
  isCreating = signal(false);
  errorMessage = signal<string | null>(null);
  projectForm!: FormGroup;
  private subscription!: Subscription;

  readonly NAME_MIN_LENGTH = 3;
  readonly NAME_MAX_LENGTH = 255;
  readonly DESCRIPTION_MAX_LENGTH = 255;

  ngOnInit(): void {
    this.initForm();
    this.subscription = this.openModalTrigger.subscribe(() => {
      this.openModal();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  initForm(): void {
    this.projectForm = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(this.NAME_MIN_LENGTH),
          Validators.maxLength(this.NAME_MAX_LENGTH),
        ],
      ],
      description: ['', [Validators.maxLength(this.DESCRIPTION_MAX_LENGTH)]],
    });
  }

  openModal(): void {
    this.isOpen.set(true);
    this.projectForm.reset();
    this.errorMessage.set(null);
  }

  closeModal(): void {
    this.isOpen.set(false);
  }

  createProject(): void {
    if (this.projectForm.invalid) {
      return;
    }

    const { name, description } = this.projectForm.value;
    const userId = this.authService.getUserId()?.toString() || '';

    this.isCreating.set(true);
    this.errorMessage.set(null);

    this.projectsService.createProject(name, description, userId).subscribe({
      next: () => {
        this.isCreating.set(false);
        this.closeModal();
        this.projectCreated.emit();
      },
      error: (err) => {
        this.isCreating.set(false);
        this.errorMessage.set(getApiError(err, 'Error creating project'));
      },
    });
  }
}
