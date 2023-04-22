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
  loginForm : FormGroup = new FormGroup({
    email: new FormControl(null,[Validators.required,Validators.email]),
    password: new FormControl(null,[Validators.required,Validators.minLength(10)]),
  })

  loginSubmit(loginForm:FormGroup){

    console.log("hello");
    this.isSubmitted = true;
    if (this.loginForm.valid)
    {
      console.log(this.email);

      var user:User ={usernameOrEmail:this.email.value,password:this.password.value}
      //const user=loginForm.value;
      console.log(user)
      this.authService.login(user).subscribe(
        (result:any) => {
          console.log(result);
          if(result.status =="success" ){

            this.authService.setAuth(result.token);
           // this.authService.setUserData();

             this.router.navigate(['/', 'home']);
          }
          else{
            this.toastService.error("Error Occured");

          }

        }
      );

    }
    else{
      this.toastService.error("Please add All Fields");
      console.log("hey");
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
