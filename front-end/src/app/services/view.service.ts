import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';


import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class ViewService {

  private apiUrl = 'http://localhost:8081/api/videos/watch/';

  constructor(private apiService: ApiService ,private http: HttpClient) { }

  // method to get the authenticated video stream
  // getAuthenticatedVideoStream(vidId: string): Observable<any> {
  //   const headers = new HttpHeaders({
  //     'Authorization': 'Bearer ' + localStorage.getItem('access_token'),
  //     'Content-Type': 'application/json'
  //   });
  //   const options = { headers: headers };
  //   return this.http.post(this.apiUrl + vidId, {}, options);
  // }
  watchVideo(vidId: string): Observable<Blob> {
    return this.apiService.post('/watch/' + vidId, {}, [{ key: 'responseType', value: 'blob' }]).pipe(
      map(response => response.body)
    );;
  }



}
