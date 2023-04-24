import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Video } from 'src/app/models/video';
import { AuthService } from 'src/app/services/auth.service';
import { VideoserviceService } from 'src/app/services/videoservice.service';


@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  vidHistory:Video[]=[];

  constructor(_videoService:VideoserviceService,private authService:AuthService,private router:Router) {
    this.vidHistory = _videoService.history;

    if (this.authService.checkAuth()) {

      this.router.navigate(['/', 'login']);

    }
  }

  ngOnInit(): void {

  }

  delete(e:any):void {
    this.vidHistory = this.vidHistory.filter((item)=>item.id!=e.target.parentElement.parentElement.id)
    // send to server to delete vid from history
  }

  view(e:any):void {
    let vidIndex = this.vidHistory.findIndex((item)=>item.id==e.target.parentElement.parentElement.id)
    this.vidHistory[vidIndex].isNew=false
      // send to server to delete vid from history
  }

}
