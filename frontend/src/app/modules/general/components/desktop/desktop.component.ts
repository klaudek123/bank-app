import { Component } from '@angular/core';


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

  constructor() { }
}
