// import { Component, OnInit } from '@angular/core';
// import Stomp from 'stompjs';
// import SockJS from 'sockjs-client';

// @Component({
//   selector: 'app-notifications',
//   template: `<div *ngIf="notification">{{ notification }}</div>`,
// })
// export class NotificationsComponent implements OnInit {
//   private stompClient: any;
//   notification: string = '';

//   ngOnInit() {
//     const socket = new SockJS('http://localhost:8080/ws');
//     this.stompClient = Stomp.over(socket);

//     this.stompClient.connect({}, () => {
//       this.stompClient.subscribe('/topic/notifications', (message: any) => {
//         const event = JSON.parse(message.body);
//         this.notification = `Nowy przelew od ${event.sender} na kwotę ${event.amount} zł.`;
//       });
//     });
//   }
// }