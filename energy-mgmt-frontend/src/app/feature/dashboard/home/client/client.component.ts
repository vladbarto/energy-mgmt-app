import {Component, DestroyRef, inject, OnDestroy, OnInit, signal, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {DeviceModel} from "../../../../shared/models/device.model";
import {DeviceService} from "../../../../core/service/device/device.service";
import {UserModel} from "../../../../shared/models/user.model";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {WebSocketService} from "../../../../core/service/web-socket/web-socket.service";
import {NotificationModel} from "../../../../shared/models/notification.model";
import {MatDialog} from "@angular/material/dialog";
import {PushNotificationComponent} from "./push-notification/push-notification.component";

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent implements OnInit, OnDestroy {
  newDeviceRequestForm: FormGroup = new FormGroup({});
  showNewDevice: boolean = false;
  devices: DeviceModel[] = [];
  noOfDevices: number = 0;
  notification: NotificationModel = {
    title: '',
    message: ''
  };

  constructor(
    private router: Router,
    private destroyRef: DestroyRef,
    private formBuilder: FormBuilder,
    private deviceService: DeviceService,
    private webSocketService: WebSocketService,
    private dialogRef: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.buildNewDeviceForm();
    this.getDevicesOfLoggedUser();
    this.webSocketService.connectSocket();
  }

  ngOnDestroy() { // TODO: mai testeaza, ca nu se distruge cookie-ul cum vrei tu cand dai stop la app
    // Disconnect from the WebSocket when the component is destroyed
    this.webSocketService.disconnectSocket();
    console.warn("Socket connection closed");

    this.clearCookies();
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
          this.noOfDevices = response.length;
        }, error: err => console.log(err),
      });
  }

  triggerNotification() {
    this.notification = {
      title: 'New Notification',
      message: 'This is a dynamically triggered notification!'
    };
  }

  openDialog() {
    this.dialogRef.open(PushNotificationComponent, {
      data: {
        title: 'New Notification',
        message: 'This is a dynamically triggered notification!'
      },
      width: '350px',
      height: '100px',
      position: {right:'10px', top: '10px'},
      // disableClose: true
    });
  //   https://www.youtube.com/watch?v=FThtv9iorao&t=10s
  //   https://www.concretepage.com/angular-material/angular-material-dialog-position
  }
}

