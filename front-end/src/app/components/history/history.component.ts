import { Component, OnInit } from '@angular/core';
import { Video } from 'src/app/models/video';
import { VideoserviceService } from 'src/app/services/videoservice.service';


@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  vidHistory:Video[]=[];

  constructor(_videoService:VideoserviceService) {
    this.vidHistory = _videoService.history;
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
