import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DeviceModel} from "../../../../../../shared/models/device.model";
import {UserModel} from "../../../../../../shared/models/user.model";

@Component({
  selector: 'app-device-card-a',
  templateUrl: './device-card-a.component.html',
  styleUrl: './device-card-a.component.css'
})
export class DeviceCardAComponent {
  @Input() device!: DeviceModel;
  @Output() saveDevice: EventEmitter<DeviceModel> = new EventEmitter<DeviceModel>();
  @Output() deleteDevice: EventEmitter<string> = new EventEmitter<string>();

  editMode: boolean = false;
  editableDevice!: DeviceModel;  // Temporary object for editable data

  editDevice(): void {
    this.editMode = true;
    this.editableDevice = { ...this.device }; // Make a copy of user data for editing
  }

  cancelEdit(): void {
    this.editMode = false;
  }

  save(): void {
    console.log('[device-card] se emite request de save');
    this.saveDevice.emit(this.editableDevice); // Emit the updated data to parent
    this.editMode = false; // Exit edit mode after saving
  }

  deleteDevicePressed(deviceId: string | undefined): void {
    this.deleteDevice.emit(deviceId);
  }
}
