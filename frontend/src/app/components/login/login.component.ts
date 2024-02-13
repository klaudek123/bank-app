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
  idAccount: string = "";
  password: string = "";
  
  constructor(private router: Router, private http: HttpClient) {}

  handleLoginSubmit(): void {
    if (!this.idAccount) {
      window.alert("Numer Klienta jest pusty");
      return;
    }

    if (!this.password) {
      window.alert("Hasło jest puste");
      return;
    }

  const loginClient = { idAccount: this.idAccount, password: this.password };

    this.http.post<any>('http://localhost:8080/accounts/login', loginClient)
    .subscribe(
      (response: any) => {
        console.log(response);
        // Sprawdź, czy odpowiedź zawiera kod 200 i komunikat "success"
        if (response && response.message === "Zalogowano pomyślnie!") {
          localStorage.setItem('idAccount', this.idAccount);
          window.alert(response.message);
          this.router.navigateByUrl('/general'); // Przekierowanie po pomyślnym zalogowaniu
        } else {
          // Obsługa przypadku, gdy logowanie się nie powiodło
          if(response){
            // window.alert(response.message);
          }
          // Możesz wyświetlić komunikat dla użytkownika lub podjąć inne działania w przypadku niepowodzenia logowania
        }
      },
      error => {
        window.alert('Błąd podczas logowania.'+ error);
        // Handle login error (display error message to user, etc.)
      }
      );
  }
}
