import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ApiService } from './../../services/api.service';
import { HttpClient, HttpHeaders ,HttpEvent, HttpRequest, HttpResponse, HttpEventType} from '@angular/common/http';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';





import { ViewService } from 'src/app/services/view.service';
import { Observable } from 'rxjs';


// import { DomSanitizer, SafeUrl } from '@angular/platform-browser';


@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {
  videoUrl: SafeUrl | string = '';
  videoId: number = -1;
  videoLink: string = '';
  isLoaded:boolean=false;
  showMsg:boolean=false;
  msg:string="";
  constructor(private viewService: ViewService,
     private authService: AuthService, 
     private activatedRoute: ActivatedRoute,
     private apiService: ApiService,
     private httpClient: HttpClient, 
     private sanitizer: DomSanitizer
     ) {  }
  // imgList = {
  //   "img1": "https://placehold.co/120x120",
  //   "img2": "https://placehold.co/120x120",
  //   "img3": "https://placehold.co/120x120"};
  ngOnInit() {
    this.activatedRoute.queryParams.subscribe((params) => {
      this.videoId = params['videoId'];
  
      if (this.videoId != -1) {
        this.viewVideo("http://localhost:8081/api/videos/watch/"+this.videoId.toString());
      }
    });
  }

  download(){
    this.apiService.downloadFile("http://localhost:8081/api/videos/watch/"+this.videoId.toString());
  }


  viewVideo(fileUrl: string): void {
    let headers: HttpHeaders = new HttpHeaders();
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
    }
    this.httpClient.get(fileUrl, { headers, responseType: 'blob' })
    .subscribe(response => {
        this.playVideo(response);
        this.isLoaded=true;
      }, error => {
        console.error('Error downloading the file:', error);
        this.showMsg=true;
        this.msg=error.message;
      });  
  }

  playVideo(response: any): void {
    console.log('testx1')
    const blob = new Blob([response], { type: response.type });
    console.log('testx2')
    this.videoUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));

    // this.videoUrl = URL.createObjectURL(blob);
    console.log('testx3')
  }


  
  
    
}


  

  

