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

}