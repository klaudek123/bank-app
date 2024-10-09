import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../services/axios.service';
import { Investment } from '../../../../models/investment.model';
import { environment } from '../../../../../environments/environment';

interface AccountInfo {
  owner: string;
  accountName: string;
  accountNumer: string;
  currency: string;
  deposit: number;
  loan: number;
  accountBalance: number;
  status: string;
}

@Component({
  selector: 'app-desktop',
  templateUrl: './desktop.component.html',
  styleUrls: ['./desktop.component.css']
})
export class DesktopComponent {
  accountInfo: AccountInfo = {
    owner: 'NaN',
    accountName: 'NaN',
    accountNumer: 'NaN',
    currency: 'NaN',
    deposit: NaN,
    loan: NaN,
    accountBalance: NaN,
    status: 'NaN'
  };
  investments: Investment[] = []; 

  constructor(private router: Router, private axiosService : AxiosService){ }


  ngOnInit(): void {
    const idAccount = localStorage.getItem('idAccount');
    console.log("idAccount = " + idAccount);
    if (idAccount) {
      this.axiosService.request('GET', `${environment.apiUrl}/accounts/${idAccount}`, {})
        .then(
          (response) => {
            console.log(response);

            this.accountInfo.owner = response.data.idAccount;
            this.accountInfo.accountBalance = response.data.balance;
            this.accountInfo.accountNumer = response.data.number;
            localStorage.setItem('accountNumer', response.data.number);
            console.log(response.data.number);
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
        console.log("idAccount = " + idAccount);
        this.axiosService.request('GET', `${environment.apiUrl}/accounts/${idAccount}/investments`, {})
        .then((response) => {
            // console.log(response.data[0].startDate);
            
          if (Array.isArray(response.data)) {
            this.investments = response.data;
          } else {
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

        this.axiosService.request('GET', `${environment.apiUrl}/accounts/${idAccount}/loans`, {})
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

}
