import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../services/axios.service';
import { Investment } from '../../../../models/investment.model';

interface AccountInfo {
  owner: string;
  accountName: string;
  accountNumer: string;
  currency: string;
  deposit: number;
  loan: number;
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
    loan: NaN,
    accountBalance: NaN

  };
  investments: Investment[] = []; 

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

        this.axiosService.request('GET', `http://localhost:8080/investments/${idAccount}`, {})
        .then((response) => {
            console.log(response);
            // przypisywanie do tablicy obiektów wartości
          //   if (Array.isArray(response.data)) {
          //     // Jeśli tak, przypisz odpowiedź do tablicy transfers
          //     this.investments = response.data.map((investment: any) => {
          //       investment.startDate = this.formatDate(investment.startDate);
          //       return investment;
          //     });
          //   } else {
          //     // Jeśli nie, umieść pojedynczy obiekt w tablicy transfers
          //     // this.investments = [response.data];
          //     // this.investments[0].startDate = this.formatDate(this.investments[0].startDate);
          //     // this.investments = response.data.map((investment: any) => {
          //     //   investment.startDate = this.formatDate(investment.startDate);
          //     //   return investment;
          //     // });
          // }
          if (Array.isArray(response.data)) {
            // Jeśli tak, przypisz odpowiedź do tablicy transfers
            this.investments = response.data;
          } else {
            // Jeśli nie, umieść pojedynczy obiekt w tablicy transfers
            this.investments = [response.data];
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

        this.axiosService.request('GET', `http://localhost:8080/loans/${idAccount}`, {})
        .then(
          (response) => {
            console.log(response);
            // Ustaw dane użytkownika w komponencie nagłówka
            this.accountInfo.loan = response.data.amount;
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
 formatDate(date: string): string {
  const parsedDate = new Date(date);
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  };
  return parsedDate.toLocaleString('pl-PL', options);
}

}
