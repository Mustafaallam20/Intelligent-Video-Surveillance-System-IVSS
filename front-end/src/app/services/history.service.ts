import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  constructor(private http: HttpClient, private apiService:ApiService ) { }

  getAllVideos(){
    return this.apiService.post('/api/videos/all');
  }
  getVideoMetadata(id:string){
    return this.apiService.post('/api/videos/video/'+id)
  }
  view(id:string){
    return this.apiService.post('/api/videos/view/'+id)
  }
  delete(id:string){
    return this.apiService.post('/api/videos/delete/'+id)
  }
//   download(id:string){
//     return this.apiService.post('/api/videos/download/'+id)

//   }


//   download() {
//     const videoId = //get the videoId from the component
//     const url = `http://localhost:8080/watch/${videoId}`;
//     this.http.get(url, { responseType: 'blob' }).subscribe((blob: Blob) => {
//       saveAs(blob, 'video.mp4');
//     });
// }

}

