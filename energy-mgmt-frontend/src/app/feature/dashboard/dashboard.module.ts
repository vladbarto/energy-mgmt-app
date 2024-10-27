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


@NgModule({
  declarations: [
    AdminComponent,
    ClientComponent,
    DeviceCardComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule, // Important again!!! (see authentication.module.ts)
    ReactiveFormsModule,
    FormsModule,
    MatIcon,
    MatMenu,
    MatMenuTrigger,
    //AppModule
  ]
})
export class DashboardModule { }
