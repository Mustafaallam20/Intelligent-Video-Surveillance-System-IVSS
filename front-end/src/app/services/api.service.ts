import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = "http://localhost:8081";
  constructor(private httpClient: HttpClient) { }

  public post(path: string, body: any = {}, httpHeaders?: Array<{ key: any, value: any }>): Observable<any> {

    let headers: HttpHeaders = new HttpHeaders();
    if (httpHeaders) {
      httpHeaders.forEach((header) => {
        headers = headers.set(header.key, header.value);
      });
    }
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
      console.log("Bearer "+localStorage.getItem('token')!)
    }
    headers = headers.set('Access-Control-Allow-Origin',this.baseUrl);
    console.log({headers})
    return this.httpClient
      .post(this.baseUrl + path, body,{headers})
  }

  public get(path: string, body: any = {}, httpHeaders?: [{ key: any, value: any }]): Observable<any> {

    let headers: HttpHeaders = new HttpHeaders();
    if (httpHeaders) {
      httpHeaders.forEach((header) => {
        headers = headers.set(header.key, header.value);
      });
    }
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
      console.log("Bearer "+localStorage.getItem('token')!)
    }
    headers = headers.set('Access-Control-Allow-Origin',this.baseUrl);
    console.log({headers})
    return this.httpClient
      .post(this.baseUrl + path, body,{headers})
  }
}
