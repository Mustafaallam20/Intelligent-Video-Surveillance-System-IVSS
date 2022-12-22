import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../../services/auth.service';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private authService : AuthService) { }
  isLogedIn = false;
  ngOnInit(): void {
    
  }

  SignUp()
  {
    this.authService.logOut();
  }
  LogIn()
  {
    this.authService.logOut();
  }
  LogOut()
  {
    this.authService.logOut();
  }
}
