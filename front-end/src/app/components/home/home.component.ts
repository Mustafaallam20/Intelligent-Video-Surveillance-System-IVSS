import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private authService:AuthService, private router:Router) {

    if (this.authService.checkAuth()) {

      this.router.navigate(['/', 'login']);

    }
  }

  ngOnInit(): void {

    this.authService.test().subscribe(
      (result:any) => {
        console.log(result);
      }
    );
    console.log(this.authService.getAuth())
  }



}
