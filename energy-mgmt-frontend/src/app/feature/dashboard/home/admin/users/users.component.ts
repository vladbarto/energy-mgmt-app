import { Component, DestroyRef, OnInit } from '@angular/core';
import { UserService } from "../../../../../core/service/user/user.service";
import { UserModel } from "../../../../../shared/models/user.model";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserRefService} from "../../../../../core/service/user-ref/user-ref.service";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: UserModel[] = [];
  username: string = "";
  newUserForm: FormGroup = new FormGroup({});
  showNewUser: boolean = false;

  constructor(
    private userService: UserService,
    private userRefService: UserRefService,
    private destroyRef: DestroyRef,
    private router: Router,
    private formBuilder: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.getUsers();
    this.buildNewUserForm();
  }

  private buildNewUserForm(): void {
    this.newUserForm = this.formBuilder.group({
      username: [''],
      email: [''],
      password: [''],
      admin: ['']
    });
  }

  displayNewUserForm(): void {
    this.showNewUser = !this.showNewUser;
  }

  protected getUsers(): void {
    this.userService.getAll()
      .subscribe({
        next: response => this.users = response,
        error: err => console.log(err)
      });
  }

  getUser = (): UserModel => {
    return JSON.parse(sessionStorage.getItem('loggedUser') || '');
  }

  logOut(): void {
    this.clearCookies();
    sessionStorage.removeItem('loggedUser');
    this.router.navigateByUrl('/auth/login');
  }

  private clearCookies(): void {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i];
      const equalPos = cookie.indexOf('=');
      const name = equalPos > -1 ? cookie.slice(0, equalPos) : cookie;
      document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;';
    }
  }

  addUser() {
    const newUser = {
      username: this.newUserForm?.get('username')?.value,
      email: this.newUserForm?.get('email')?.value,
      password: this.newUserForm?.get('password')?.value,
      admin: this.newUserForm?.get('admin')?.value
    };

    this.userService
      .insert(newUser)
      .subscribe( (newUser) => {
        this.users.push(newUser);
        this.buildNewUserForm(); // reset the form
        this.displayNewUserForm();
      } );

  }

  deleteUserCalled(userId: string): void {
    this.userService.deleteById(userId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          console.log('User deleted, attempting to delete user reference');
          this.getUsers(); // refresh list

          // Subscribe explicitly to ensure the request is sent
          this.userRefService.delete(userId).subscribe({
            next: () => console.log('User reference deleted successfully'),
            error: err => console.log('Error deleting user reference:', err)
          });
        },
        error: err => console.log('Error deleting user:', err)
      });
  }


  saveUserCalled(user: UserModel): void {
    this.userService.update(user, user.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: updatedUser => {
          const index = this.users.findIndex(u => u.id === updatedUser.id);
          if (index !== -1) {
            this.users[index] = updatedUser; // Update user in list
          }
        },
        error: err => console.log('Error updating user:', err)
      });
  }

  searchByUsername() {
    this.userService.getByUsername(this.username)
      .subscribe(response => this.users = [response]);
  }
}
