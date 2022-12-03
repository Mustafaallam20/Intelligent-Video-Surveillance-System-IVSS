import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HistoryComponent } from './history/history.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ViewComponent } from './view/view.component';

const routes: Routes = [
  {path: 'login',component:LoginComponent },
  {path: 'signup',component:SignupComponent},
  {path: 'home',component:HomeComponent },
  {path: 'view',component:ViewComponent },
  {path: 'history',component:HistoryComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
