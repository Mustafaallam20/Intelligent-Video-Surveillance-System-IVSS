import { Injectable } from '@angular/core';
import { Video } from '../models/video';

@Injectable({
  providedIn: 'root'
})
export class VideoserviceService {

  history:Video[]=[{date:"15/5/2022", link:"videolink", status:'viewed',type:"Car Crash"}];

  constructor() { }
}
