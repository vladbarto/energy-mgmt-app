import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterModule} from "@angular/router";
import {HttpClientModule, provideHttpClient, withInterceptors} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NotFoundComponent } from './shared/components/not-found/not-found.component';
import {HeaderComponent} from "./shared/components/header/header.component";
import {requestInterceptor} from "./core/http-interceptor/http-interceptor";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    AppRoutingModule,
    BrowserAnimationsModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([requestInterceptor])),
    provideAnimationsAsync(),
    provideCharts(withDefaultRegisterables())
  ],
  exports: [

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
