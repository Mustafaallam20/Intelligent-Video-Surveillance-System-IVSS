import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { NgForm } from '@angular/forms';
import { FileUploadService } from 'src/app/services/file-upload.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  userName?:string ;

  selectedFile: File | undefined;
  // @ViewChild('fileUploadForm') fileUploaadForm!: NgForm;
  constructor(private authService:AuthService, private router:Router, private fileUploadService: FileUploadService) {
    
    
    if (this.authService.checkAuth()) {
      
      this.router.navigate(['/', 'login']);
      
    }
  }
  
  ngOnInit(): void {

    this.userName!=this.authService.getUserName();
    console.log(this.authService.getUserName())
   /* this.authService.test().subscribe(
      (result:any) => {
        console.log(result);
      }
    );*/
    console.log(this.authService.getAuth())
  }

  onFileSelected(event: any){
    this.selectedFile = event.target.files[0];
  }
  onSubmit(){
    if (! this.selectedFile) return;
    this.fileUploadService.uploadFile(this.selectedFile).subscribe(response => {
      console.log(response)
    })
  }
}

