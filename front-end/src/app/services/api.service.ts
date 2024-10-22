import { HttpClient, HttpHeaders ,HttpEvent, HttpRequest, HttpResponse, HttpEventType} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';




@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = "http://localhost:8081";
  constructor(private httpClient: HttpClient, private sanitizer: DomSanitizer) { }

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


  // public uploadFile(path: string, file: {}): Observable<any> {
  //   let headers: HttpHeaders = new HttpHeaders();
  //   if (localStorage.getItem('token') != null) {
  //     headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
  //     console.log("Bearer "+localStorage.getItem('token')!)
  //   }
  //   headers = headers.set('Access-Control-Allow-Origin',this.baseUrl);
  //   return this.httpClient.post(this.baseUrl + path, file, {
  //     headers,
  //     params: new HttpParams(),
  //     reportProgress: true,
  //   })
  // }

  uploadFile(file: File, model:string): Observable<HttpEvent<any>> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('model', model);
    let headers: HttpHeaders = new HttpHeaders();
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
      console.log("Bearer "+localStorage.getItem('token')!)
    }
    const req = new HttpRequest('POST', 'http://localhost:8081/api/videos/upload', formData, {
      headers,
      reportProgress: true
    });
    let res = this.httpClient.request(req);
    console.log(res)
    return res
  }


  // processStatus(id: number): Observable<HttpEvent<any>> {
  //   // const formData = new FormData();
  //   // formData.append('file', file);
  //   let headers: HttpHeaders = new HttpHeaders();
  //   if (localStorage.getItem('token') != null) {
  //     headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
  //     console.log("Bearer "+localStorage.getItem('token')!)
  //   }
  //   const req = new HttpRequest('POST', 'http://localhost:8081/api/videos/status/' + id.toString() , {
  //     headers,
  //     // reportProgress: true
  //   });

  //   return this.httpClient.request(req);
  // }
  // watchVideo(videoId:string): Observable<HttpEvent<any>> {

  //   let headers: HttpHeaders = new HttpHeaders();
  //   if (localStorage.getItem('token') != null) {
  //     headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
  //     console.log("Bearer "+localStorage.getItem('token')!)
  //   }
  //   const req = new HttpRequest('POST', 'http://localhost:8081/api/videos/watch/' + videoId, {
  //     headers,
  //   });

  //   return this.httpClient.request(req);
  // }

  watchVideo(videoId: number): Observable<SafeResourceUrl> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    });

    return this.httpClient.get('http://localhost:8081/api/videos/watch/' + videoId, {
      headers,
      responseType: 'blob',
      observe: 'events'
    }).pipe(
      filter((event: HttpEvent<any>) => event.type === HttpEventType.Response),
      map((event: HttpEvent<any>) => {
        const response = event as HttpResponse<Blob>;
        const blob = response.body;
        if (blob !== null && blob !== undefined) {
          const url = URL.createObjectURL(blob);
          return this.sanitizer.bypassSecurityTrustResourceUrl(url);
        } else {
          throw new Error('Video response body is null or undefined');
        }
      })
    );
  }





  downloadFile(fileUrl: string, type:string): void {
    let headers: HttpHeaders = new HttpHeaders();
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
      console.log("Bearer "+localStorage.getItem('token')!)
    }
    this.httpClient.get(fileUrl, { headers, responseType: 'blob' })
      .subscribe(response => {
        this.saveFile(response, type);
      }, error => {
        console.error('Error downloading the file:', error);
      });
  }
  private saveFile(response: any, type:string): void {
    const blob = new Blob([response], { type: response.type });

    const downloadLink = document.createElement('a');
    downloadLink.href = URL.createObjectURL(blob);
    if(type=='video')
      downloadLink.download = 'output-video.mp4'; // Replace with desired file name
    if(type=='image')
      downloadLink.download = 'output-image.jpg'; // Replace with desired file name
    downloadLink.click();
    URL.revokeObjectURL(downloadLink.href);
    downloadLink.remove();
  }



}
