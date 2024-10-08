import { Component, OnInit  } from '@angular/core';
import { Transfer } from '../../../../models/transfer.model';
import { AxiosService } from '../../../../services/axios.service';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

  transfers: Transfer[] = []; 



  constructor(private axiosService: AxiosService) { }

  ngOnInit(): void {
    // Pobierz identyfikator konta z localStorage
    const idAccount = localStorage.getItem('idAccount');
    console.log("idAccount = " + idAccount);
    if (idAccount) {
      // Wyślij żądanie do serwera, aby pobrać dane użytkownika na podstawie identyfikatora konta
      this.axiosService.request('GET', `${environment.apiUrl}/accounts/${idAccount}/transfers`, {})
        .then(
          (response) => {
            console.log(response);
            // przypisywanie do tablicy obiektów wartości
            if (Array.isArray(response.data)) {
              // Jeśli tak, przypisz odpowiedź do tablicy transfers
              this.transfers = response.data;
            } else {
              // Jeśli nie, umieść pojedynczy obiekt w tablicy transfers
              this.transfers = [response.data];
            }
          }
        ).catch(
          (error) => {
            if (error.response.status === 401) {
              // this.axiosService.setAuthToken(null);
            } else {
                // this.testAccountInfo.owner = error.response.code;
            }
            console.error('Błąd podczas pobierania danych użytkownika.', error);
          }
        );
    }
 }
}

