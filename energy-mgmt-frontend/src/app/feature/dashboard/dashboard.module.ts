import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './home/admin/admin.component';
import {DashboardRoutingModule} from "./dashboard-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppModule} from "../../app.module";
import { ClientComponent } from './home/client/client.component';
import {MatIcon} from "@angular/material/icon";
import {MatMenu, MatMenuTrigger} from "@angular/material/menu";
import { DeviceCardComponent } from './home/client/device-card/device-card.component';
import { UsersComponent } from './home/admin/users/users.component';
import { DevicesComponent } from './home/admin/devices/devices.component';
import { UserCardComponent } from './home/admin/users/user-card/user-card.component';
import { DeviceCardAComponent } from './home/admin/devices/device-card-a/device-card-a.component';
import { DeviceConsumptionStatsComponent } from './home/client/device-consumption-stats/device-consumption-stats.component';
import {NgxChartsModule} from "@swimlane/ngx-charts";

@NgModule({
  declarations: [
    AdminComponent,
    ClientComponent,
    DeviceCardComponent,
    UsersComponent,
    DevicesComponent,
    UserCardComponent,
    DeviceCardAComponent,
    DeviceConsumptionStatsComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule, // Important again!!! (see authentication.module.ts)
    ReactiveFormsModule,
    FormsModule,
    MatIcon,
    MatMenu,
    MatMenuTrigger,
    NgxChartsModule,
    //AppModule
  ]
})
export class DashboardModule { }
