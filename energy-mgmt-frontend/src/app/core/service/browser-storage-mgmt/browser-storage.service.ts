import { Injectable } from '@angular/core';
import {UserModel} from "../../../shared/models/user.model";

@Injectable({
  providedIn: 'root'
})
export class BrowserStorageService {

  constructor() { }

  public getUser = (): UserModel => {
    return JSON.parse(sessionStorage.getItem('loggedUser') || '');
  }

  public clearCookies(): void {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i];
      const equalPos = cookie.indexOf('=');
      const name = equalPos > -1 ? cookie.slice(0, equalPos) : cookie;
      document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;';
    }
  }
}
