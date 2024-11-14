import {Component, DestroyRef} from '@angular/core';
import {Router} from "@angular/router";
import {UserModel} from "../../../../shared/models/user.model";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  constructor(
    private router: Router
  ) {
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

  getUser = (): UserModel => {
    return JSON.parse(sessionStorage.getItem('loggedUser') || '');
  }
}
