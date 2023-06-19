import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ApiService } from './../../services/api.service';
import { HttpClient, HttpHeaders ,HttpEvent, HttpRequest, HttpResponse, HttpEventType} from '@angular/common/http';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { HistoryService } from 'src/app/services/history.service';





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
     private sanitizer: DomSanitizer,
     private historyService:HistoryService
     ) {  }
  // imgList = {
  //   "img1": "https://placehold.co/120x120",
  //   "img2": "https://placehold.co/120x120",
  //   "img3": "https://placehold.co/120x120"};

  // getPercent(): any{
  //   this.apiService.post("/api/videos/status/0").subscribe(res=>{
  //     console.log(res)
  //     if (res.status == "processing") {
  //       this.processingPresentage = res.percent;
  //       console.log(this.processingPresentage)
  //     } else if (res.status == "failed") {
  //        console.log(res)
  //     } else if (res.status == "finished") {
  //       console.log(res)
  //     }
  //     console.log(this.processingPresentage)
  //     if(this.processingPresentage<100)
  //     setTimeout(() => {
  //       this.getPercent()
  //     }, 5000);
  //   })
  // }
  


  ngOnInit() {
    if (this.videoId == undefined){
    // if (1){
      this.showMsg=true;
      this.msg="No video file selected to view, Select file or upload a new one."
      this.msgBtnTxt1="Select from uploaded";
      this.msgBtnTxt2="Upload a new video";
      this.msgBtnLink1="/history";
      this.msgBtnLink2="/home";
      // this.processingPresentage = 0;
      // let res = this.getPercent()

          
    }else{
      this.activatedRoute.queryParams.subscribe((params) => {
        this.showMsg=false;
        this.videoId = params['videoId'];
        if (this.videoId != -1) {
          this.viewVideo("http://localhost:8081/api/videos/watch/"+this.videoId.toString());
          this.getImgs(this.videoId)
          
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
    // this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl('data:video/mp4;base64,' + URL.createObjectURL(blob));


    // this.videoUrl = URL.createObjectURL(blob);
    console.log('testx3')
  }

  closeTost():void{
    this.showMsg= false;
  }

  // getImage() {
  //   const imagePath = 'path-to-your-image';
  //   this.imageService.getImage(imagePath).subscribe(
  //     (response) => {
  //       const reader = new FileReader();
  //       reader.onloadend = () => {
  //         this.imageSource = reader.result as string;
  //       };
  //       reader.readAsDataURL(response);
  //     },
  //     (error) => {
  //       console.error('Failed to load image:', error);
  //     }
  //   );
  // }

  getImgs(id:any):void {
    this.historyService.getVideoMetadata(id.toString()).subscribe( response => {
      console.log(response)
      this.getImg(response.fallImgPath[0])
    }
  )
  }

  getImg(img:any):void {
    this.viewVideo("http://localhost:8081/api/videos/getImg/"+img);
    // this.historyService.getVideoMetadata(id.toString()).subscribe( response => {
    //   console.log(response)
    //   console.log(response)
    // }
  // )
  }


  
  
    
}


  

  

