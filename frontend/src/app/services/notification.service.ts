import { Injectable } from '@angular/core';
import { Socket  } from 'ngx-socket-io';



@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private socket: Socket) {}


  connect() {
    const token = localStorage.getItem('auth_token');
    this.socket.ioSocket.auth = { token };

    this.socket.connect();
    console.log('Połączenie WebSocket nawiązane');

    this.socket.fromEvent('topic/notifications').subscribe((message: any) => {
      const transfer = JSON.parse(message.body);
      this.notifyUser(transfer);
    });
  }

  notifyUser(transfer: any) {
    window.alert(`Nowy przelew: ${transfer.title}, kwota: ${transfer.amount}`);
  }

  disconnect() {
    this.socket.disconnect();
    console.log('Disconnected');
    console.log('Rozłączono z WebSocket');
  }
}
