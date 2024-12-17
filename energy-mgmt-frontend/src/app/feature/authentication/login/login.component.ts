import {Component, DestroyRef, inject, model, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../../core/service/auth/auth.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {LoginModel} from "../../../shared/models/login.model";
import {UserService} from "../../../core/service/user/user.service";
import {MatDialog} from '@angular/material/dialog';
import {DialogOverviewExampleComponent} from "./dialog-overview-example/dialog-overview-example.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup = new FormGroup({});
  errorMessage?: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private destroyRef: DestroyRef
  ) {
  }

  ngOnInit() {
    this.buildLoginForm();
  }

  readonly animal = signal('');
  readonly name = model('');
  readonly dialog = inject(MatDialog);
  openDialog(): void {
    const dialogRef = this.dialog.open(DialogOverviewExampleComponent, {
      data: {name: this.name(), animal: this.animal()},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result !== undefined) {
        this.animal.set(result);
      }
    });
  }

  private buildLoginForm(): void {
    this.loginForm = this.formBuilder.group({
      username: [ '', [ Validators.required ] ],
      password2: [ '', [ Validators.required ] ]
    });
  }

  login(): void {
    if (!this.loginForm?.valid) {
      this.errorMessage = 'Invalid form completion!';
      setTimeout(() => this.errorMessage = undefined, 3000);
      return;
    }

    const credentials: LoginModel = {
      username: this.loginForm?.get('username')?.value,
      password: this.loginForm?.get('password2')?.value
    };

    this.authService.login(credentials)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => this.getUserInfo(),
        error: () => this.errorMessage = 'Invalid credentials'
      });
  }

  private getUserInfo(): void {

    this.userService.getInfo()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(response => {
        sessionStorage.setItem('loggedUser', JSON.stringify(response));
        this.router.navigateByUrl(response.admin? '/dashboard/admin' : '/dashboard/client');
        // this.router.navigateByUrl('/dashboard/chat');
      });
  }
}


