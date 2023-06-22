import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { FileUploadService } from 'src/app/services/file-upload.service';
import { ApiService } from './../../services/api.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userName?: string;
  uploading: boolean = false;
  processing = false;
  uploadPresentage: number = 0;
  msg: string = '';
  selectedFile: File | undefined;
  // processingPresentage:number=0;

  modelOptions = [
    { value: 'general', label: 'General Detection' },
    { value: 'fight_fall_crash', label: 'Fall, Fight & Crash Detection' },
    { value: 'indoor', label: 'Indoor Detection' },
    { value: 'indoor_mask', label: 'Indoor & Mask Detection' },
  ];
  selectedOption: string = "general";


  constructor(
    private authService: AuthService,
    private router: Router,
    private fileUploadService: FileUploadService,
    private apiService: ApiService
  ) {
    // if (this.authService.checkAuth()) {
    //   this.router.navigate(['/', 'login']);
    // }
  }

  getPercent(id: string): any{    
    this.apiService.post("/api/videos/status/"+id).subscribe(res=>{
      this.processing = true;
      if (res.status == "processing") {
        this.uploadPresentage = Math.round(res.percent);
      } else if (res.status == "failed") {
         console.log(res)
      } else if (res.status == "finished") {
        this.uploadPresentage = Math.round(res.percent);
        // let path = '/view/' + id
        // const encodedId = encodeURIComponent(id.toString());
        console.log('/', 'view', '/', id, typeof(id))
        this.router.navigate(['/view'], { queryParams: { 'videoId': id } });

      }
      console.log(this.uploadPresentage)
      if(this.uploadPresentage<100){        
        setTimeout(() => this.getPercent(id), 5000);
    }
    })
  }

  ngOnInit(): void {
    this.userName = this.authService.getUserName();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onSubmit() {
    if (!this.selectedFile) return;
    this.uploading = true;
    let res = this.apiService.uploadFile(this.selectedFile, this.selectedOption).subscribe(
      (event: any) => {
        // console.log(event.type)
        if (event.type === 1) {
          // console.log(event)
          // Progress event
          this.uploadPresentage = Math.round((event.loaded / event.total) * 100);
          if(this.uploadPresentage==100){// cam cause error
            this.uploading = false;
            this.uploadPresentage = 0;
            this.processing = true;
            // let res = this.getPercent(event.body.data.id)
          }

        } else if (event.type === 4) {
        // console.log(event.body.data)

          // Response event
          // this.uploading = false;
          // this.uploadPresentage = 0.05;
          // this.processing = true;
          this.getPercent(Math.round(event.body.data.videoId).toString());
        }
      },
      (error: any) => {
        console.log('Upload error:', error);
        this.uploading = false;
        this.msg = 'An error occurred during file upload. Please try again.';
      }
    );
  }


  updateModel(event:any){
    console.log((event.target as HTMLSelectElement).value)
    this.selectedOption = (event.target as HTMLSelectElement).value
  }
}
