import {AfterViewInit, Component, DestroyRef, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DeviceModel} from "../../../../shared/models/device.model";
import {DeviceService} from "../../../../core/service/device/device.service";
import {UserModel} from "../../../../shared/models/user.model";
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {multi} from "./data";
import {Color, NgxChartsModule} from "@swimlane/ngx-charts";

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

  multi: any[] | undefined;
  view = [700, 300];

  // options
  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Year';
  yAxisLabel: string = 'Population';
  timeline: boolean = true;


  constructor(
    private router: Router,
    private destroyRef: DestroyRef,
    private formBuilder: FormBuilder,
    private deviceService: DeviceService
  ) {
    Object.assign(this, { multi });
  }

  // colorScheme: Color = {
  //   domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  // };


  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: any): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: any): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
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
    return JSON.parse(sessionStorage.getItem('loggedUser') || '');
  }

  private getDevicesOfLoggedUser(): void {
    this.deviceService.getAllOfLoggedUser(JSON.parse(sessionStorage.getItem('loggedUser') || '').id)
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

