import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { HistoryComponent } from './components/history/history.component';
import { HomeComponent } from './components/home/home.component';
import { ViewComponent } from './components/view/view.component';

const routes: Routes = [

  {path:'' , redirectTo:'home', pathMatch: 'full'},
  {path:'login', component:LoginComponent},
  {path: 'signup',component:SignupComponent},
  {path:'home' , component:HomeComponent},
  {path:'view' , component:ViewComponent},
  {path:'history' , component:HistoryComponent},

  // {path:'' , redirectTo:'home', pathMatch: 'full'},
  // {path:'login', component:LoginComponent},
  // {path: 'signup',component:SignupComponent},
  // {path:'home' , canActivate:[AuthGuard], component:HomeComponent},
  // {path:'view' , canActivate:[AuthGuard], component:ViewComponent},
   //{path:'history' , canActivate:[AuthGuard], component:HistoryComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
