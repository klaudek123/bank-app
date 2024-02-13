import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  showLogout: boolean = false;
  user: any = { name: '', lastname: '' }; // Przykładowe dane użytkownika

  
  constructor(private router: Router, private http: HttpClient){ }

  ngOnInit(): void {
     // Pobierz identyfikator konta z localStorage
     const idAccount = localStorage.getItem('idAccount'); // działa już
     console.log("idAccount = "+idAccount); // działa już
     if (idAccount) {
       // Wyślij żądanie do serwera, aby pobrać dane użytkownika na podstawie identyfikatora konta
       this.http.get<any>(`http://localhost:8080/users/${idAccount}`)
         .subscribe(
           (response) => {
             // Ustaw dane użytkownika w komponencie nagłówka
             this.user.name = response.firstname;
             this.user.lastname = response.lastname;
           },
           (error) => {
             console.error('Błąd podczas pobierania danych użytkownika.', error);
           }
         );
     }
  }

  showLogoutPanel(): void {
    this.showLogout = true;
  }

  hideLogoutPanel(): void {
    this.showLogout = false;
  }

  logout(): void {
    // Tutaj można dodać logikę wylogowania użytkownika
    this.router.navigate(['/login']);
  }
}
