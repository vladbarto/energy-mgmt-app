import {Component, DestroyRef, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserModel} from "../../../../shared/models/user.model";
import {BrowserStorageService} from "../../../../core/service/browser-storage-mgmt/browser-storage.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit{

  protected loggedUser: UserModel = {admin: false, email: "", id: "", password: "", username: ""}

  constructor(
    private router: Router,
    private browserStorage: BrowserStorageService
  ) {
  }

  ngOnInit(): void {
    this.loggedUser = this.browserStorage.getUser();
  }

  logOut(): void {
    this.browserStorage.clearCookies();
    sessionStorage.removeItem('loggedUser');
    this.router.navigateByUrl('/auth/login');
  }


  goToPage(pageName:string){
    this.router.navigate([`${pageName}`]);
  }
}
