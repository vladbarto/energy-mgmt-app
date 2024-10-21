import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import {AuthenticationRoutingModule} from "./authentication-routing.module";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule, // !! Important, ca sa functioneze rutarea in Authentication step
    ReactiveFormsModule
  ]
})
export class AuthenticationModule { }
