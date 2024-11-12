
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private stompClient!: Stomp.Client;

  connect() {
    const socket = new SockJS(`${environment.apiUrl}/ws`);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);

      this.stompClient.subscribe('/topic/notifications', (message) => {
        const transfer = JSON.parse(message.body);
        this.notifyUser(transfer);
      });
    });
  }

  notifyUser(transfer: any) {
    window.alert(`Nowy przelew: ${transfer.title}, kwota: ${transfer.amount}`);
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected');
      });
    }
  }
}
