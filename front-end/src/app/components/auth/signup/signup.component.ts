import { Component, OnInit } from '@angular/core';
import { FormGroup , FormControl } from '@angular/forms';
import {ToastService} from '../../../services/toast.service'
import { FormBuilder, Validators, NgForm } from '@angular/forms';
import { User } from '../../../models/user';
import {AuthService} from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  constructor(private toastService :ToastService ,private formBuilder:FormBuilder , private authService:AuthService,private router:Router ) { }


  isSubmitted = false;

  registerForm : FormGroup = new FormGroup({

    name: new FormControl(null, [Validators.required, Validators.pattern('[a-zA-Z ]*'),Validators.minLength(3)]),
    username: new FormControl(null,[Validators.required, Validators.minLength(3)]),
    email: new FormControl(null,[Validators.required,Validators.email]),
    password: new FormControl(null,[Validators.required,Validators.minLength(8)]),
  })

  submitRegister(registerForm:FormGroup){
    this.isSubmitted = true;
    if(this.registerForm.valid)
    {

      var user:User ={name:this.name.value,username:this.username.value, email:this.email.value,password:this.password.value}
      this.authService.signup(user).subscribe((response: any) => {
        console.log("response"+response);

        if(response.status == "Success"){


           this.router.navigate(['/', 'login']);
        }
        else{
          this.toastService.error(response.status);

        }


      });
    }
    else{
      this.isSubmitted = false;
      this.toastService.error("error");

    }

  }

  get username(): any {
    return this.registerForm.get('username');
  }
  get name(): any {
    return this.registerForm.get('name');
  }
  get email(): any {
    return this.registerForm.get('email');
  }
  get password(): any {
    return this.registerForm.get('password');
  }

  ngOnInit() {

  }






}
