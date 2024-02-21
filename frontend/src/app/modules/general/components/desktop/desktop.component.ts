import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../services/axios.service';

interface AccountInfo {
  owner: string;
  accountName: string;
  accountNumer: string;
  currency: string;
  deposit: number;
  credit: number;
  accountBalance: number;
  // Inne pola związane z informacjami o rachunku
}

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrl: './desktop.component.css'
})
export class DesktopComponent {
  //@Input() accountInfo: AccountInfo;
  accountInfo: AccountInfo = {
    owner: 'NaN',
    accountName: 'NaN',
    accountNumer: 'NaN',
    currency: 'NaN',
    deposit: NaN,
    credit: NaN,
    accountBalance: NaN

  };

  constructor(private router: Router, private axiosService : AxiosService){ }


  ngOnInit(): void {
    // Pobierz identyfikator konta z localStorage
    const idAccount = localStorage.getItem('idAccount');
    console.log("idAccount = " + idAccount);
    if (idAccount) {
      // Wyślij żądanie do serwera, aby pobrać dane użytkownika na podstawie identyfikatora konta
      this.axiosService.request('GET', `http://localhost:8080/accounts/${idAccount}`, {})
        .then(
          (response) => {
            console.log(response);
            // Ustaw dane użytkownika w komponencie nagłówka
            this.accountInfo.accountBalance = response.data.balance;
            this.accountInfo.accountNumer = response.data.number;
          }
        ).catch(
          (error) => {
            if (error.response.status === 401) {
              this.axiosService.setAuthToken(null);
            } else {
                this.accountInfo.owner = error.response.code;
            }
            console.error('Błąd podczas pobierania danych użytkownika.', error);
          }
        );
    }
 }

}
