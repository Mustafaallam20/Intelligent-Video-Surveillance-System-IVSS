import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { HistoryComponent } from './history/history.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ViewComponent } from './view/view.component';

const routes: Routes = [
  {path:'' , redirectTo:'home', pathMatch: 'full'},
  {path:'login', component:LoginComponent},
  {path: 'signup',component:SignupComponent},
  {path:'home' , canActivate:[AuthGuard], component:HomeComponent},
  {path:'view' , canActivate:[AuthGuard], component:ViewComponent},
  {path:'history' , canActivate:[AuthGuard], component:HistoryComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
