import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import {AuthenticationRoutingModule} from "./authentication-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { DialogOverviewExampleComponent } from './login/dialog-overview-example/dialog-overview-example.component';
import {MatDialogActions, MatDialogClose, MatDialogContent} from "@angular/material/dialog";
import {MatFormField} from "@angular/material/form-field";


@NgModule({
  declarations: [
    LoginComponent,
    DialogOverviewExampleComponent
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule, // !! Important, ca sa functioneze rutarea in Authentication step
    ReactiveFormsModule,
    MatDialogContent,
    MatFormField,
    MatDialogActions,
    MatDialogClose,
    FormsModule
  ]
})
export class AuthenticationModule { }
