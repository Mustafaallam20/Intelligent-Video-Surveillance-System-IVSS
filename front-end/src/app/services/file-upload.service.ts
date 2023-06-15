import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpHeaders, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  constructor(private http: HttpClient) { }

  // uploadFile(file: File): Observable<HttpEvent<any>> {
  //   const formData = new FormData();
  //   formData.append('file', file);
  //   let headers: HttpHeaders = new HttpHeaders();
  //   if (localStorage.getItem('token') != null) {
  //     headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
  //     console.log("Bearer "+localStorage.getItem('token')!)
  //   }
  //   const req = new HttpRequest('POST', 'http://localhost:8081/api/videos/upload', formData, {
  //     headers,
  //     reportProgress: true
  //   });

  //   return this.http.request(req);
  // }
}
