import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { ApiService } from './api.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import jwtDecode from 'jwt-decode';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService  {

  constructor(private httpClient: HttpClient ,private apiService:ApiService, private _Router: Router)
  {
    if (localStorage.getItem('userToken') != null)
    {
      this.setUserData();
    }
  }


login(user: User): Observable<any> {

      return this.apiService.post("login",user).pipe(
        map((result: any) => {
          console.log(result);

          return result;

        })
      );


}

userData = new BehaviorSubject(null);
setUserData(): void
{
  let encodedToken = JSON.stringify(localStorage.getItem('userToken'));
  let decodedToken:any = jwtDecode(encodedToken);
  this.userData.next(decodedToken);
}

logOut(): void
{
  localStorage.removeItem('userToken');
  this.userData.next(null);
  this._Router.navigate(['/login']);
}


public checkAuth ():boolean
{
  return localStorage.getItem("token")!=null;
}

public setAuth ( token: any)
{
   localStorage.setItem("token ", token);
}



signup(user: User): Observable<any> {
  return this.apiService.post("signup",user).pipe(
    map((result: any) => {
      console.log(result);

      return result;

    })
  );


  return this.apiService.post("signup", user).pipe(
    map(
      (result) => {
        return result;
      }
    )
  );

}

}


