import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = "http://localhost:8081/api/auth/";
  constructor(private httpClient: HttpClient) { }

  public post(path: string, body: any = {}, httpHeaders?: [{ key: any, value: any }]): Observable<any> {
    return this.httpClient
      .post(this.baseUrl + path, body)
  }
}
