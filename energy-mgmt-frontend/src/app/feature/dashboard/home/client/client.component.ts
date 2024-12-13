import {Component, DestroyRef, OnDestroy, OnInit} from '@angular/core';
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
import {environment} from "../../../../../environments/environment.development";

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
    message: '',
    device: '',
    date: ''
  };
  notificationList: NotificationModel[] = [];
  // Use a Map to store readings for each device
  deviceReadings: { [deviceId: string]: any[] } = {}; // Store readings for each device

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

    // Note: ws://localhost/ws?userId=some-uuid
    let currentUserId = this.getUser().id;
    this.webSocketService.connectSocket(`${environment.MONITORING_URL}?userId=${currentUserId}`);

    // Subscribe to WebSocket messages
    this.webSocketService.message$.subscribe((message) => {
      if (message.type === 'PUSH_NOTIFICATION') {
        this.handlePushNotification(message);
      } else if (message.type === 'BUNCH_OF_READINGS') {
        this.handleReadings(message); // Pass the data to the chart component
      }
    });
  }

  ngOnDestroy() { // TODO: mai testeaza, ca nu se distruge cookie-ul cum vrei tu cand dai stop la app
    // Disconnect from the WebSocket when the component is destroyed
    this.webSocketService.disconnectSocket();
    console.warn("Socket connection closed");

    this.clearCookies();
  }

  // Handle PUSH_NOTIFICATION type
  private handlePushNotification(notificationData: any): void {
    this.notification = {
      message: notificationData.message || 'New Notification',
      device: notificationData.deviceId || 'Some device!',
      date: notificationData.date || 'Today'
    };
    let newNotification = this.notification;
    this.notificationList.push(newNotification);
    this.openDialog();
  }

  // Handle BUNCH_OF_READINGS type
  private handleReadings(incomingData: any): void {
    const requiredDeviceId = incomingData.deviceId;
    const readings: any[] = JSON.parse(incomingData.message);
    const x = readings.map((reading) =>
      ({
        name: reading.hour,
        value: reading.readValue
      }));

    // Check if the device already has data in the deviceReadings map
    if (!this.deviceReadings[requiredDeviceId]) {
      // If not, initialize it as an empty array
      this.deviceReadings[requiredDeviceId] = [];
    }

    const deviceData = {
      name: 'Device whatever',
      series: x
    };

    // this.deviceReadings[requiredDeviceId].push(deviceData);
    // console.log(JSON.stringify(this.deviceReadings[requiredDeviceId]));
    // Update the readings array with a new reference
    this.deviceReadings = {
      ...this.deviceReadings,
      [requiredDeviceId]: [deviceData],
    };

    console.log(JSON.stringify(this.deviceReadings[requiredDeviceId]));
  }

  // A helper method to get readings for a specific device
  getReadingsForDevice(deviceId: string | undefined): any[] {
    if(deviceId) {
      return this.deviceReadings[deviceId] || [];
    }
    return [];
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

  openDialog() {
    //   https://www.youtube.com/watch?v=FThtv9iorao&t=10s
    //   https://www.concretepage.com/angular-material/angular-material-dialog-position

    // Calculate the offset based on the current number of notifications
    const offset = (this.notificationList.length - 1) * 110; // 110px per notification (adjust as needed)

    let notification = this.notification;
    console.log("what data gets here: {}", notification);
    this.dialogRef.open(PushNotificationComponent, {
      data: notification,
      width: '350px',
      height: '100px',
      position: {right:'10px', top: `${10 + offset}px`},
      hasBackdrop: false
      // disableClose: true
    });
  }

  goToPage(pageName:string){
    this.router.navigate([`${pageName}`]);
  }
}

