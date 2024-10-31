import {Component, inject, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserModel} from "../../../../shared/models/user.model";
import {UserService} from "../../../../core/service/user/user.service";

@Component({
  selector: 'app-dialog-overview-example',
  styleUrl: './dialog-overview-example.component.css',
  templateUrl: `./dialog-overview-example.component.html`
})
export class DialogOverviewExampleComponent implements OnInit {
  registerForm: FormGroup = new FormGroup({});
  errorMessage?: string;
  successMessage?: string;

  readonly dialogRef = inject(MatDialogRef<DialogOverviewExampleComponent>);

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService
  ) {

  }

  ngOnInit() {
    this.buildRegisterForm();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  private buildRegisterForm(): void {
    this.registerForm = this.formBuilder.group({
      username: [ '', [ Validators.required ] ],
      password: [ '', [ Validators.required ] ],
      email: ['', [ Validators.required ] ],
      admin: ['']
    });
  }

  register(): void {
    if (!this.registerForm?.valid) {
      this.errorMessage = 'Invalid form completion!';
      setTimeout(() => this.errorMessage = undefined, 3000);
      return;
    }

    const credentials: UserModel = {
      email: this.registerForm?.get('email')?.value,
      username: this.registerForm?.get('username')?.value,
      password: this.registerForm?.get('password')?.value,
      admin: this.registerForm?.get('admin')?.value
    };

    this.userService.register(credentials).subscribe(
      {
        next: () => {
          this.successMessage = 'Account created successfully';
          setTimeout(() => this.successMessage = undefined, 3000);
        },
        error: () => this.errorMessage = 'Invalid credentials'
      });
  }


}
