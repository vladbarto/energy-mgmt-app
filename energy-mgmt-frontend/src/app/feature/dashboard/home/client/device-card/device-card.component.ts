import { Component, Input } from '@angular/core';
import { DeviceModel } from '../../../../../shared/models/device.model';

@Component({
  selector: 'app-device-card',
  templateUrl: './device-card.component.html',
  styleUrl: './device-card.component.css'
})
export class DeviceCardComponent {
  @Input() device!: DeviceModel;
  editMode: boolean = false;

  editDevice(): void {
    ;
  }
}
