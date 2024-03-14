// login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../services/axios.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  idAccount: string = "";
  password: string = "";
  
  constructor(private router: Router, private axiosService: AxiosService) {}

  handleLoginSubmit(): void {
    localStorage.clear();
    if (!this.idAccount) {
      window.alert("Numer Klienta jest pusty");
      return;
    }

    if (!this.password) {
      window.alert("Hasło jest puste");
      return;
    }

  const loginClient = { idAccount: this.idAccount, password: this.password };

    this.axiosService.request('POST', '/accounts/login', loginClient)
    .then((response: any) => {
      console.log(response);
      // Sprawdź, czy odpowiedź zawiera kod 200 i komunikat "success"
      if (response && response.status === 200) {
        localStorage.setItem('idAccount', this.idAccount);
        this.axiosService.setAuthToken(response.data.token); // Zapisanie tokena autoryzacyjnego
        console.log(response.data);
        // window.alert("Udało się zalogować!");
        this.router.navigateByUrl('/general'); // Przekierowanie po pomyślnym zalogowaniu
      } 
      else if(response && response.status === 401) {
        this.axiosService.setAuthToken(null);
        window.alert("Wystąpił nieoczekiwany błąd. Proszę zaloguj się ponownie.");
        // // Obsługa przypadku, gdy logowanie się nie powiodło
        // if(response){
        //   window.alert(response.message);
        // }
        // // Możesz wyświetlić komunikat dla użytkownika lub podjąć inne działania w przypadku niepowodzenia logowania
      }
    }).catch(
      error => {
        window.alert('Błąd podczas logowania.'+ error);
        this.axiosService.setAuthToken(null);
        // Handle login error (display error message to user, etc.)
      }
      );
  }
}
