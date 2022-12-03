
import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor(private messageService: MessageService) { }

  public success(messageText: string) {
    this.messageService.add({ severity: 'success', summary: 'Success Message', detail: messageText });

  }

  public error(messageText: string) {
    console.log("rr");
    this.messageService.add({ severity: 'error', summary: 'Error Message', detail: messageText });
    console.log("rr");
  }


  public showConfirm(d: string) {
    this.messageService.add({key: 'confirm', sticky: true, severity: 'warn', summary: 'Are you sure?', detail: d });
  }

  public clear(){
    this.messageService.clear('confirm');

  }
}
