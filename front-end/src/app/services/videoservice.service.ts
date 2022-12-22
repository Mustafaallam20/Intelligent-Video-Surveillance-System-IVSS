import { Injectable } from '@angular/core';
import { Video } from '../models/video';

@Injectable({
  providedIn: 'root'
})
export class VideoserviceService {

  history:Video[]=[
      {date:"27/9/2022", id:"video_1", isNew:true,type:"Car Crash"},
      {date:"15/5/2022", id:"video_2", isNew:true,type:"Car Crash"},
      {date:"13/5/2021", id:"video_3", isNew:false,type:"Car Crash"}
  ];

  constructor() { }
}
