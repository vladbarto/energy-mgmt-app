import {Component, Inject} from '@angular/core';
import {NotificationModel} from "../../../../../shared/models/notification.model";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-push-notification',
  templateUrl: './push-notification.component.html',
  styleUrl: './push-notification.component.css'
})
export class PushNotificationComponent {

  protected notification: NotificationModel = {
    title: '',
    message: ''
  };

  constructor(@Inject(MAT_DIALOG_DATA) public data: NotificationModel) {
    this.notification = {
      title: data.title,
      message: data.message
    };
  }
}
