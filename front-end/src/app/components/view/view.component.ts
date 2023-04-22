import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {

  constructor(private authService:AuthService, private router:Router) {

    if (this.authService.checkAuth()) {

      this.router.navigate(['/', 'login']);

    }
  }

  ngOnInit(): void {
    console.log(this.authService.getAuth())
  }

}
