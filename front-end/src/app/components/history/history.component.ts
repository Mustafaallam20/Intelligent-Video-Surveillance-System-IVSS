import { Component, OnInit } from '@angular/core';
// import { Router } from '@angular/router';
import { Router, NavigationExtras } from '@angular/router';
import { Video } from 'src/app/models/video';
import { AuthService } from 'src/app/services/auth.service';
import { VideoserviceService } from 'src/app/services/videoservice.service';
import { HistoryService } from 'src/app/services/history.service';



@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  videos: any = [];
  deleteFlag:boolean =false;
  deleteVideoId:any;

  constructor(private historyService:HistoryService,private authService:AuthService,private router:Router) {

    if (this.authService.checkAuth()) {

      this.router.navigate(['/', 'login']);

    }
  }

  ngOnInit(): void {
    this.historyService.getAllVideos().subscribe(response => {
      console.log(response);
      for (let key in response.data) {
        // response.data[key].uploadDate = response.data[key].uploadDate.toLocaleStr
        this.historyService.getVideoMetadata((response.data[key]).toString()).subscribe( response => {
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
      this.deleteVideoId = e.target.parentElement.parentElement.id;
      this.deleteFlag=true;
  }
  cancelDelete(e:any):void {
    e.target.parentElement.parentElement.id;
    this.deleteFlag=false;
}
  confirmDelete():void {
      this.historyService.delete(this.deleteVideoId).subscribe( response => {
      this.videos = this.videos.filter((item: any)=>item.id!=this.deleteVideoId)
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
  
}
