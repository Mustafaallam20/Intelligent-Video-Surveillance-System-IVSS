import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { ApiService } from './api.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private apiService:ApiService)  { }


login(user: User): Observable<any> {
  console.log("hello Login from aueh service");
  return this.apiService.post("login",user).pipe(
    map((result: any) => {
      console.log(result);
     /* if (result !=null)
      {
        // we want to store token that returned from the login end point
        this.setAuth();
      }
      else{
        console.log("resulr is null");

      }*/
      return result;

    })
  );

}


public checkAuth ():boolean
{
  return localStorage.getItem("token")!=null;
}

public setAuth ()
{
   localStorage.setItem("token ","tokenFormBackEnd")
}



signup(user: User): Observable<any> {
  return this.apiService.post("signup",user).pipe(
    map((result: any) => {
      console.log(result);
 
      return result;

    })
  );

}

}


