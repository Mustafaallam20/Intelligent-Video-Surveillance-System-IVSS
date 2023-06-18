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
  msgBtnTxt1:string="";
  msgBtnTxt2:string="";
  msgBtnLink1:string="";
  msgBtnLink2:string="";
  processingPresentage:number=0;
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

  getPercent(): any{
    return this.apiService.processStatus(0).subscribe(
      (res: any) => {
        // console.log(res)
        // if (res.status == "processing") {
        //   this.processingPresentage = res.percent;
        //   // console.log(this.processingPresentage)
        // } else if (res.status == "failed") {
        //   // console.log(res)
        // } else if (res.status == "finished") {
        //   // console.log(res)
        // }
        // console.log(this.processingPresentage)
        // if(this.processingPresentage<100)
        // setTimeout(() => {
        //   this.getPercent()
        // }, 15000);

      },
      (error: any) => {
        console.log('Upload error:', error);
        this.msg = 'An error occurred during file upload. Please try again.';
      }
    )
  }
  


  ngOnInit() {
    // if (this.videoId == undefined){
    if (1){
      this.showMsg=true;
      this.msg="No video file selected to view, Select file or upload a new one."
      this.msgBtnTxt1="Select from uploaded";
      this.msgBtnTxt2="Upload a new video";
      this.msgBtnLink1="/history";
      this.msgBtnLink2="/home";
      this.processingPresentage = 0;
      let res = this.getPercent()
      console.log(res)
      console.log("done...")
          
    }else{
      this.activatedRoute.queryParams.subscribe((params) => {
        this.showMsg=false;
        this.videoId = params['videoId'];
        if (this.videoId != -1) {
          this.viewVideo("http://localhost:8081/api/videos/watch/"+this.videoId.toString());
        }else{
          this.showMsg=true;
          this.msg="Video not found."
          this.msgBtnTxt1="Select from uploaded";
          this.msgBtnTxt2="Upload a new video";
          this.msgBtnLink1="/history";
          this.msgBtnLink2="/home";
        }
      });
    }

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
        this.msg="Faild to view video, please try leter."
        this.msgBtnTxt1="Try again.";
        this.msgBtnTxt2="Upload a new video";
        this.msgBtnLink1="/";
        this.msgBtnLink2="/home";
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

  closeTost():void{
    this.showMsg= false;
  }

  
  
    
}


  

  

