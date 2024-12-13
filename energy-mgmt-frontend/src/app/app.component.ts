import {Component, OnInit} from '@angular/core';
import {BrowserStorageService} from "./core/service/browser-storage-mgmt/browser-storage.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'energy-mgmt-frontend';

  constructor(private browserStorage: BrowserStorageService) {}

  ngOnInit() {
    this.browserStorage.clearCookies();
  }

}

