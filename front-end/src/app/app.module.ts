import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { ViewComponent } from './components/view/view.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './components/home/home.component';
import { HistoryComponent } from './components/history/history.component';
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    SignupComponent,
    NavbarComponent,
    ViewComponent,
    HistoryComponent

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ToastModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule


  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
