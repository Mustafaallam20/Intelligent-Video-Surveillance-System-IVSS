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

    first_name: new FormControl(null, [Validators.required]),
    last_name: new FormControl(null,Validators.required),
    email: new FormControl(null,[Validators.required,Validators.email]),
    password: new FormControl(null,[Validators.required,Validators.minLength(10)]),
  })

  submitRegister(registerForm:FormGroup){
    this.isSubmitted = true;
    if(this.registerForm.valid)
    {
      var user:User ={
        first_name:this.firstName.value, last_name:this.lastName.value,email:this.email.value,password:this.password.value

      }
      this.authService.signup(user).subscribe((response: any) => {
        console.log("response"+response);

        if(response != null ){


           this.router.navigate(['/', 'login']);
        }
        else{
          this.toastService.error("Error Occured");

        }


      });
    }
    else{
      this.toastService.error("error");

    }

  }

  get firstName(): any {
    return this.registerForm.get('first_name');
  }
  get lastName(): any {
    return this.registerForm.get('last_name');
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
