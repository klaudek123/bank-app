import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../axios.service';

interface AccountInfo {
  owner: string;
  accountName: string;
  accountNumer: string;
  currency: string;
  depositsInPLN: number;
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
  testAccountInfo: AccountInfo = {
    owner: 'John Doe',
    accountName: 'Main Account',
    accountNumer: '61109010140000071219812874',
    currency: 'USD',
    depositsInPLN: NaN,
    accountBalance: 12000
    // Tutaj można przypisać inne testowe wartości dla pól AccountInfo
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
            // Ustaw dane użytkownika w komponencie nagłówka
            this.testAccountInfo.accountBalance = response.data.balance;
            this.testAccountInfo.accountNumer = response.data.number;
          }
        ).catch(
          (error) => {
            if (error.response.status === 401) {
              this.axiosService.setAuthToken(null);
            } else {
                this.testAccountInfo.owner = error.response.code;
            }
            console.error('Błąd podczas pobierania danych użytkownika.', error);
          }
        );
    }
 }

}
