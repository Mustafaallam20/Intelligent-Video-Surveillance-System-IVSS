import { Component, OnInit } from '@angular/core';
import { FormGroup , FormControl } from '@angular/forms';
import {ToastService} from '../../../services/toast.service'
import { FormBuilder, Validators, NgForm } from '@angular/forms';
import { User } from '../../../models/user';
import {AuthService} from '../../../services/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private toastService: ToastService , private authService : AuthService, private router:Router ) { }
    isSubmitted = false;
    ToastFlag = false;
    ToastMsg:string="";
    loginForm : FormGroup = new FormGroup({
    email: new FormControl(null,[Validators.required,Validators.email]),
    password: new FormControl(null,[Validators.required,Validators.minLength(8)]),
  })

  loginSubmit(loginForm:FormGroup){
    this.isSubmitted = true;
    if (this.loginForm.valid)
    {
      var user:User ={usernameOrEmail:this.email.value,password:this.password.value}
      this.authService.login(user).subscribe(
        (result:any) => {
          if(result.status =="Success" ){

            this.authService.setAuth(result.token);

            this.authService.setUserName(result.userName)
           // this.authService.setUserData();

             this.router.navigate(['/', 'home']);
          }
          else{
            this.isSubmitted = false;
            this.ToastFlag= true;
            this.ToastMsg="Wrong email or password." 
          }
        }
      );

    }
    else{
      this.isSubmitted = false;
      this.ToastFlag= true;
      this.ToastMsg="Invalid emial or password" 
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
  closeTost():void{
    this.ToastFlag= false;
  }

}
