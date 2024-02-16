import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../services/axios.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  showLogout: boolean = false;
  user: any = { name: '', lastname: '' }; // Przykładowe dane użytkownika

  
  constructor(private router: Router, private axiosService: AxiosService){ }

  ngOnInit(): void {
     // Pobierz identyfikator konta z localStorage
     const idAccount = localStorage.getItem('idAccount'); // działa już
     console.log("idAccount = "+idAccount); // działa już
     if (idAccount) {
       // Wyślij żądanie do serwera, aby pobrać dane użytkownika na podstawie identyfikatora konta
       this.axiosService.request('GET', `http://localhost:8080/users/${idAccount}`, {})
        .then(
           (response) => {
            console.log(response);
             // Ustaw dane użytkownika w komponencie nagłówka
             this.user.name = response.data.firstname;
             this.user.lastname = response.data.lastname;
           },
           (error) => {
            if (error.response.status === 401) {
              this.axiosService.setAuthToken(null);
            } else {
                this.user.name = error.response.code;
            }
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
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
