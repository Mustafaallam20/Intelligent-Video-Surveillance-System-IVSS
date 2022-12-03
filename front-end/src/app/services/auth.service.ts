import { Injectable } from '@angular/core';
import { User } from '../models/user';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }
signUp (user:User)
{
  console.log("hello Signup");
}

login (user:User)
{
  console.log("hello Login");
}



}
