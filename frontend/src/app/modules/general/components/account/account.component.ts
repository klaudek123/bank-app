import { Component, OnInit  } from '@angular/core';
import { Transfer } from '../../../../models/transfer.model';
import { AxiosService } from '../../../../services/axios.service';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent {

  transfers: Transfer[] = []; 
  selectedFilter: string = ''; 
  filterValue: string = ''; 
  startDate: Date | null = null; 
  endDate: Date | null = null;


  constructor(private axiosService: AxiosService) { }

  ngOnInit(): void {
    const idAccount = localStorage.getItem('idAccount');
    console.log("idAccount = " + idAccount);
    if (idAccount) {
      this.axiosService.request('GET',`${environment.apiUrl}/accounts/${idAccount}/transfers`, {})
        .then(
          (response) => {
            console.log(response);
            if (Array.isArray(response.data)) {
              this.transfers = response.data;
            } else {
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
  filteredTransfers(): Transfer[] {
    let filtered = this.transfers;

    if (this.selectedFilter && this.startDate) {
      filtered = filtered.filter(transfer => transfer.date >= this.startDate!);
    }
    if (this.selectedFilter && this.endDate) {
      filtered = filtered.filter(transfer => transfer.date < this.endDate!);
    }

    if (this.selectedFilter && this.filterValue) {
      const value = this.filterValue.toLowerCase(); 
      filtered = filtered.filter(transfer => {
        switch (this.selectedFilter) {
          case 'sender':
            return transfer.sender.toString().includes(value);
          case 'recipient':
            return transfer.recipient.toString().includes(value);
          case 'title':
            return transfer.title.toLowerCase().includes(value);
          case 'amount':
            return transfer.amount.toString().includes(value);
          default:
            return true;
        }
      });
    }

    return filtered;
  }
  onFilterTypeChange(): void {
    this.clearFilter();
  
    setTimeout(() => {
      this.filterValue = ''; 
    }, 0);
  }


  clearFilter(): void {
    this.filterValue = ''; 
    this.startDate = null;
    this.endDate = null;
  }
}