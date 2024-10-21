import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './home/admin/admin.component';
import {DashboardRoutingModule} from "./dashboard-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppModule} from "../../app.module";
import {AppRoutingModule} from "../../app-routing.module";
import { ClientComponent } from './home/client/client.component';



@NgModule({
  declarations: [
    AdminComponent,
    ClientComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule, // Important again!!! (see authentication.module.ts)
    ReactiveFormsModule,
    FormsModule
  ]
})
export class DashboardModule { }
