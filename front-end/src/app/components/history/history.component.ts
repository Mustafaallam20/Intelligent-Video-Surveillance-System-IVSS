import { Component, OnInit } from '@angular/core';
// import { Router } from '@angular/router';
import { Router, NavigationExtras } from '@angular/router';
import { Video } from 'src/app/models/video';
import { AuthService } from 'src/app/services/auth.service';
import { VideoserviceService } from 'src/app/services/videoservice.service';
import { HistoryService } from 'src/app/services/history.service';
import { ApiService } from './../../services/api.service';




@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  videos: any = [];
  deleteFlag:boolean =false;
  selectedVidId:any;
  downloadFlag:boolean=false;
  DaownloadVideoId:any

  detectionOptions = [
    { value: 'all', label: 'All Detection Types' },
    { value: 'car', label: 'Car Cresh Detection' },
    { value: 'fall', label: 'Fall Detection' },
    { value: 'face', label: 'Face Recognation' }
  ];
  statusOptions = [
    { value: 'all', label: 'New & Viewed' },
    { value: '0', label: 'New' },
    { value: '1', label: 'Viewed' },
  ];
  
  detectionSelectedOption: string = "all";
  statusSelectedOption: string = "all";


  constructor(private historyService:HistoryService,private authService:AuthService,private router:Router, private apiService: ApiService,
    ) {

    // if (this.authService.checkAuth()) {

    //   this.router.navigate(['/', 'login']);

    // }
  }

  ngOnInit(): void {
    this.historyService.getAllVideos().subscribe(response => {
      console.log(response);
      for (let key in response.data) {
        // response.data[key].uploadDate = response.data[key].uploadDate.toLocaleStr
        this.historyService.getVideoMetadata((response.data[key]).toString()).subscribe( response => {
          console.log(response.uploadDate)

          response.uploadDate = this.formatDate(new Date(response.uploadDate))
          this.videos.push(response)
          console.log(response)
        }
      )}
    })
  }

  // getVideoMetadata(e:any):void {
  //   this.historyService.getVideoMetadata(e.id).subscribe(response => {
  //     console.log(response)
  //   })
  // }

  onDelete(e:any):void {
      this.selectedVidId = e.target.parentElement.parentElement.id;
      this.deleteFlag=true;
  }
  cancel(e:any):void {
    e.target.parentElement.parentElement.id;
    this.deleteFlag=false;
    this.downloadFlag=false;

}
  confirmDelete():void {
      this.historyService.delete(this.selectedVidId).subscribe( response => {
      this.videos = this.videos.filter((item: any)=>item.id!=this.selectedVidId)
      this.deleteFlag=false;
    })
  }

  onView(e:any):void {
    console.log(e.target.parentElement.parentElement.id)
    this.historyService.view(e.target.parentElement.parentElement.id).subscribe( response => {
      console.log(response)
      let vidIndex = this.videos.findIndex((item: any)=>item.id==e.target.parentElement.parentElement.id)
      console.log(vidIndex)
      this.videos[vidIndex].watched=true

      this.router.navigate(['/view'], { queryParams: { 'videoId': this.videos[vidIndex].id } });

    })
  }
  onDownload(e:any):void{
    this.DaownloadVideoId = e.target.parentElement.parentElement.id;

    this.downloadFlag=true;
  }
  downloadVideo(){
    this.apiService.downloadFile("http://localhost:8081/api/videos/watch/"+this.DaownloadVideoId, 'video');
  }
  downloadImages(){
    
    const imgsURLPlan:any = [];
    this.historyService.getVideoMetadata((this.DaownloadVideoId).toString()).subscribe( response => {
      response.fallImgPath.forEach((img: string) => {
        imgsURLPlan.push("http://localhost:8081/api/videos/getImg/" + img)
      });
      response.faceImgPath.forEach((img: string) => {
        imgsURLPlan.push("http://localhost:8081/api/videos/getImg/" + img)
      });
      response.crashImgPath.forEach((img: string) => {
        imgsURLPlan.push("http://localhost:8081/api/videos/getImg/" + img)
      });
    });
    imgsURLPlan.forEach((element:any)=>{
        this.apiService.downloadFile(element, 'image');
    })
  }




  formatDate(date:Date): string {
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const year = date.getFullYear();
      const hour = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${day}/${month}/${year} - ${hour}:${minutes}`;
  }


  updateDetection(){
    this.ngOnInit()
  }
  updateStatus(){
    this.ngOnInit()
  }
  
}
