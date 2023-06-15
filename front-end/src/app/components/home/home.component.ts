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
  uploadPresentage: number = 0;
  msg: string = '';
  selectedFile: File | undefined;

  constructor(
    private authService: AuthService,
    private router: Router,
    private fileUploadService: FileUploadService,
    private apiService: ApiService
  ) {
    if (this.authService.checkAuth()) {
      this.router.navigate(['/', 'login']);
    }
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
    this.apiService.uploadFile(this.selectedFile).subscribe(
      (event: any) => {
        console.log(event.type)
        if (event.type === 1) {
          // Progress event
          this.uploadPresentage = Math.round((event.loaded / event.total) * 100);
        } else if (event.type === 4) {
          // Response event
          this.uploading = false;
          this.router.navigate(['/', 'view']);
        }
      },
      (error: any) => {
        console.log('Upload error:', error);
        this.uploading = false;
        // Handle the error, e.g., display an error message
        this.msg = 'An error occurred during file upload. Please try again.';
      }
    );
  }
}
