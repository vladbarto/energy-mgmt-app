import {Component, DestroyRef, OnInit} from '@angular/core';
import {DeviceModel} from "../../../../../shared/models/device.model";
import { DeviceService } from "../../../../../core/service/device/device.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {UserModel} from "../../../../../shared/models/user.model";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-devices',
  templateUrl: './devices.component.html',
  styleUrl: './devices.component.css'
})
export class DevicesComponent implements OnInit {
  devices: DeviceModel[] = [];
  mhec: number = 0;
  newDeviceForm: FormGroup = new FormGroup({});
  showNewDevice: boolean = false;

  constructor(
    private destroyRef: DestroyRef,
    private deviceService: DeviceService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit() {
    this.getDevices();
    this.buildNewDeviceForm();
  }

  private buildNewDeviceForm(): void {
    this.newDeviceForm = this.formBuilder.group({
      description: [''],
      address: [''],
      mhec: [''],
      userId: ['']
    });
  }

  displayNewDeviceForm(): void {
    this.showNewDevice = !this.showNewDevice;
  }

  protected getDevices(): void {
    this.deviceService.getAll()
      .subscribe({
        next: response => this.devices = response,
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

  addDevice() {
    const newDevice = {
      description: this.newDeviceForm?.get('description')?.value,
      address: this.newDeviceForm?.get('address')?.value,
      mhec: this.newDeviceForm?.get('mhec')?.value,
      userId: this.newDeviceForm?.get('userId')?.value
    };

    this.deviceService
      .insert(newDevice)
      .subscribe( (newDevice) => {
        this.devices.push(newDevice);
        this.buildNewDeviceForm(); // reset the form
        this.displayNewDeviceForm();
      } );

  }

  searchAllByMhec() {
    this.deviceService.getAll_2(this.mhec)
      .subscribe(response => this.devices = response);
  }

  saveDeviceCalled(device: DeviceModel): void {
    this.deviceService.update(device, device.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: updatedDevice => {
          const index = this.devices.findIndex(u => u.id === updatedDevice.id);
          if (index !== -1) {
            this.devices[index] = updatedDevice; // Update device in list
          }
        },
        error: err => console.log('Error updating device:', err)
      });
  }

  deleteDeviceCalled(deviceId: string): void {
    this.deviceService.deleteById(deviceId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.getDevices(); //refresh list
        },
        error: err => console.log('Error deleting user:', err)
      });
  }
}
