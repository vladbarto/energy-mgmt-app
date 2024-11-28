import { Component, Input } from '@angular/core';
import { DeviceModel } from '../../../../../shared/models/device.model';
import {Color, ScaleType} from "@swimlane/ngx-charts";
import {NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {WebSocketService} from "../../../../../core/service/web-socket/web-socket.service";

@Component({
  selector: 'app-device-card',
  templateUrl: './device-card.component.html',
  styleUrl: './device-card.component.css',
})
export class DeviceCardComponent {
  @Input() device!: DeviceModel;
  @Input() readings: any[] = [];
  calendar: NgbDateStruct | undefined;

  view: [number, number] = [700, 300];

  // options
  legend: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Hour';
  yAxisLabel: string = 'Energy Consumption [kWh]';
  timeline: boolean = true;

  colorScheme: Color = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5'],
    name: "some name",
    selectable: true,
    group: ScaleType.Linear
  };

  constructor(
    private webSocketService: WebSocketService
  ) {
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: any): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: any): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  onDateChange(): void {
    if(this.calendar) {
      if(this.device) {
        this.webSocketService.sendMessage(
          "FETCH_READINGS",
          "We're gonna fetch some readings data from DB",
          this.device.id!,
          this.ngbDateToString(this.calendar),
          this.device.mhec);
      }
    }
  }

  private ngbDateToString(date: NgbDateStruct): string {
    return `${date.year}-${date.month}-${date.day}`
  }
}
