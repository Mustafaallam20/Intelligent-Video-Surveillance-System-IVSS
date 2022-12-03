import { Component, OnInit } from '@angular/core';
import { FormGroup , FormControl } from '@angular/forms';
import {ToastService} from '../services/toast.service'
import { FormBuilder, Validators, NgForm } from '@angular/forms';
import { User } from '../models/user';
import {AuthService} from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private toastService: ToastService , private authService : AuthService, private _Router: Router) { }
  isSubmitted = false;
  loginForm : FormGroup = new FormGroup({
    email: new FormControl(null,[Validators.required,Validators.email]),
    password: new FormControl(null,[Validators.required,Validators.minLength(10)]),
  })

  loginSubmit(loginForm:FormGroup){
    console.log("hello");
    this.isSubmitted = true;
    if (this.loginForm.valid)
    {

      var user:User ={
        first_name:'', last_name:'', email:this.email,password:this.password

      }
      this.authService.login(user);
      this._Router.navigate(['/home']);
    }
    else{
      this.toastService.error("Please add All Fields");
    }

  }
  ngOnInit(): void {
  }

  get email(): any {
    return this.loginForm.get('email');
  }
  get password(): any {
    return this.loginForm.get('password');
  }

}
