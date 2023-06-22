import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ApiService } from './../../services/api.service';
import { HttpClient, HttpHeaders ,HttpEvent, HttpRequest, HttpResponse, HttpEventType} from '@angular/common/http';
import { DomSanitizer, SafeUrl, SafeResourceUrl } from '@angular/platform-browser';
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
  mainURL: SafeResourceUrl = "";
  mainIndex: number = 0;
  videoId: number = -1;
  videoLink: string = '';
  isVidLoaded:boolean=false;
  isImgLoaded:boolean=false;
  showMsg:boolean=false;
  msg:string="";
  msgBtnTxt1:string="";
  msgBtnTxt2:string="";
  msgBtnLink1:string="";
  msgBtnLink2:string="";
  imgsURL:any=[];
  imgsURLPlan:any=[];
  processingPresentage:number=0;
  videoURLHack:string='';

  options = [
    { value: 'all', label: 'All Detection Types' },
    { value: 'car', label: 'Car Cresh Detection' },
    { value: 'fall', label: 'Fall Detection' },
    { value: 'face', label: 'Face Recognation' }
  ];
  
  selectedOption: string = "all";

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
          console.log("170: "+this.videoId)
          this.viewVideo("http://localhost:8081/api/videos/watch/"+this.videoId.toString());
          this.getImages(this.videoId)
          this.viewVideoS("/api/videos/watchs/"+this.videoId.toString())
          console.log(this.videoId)
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
    this.apiService.downloadFile("http://localhost:8081/api/videos/watch/"+this.videoId.toString(), 'video');
  }

  downloadImg(){
    if(this.mainIndex!=0){
      this.apiService.downloadFile(this.imgsURLPlan[this.mainIndex], 'image');
    }
  }

  downloadAllImgs(){
    this.imgsURLPlan.forEach((element:any)=>{
      if(this.mainIndex!=0){
        this.apiService.downloadFile(element, 'image');
      }
    })

  }


  viewVideoS(fileUrl: string): void {
    let headers: HttpHeaders = new HttpHeaders();
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
    }
    console.log("aaaaa", fileUrl);
    

    this.apiService.post(fileUrl, {}).subscribe(
    (response: any) => {
        this.videoURLHack = "/assets/videos/" + response.status;
        this.isVidLoaded=true;
        console.log("aaaa", response ,this.videoURLHack);
      }) 
      console.log("aaaa" ,this.videoURLHack);
  }

  viewVideo(fileUrl: string): void {
    let headers: HttpHeaders = new HttpHeaders();
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
    }
    this.httpClient.get(fileUrl, { headers, responseType: 'blob' })
    .subscribe(response => {
        this.playVideo(response);
        this.isVidLoaded=true;
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
    this.mainURL = this.sanitizer.bypassSecurityTrustResourceUrl(URL.createObjectURL(blob));
    this.imgsURL.push(this.mainURL)
    this.imgsURLPlan.push(this.mainURL)
    // this.mainURL = this.sanitizer.bypassSecurityTrustResourceUrl('data:video/mp4;base64,' + URL.createObjectURL(blob));


    // this.mainURL = URL.createObjectURL(blob);
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

  getImages(id:any):void {
    while(this.imgsURL.length>1){
      this.imgsURL.pop();
      this.imgsURLPlan.pop();
    }
    this.historyService.getVideoMetadata(id.toString()).subscribe( response => {
      console.log("172: "+this.videoId)
      console.log(response)
      console.log(response)
      if(this.selectedOption=='fall' || this.selectedOption=='all')
        response.fallImgPath.forEach((img: string) => {
          this.getImage("http://localhost:8081/api/videos/getImg/" + img)
        });
      if(this.selectedOption=='face' || this.selectedOption=='all')
        response.fightImgPath.forEach((img: string) => {
          this.getImage("http://localhost:8081/api/videos/getImg/" + img)
        });
        if(this.selectedOption=='face' || this.selectedOption=='all')
        response.faceImgPath.forEach((img: string) => {
          this.getImage("http://localhost:8081/api/videos/getImg/" + img)
        });
      if(this.selectedOption=='car' || this.selectedOption=='all')
        response.crashImgPath.forEach((img: string) => {
          this.getImage("http://localhost:8081/api/videos/getImg/" + img)
        });
      console.log("173: "+this.imgsURL)
    }
  )
  }


  getImage(fileUrl: string): void {
    console.log("174: "+fileUrl);
    let headers: HttpHeaders = new HttpHeaders();
    if (localStorage.getItem('token') != null) {
      headers = headers.set("Authorization","Bearer "+localStorage.getItem('token')!);
    }
    this.httpClient.get(fileUrl, { headers, responseType: 'blob' })
    .subscribe(response => {
        const blob = new Blob([response], { type: response.type });
        this.imgsURL.push(this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob)))
        this.imgsURLPlan.push(fileUrl);
        console.log("176: "+this.imgsURL, typeof(this.imgsURL));
        this.isVidLoaded=true;
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

  view(index: number) {
    this.mainIndex=index;
    this.mainURL=this.imgsURL[index]
    this.isImgLoaded = true;
    this.isVidLoaded = false;
    if(index.toString()=='0'){
      this.isImgLoaded = false;
      this.isVidLoaded = true;
    }
}

left() {
  if(this.mainIndex<this.imgsURL.length-1){
    this.mainIndex+=1
    this.mainURL=this.sanitizer.bypassSecurityTrustResourceUrl(this.imgsURL[this.mainIndex])
    this.isImgLoaded = true;
    this.isVidLoaded = false;
    if(this.mainIndex==0){
      this.isImgLoaded = false;
      this.isVidLoaded = true;
    }
  }
}

right() {
  if(this.mainIndex>0){
    this.mainIndex-=1
    this.mainURL=this.sanitizer.bypassSecurityTrustResourceUrl(this.imgsURL[this.mainIndex])
    this.isImgLoaded = true;
    this.isVidLoaded = false;
    if(this.mainIndex==0){
      this.isImgLoaded = false;
      this.isVidLoaded = true;
    }
  }
}

updateURLs(){
  this.getImages(this.videoId);
}


  
  
    
}


  

  

