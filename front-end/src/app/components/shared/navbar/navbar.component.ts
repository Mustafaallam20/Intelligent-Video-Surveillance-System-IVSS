import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  constructor(private authService : AuthService, private router: Router) { 
    if (this.authService.checkAuth()) {
      this.router.navigate(['/', 'login']);
    }
  }
  isLogedIn = !this.authService.checkAuth();
  userName:string =this.authService.getUserName();
  showSignUp = this.router.url=="/login"

  ngOnInit(): void {
    console.log(this.router.url)
    this.userName!=this.authService.getUserName();
    this.isLogedIn=!this.authService.checkAuth();
  }

  logOut()
  {
    console.log("loooooogout")
    this.authService.logOut();
  }
}
