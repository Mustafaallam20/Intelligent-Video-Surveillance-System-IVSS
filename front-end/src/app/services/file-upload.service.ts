import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiService } from './api.service';


@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private http: HttpClient, private apiService:ApiService) { }

  uploadFile(file: File) {
    const formData = new FormData();
    formData.append('file', file);

    return this.apiService.post('/api/videos/upload', formData);
  }

}