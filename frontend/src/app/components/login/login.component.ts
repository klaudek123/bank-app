// login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  //backgroundImage = require("../../assets/images/bank-background.jpg");
  // email: string = "";
  // password: string = "";
  
  // constructor(private router: Router, private http: HttpClient) {}

  // handleLoginSubmit(): void {
  //   if (!this.email) {
  //     window.alert("Login jest pusty");
  //     return;
  //   }

  //   if (!this.password) {
  //     window.alert("Hasło jest puste");
  //     return;
  //   }

  //const loginClient = { email: this.email, password: this.password };

  //   this.http.post<any>('http://localhost:8080/api/v1/auth/authenticate', loginClient)
  //     .subscribe(
  //       data => {
  //         localStorage.setItem('jwtToken', data.token);
  //         console.log("jwtToken z logowania: " + data.token);
  //         this.router.navigate(['/general']);
  //       },
  //       error => {
  //         console.error('Błąd podczas logowania.', error);
  //       }
  //     );
  // }
}
