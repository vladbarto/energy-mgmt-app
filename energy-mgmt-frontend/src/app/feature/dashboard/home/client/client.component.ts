import {Component, DestroyRef, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DeviceModel} from "../../../../shared/models/device.model";
import {DeviceService} from "../../../../core/service/device/device.service";
import {UserModel} from "../../../../shared/models/user.model";
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css',
})
export class ClientComponent implements OnInit {
  newDeviceRequestForm: FormGroup = new FormGroup({});
  showNewDevice: boolean = false;
  devices: DeviceModel[] = [];
  noOfDevices: number = 0;

  constructor(
    private router: Router,
    private destroyRef: DestroyRef,
    private formBuilder: FormBuilder,
    private deviceService: DeviceService
  ) {
  }

  ngOnInit(): void {
    this.buildNewDeviceForm();
    this.getDevicesOfLoggedUser();
  }

  private buildNewDeviceForm(): void {
    this.newDeviceRequestForm = this.formBuilder.group({
      description: [ '' ],
      address: [ '' ],
      mhec: [ '' ],
      userId: [ '' ]
    });
  }

  logOut(): void {
    this.clearCookies();
    localStorage.removeItem('loggedUser');
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

  displayNewDeviceForm(): void {
    this.showNewDevice = !this.showNewDevice;
  }

  onAddNewDevice(): void {
    console.log("YAAAY!");

    const newDevice: DeviceModel = {
      description: this.newDeviceRequestForm?.get('description')?.value,
      address: this.newDeviceRequestForm?.get('address')?.value,
      mhec: this.newDeviceRequestForm?.get('mhec')?.value,
      userId: this.getUser().id
    };

    console.log(newDevice);
    this.deviceService.insert(newDevice).subscribe(() => {
      // Optionally, trigger a refresh of the devices list after a new device is added
      this.getDevicesOfLoggedUser()
    });

  }

  getUser = (): UserModel => {
    return JSON.parse(localStorage.getItem('loggedUser') || '');
  }

  private getDevicesOfLoggedUser(): void {
    this.deviceService.getAllOfLoggedUser(JSON.parse(localStorage.getItem('loggedUser') || '').id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: response => {
          this.devices = response;
          console.log(this.devices);
          this.noOfDevices = response.length;
        }, error: err => console.log(err),
      });
  }

}

