import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

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
    depositsInPLN: 5000,
    accountBalance: 10000
    // Tutaj można przypisać inne testowe wartości dla pól AccountInfo
  };

  constructor(private router: Router, private http: HttpClient){ }


  ngOnInit(): void {
    // Pobierz identyfikator konta z localStorage
    const idAccount = localStorage.getItem('idAccount'); // działa już
    console.log("idAccount = "+idAccount); // działa już
    if (idAccount) {
      // Wyślij żądanie do serwera, aby pobrać dane użytkownika na podstawie identyfikatora konta
      this.http.get<any>(`http://localhost:8080/accounts/${idAccount}`)
        .subscribe(
          (response) => {
            // Ustaw dane użytkownika w komponencie nagłówka
            this.testAccountInfo.accountBalance = response.balance;
          },
          (error) => {
            console.error('Błąd podczas pobierania danych użytkownika.', error);
          }
        );
    }
 }

}
