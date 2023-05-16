import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
// import { HttpClient, HttpHeaders } from '@angular/common/http';


import { ViewService } from 'src/app/services/view.service';
import { Observable } from 'rxjs';


// import { DomSanitizer, SafeUrl } from '@angular/platform-browser';


@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {
  videoUrl: any;
  videoId: number = -1;
  videoLink: string = '';
  constructor(private viewService: ViewService, private authService: AuthService, private activatedRoute: ActivatedRoute) {  }
  ngOnInit() {
    console.log(this.authService.getAuth())
    this.activatedRoute.queryParams.subscribe((params) => {
      this.videoId = params['videoId'];
      console.log(this.videoId)
      if (this.videoId != -1){
        console.log((params['videoId']));
        // this.viewService.watchVideo(this.videoId.toString()).subscribe(response =>{
        //   const videoBlob = new Blob([response], { type: 'video/mp4' });
        //   this.videoUrl = URL.createObjectURL(videoBlob);
        // })
        this.viewService.watchVideo(this.videoId.toString()).subscribe(blob => {
          const url = URL.createObjectURL(blob);
          this.videoUrl = url;
        });
        // this.viewService.getAuthenticatedVideoStream(this.videoId.toString()).subscribe(response => {
        //   this.videoUrl = URL.createObjectURL(response);
        // });
      }
    })

    
  }


  

  
}
