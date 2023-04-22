import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private authService : AuthService) { }
  isLogedIn = !this.authService.checkAuth();
  ngOnInit(): void {
    this.isLogedIn=!this.authService.checkAuth();
  }

  logOut()
  {
    this.authService.logOut();
  }
}
