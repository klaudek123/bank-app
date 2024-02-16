import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GeneralRoutingModule } from './general-routing.module';
import { DesktopComponent } from './components/desktop/desktop.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { AccountComponent } from './components/account/account.component';
import { CreditComponent } from './components/credit/credit.component';
import { InvestmentComponent } from './components/investment/investment.component';
import { GeneralDashboardComponent } from './components/general-dashboard/general-dashboard.component';
import { TransferComponent } from './components/transfer/transfer.component';
import { CounterpartyComponent } from './components/counterparty/counterparty.component';
import { SettingsComponent } from './components/settings/settings.component';
import { NewsComponent } from './components/news/news.component';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from '../../app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';


@NgModule({
  declarations: [
    DesktopComponent,
    HeaderComponent,
    FooterComponent,
    AccountComponent,
    CreditComponent,
    InvestmentComponent,
    GeneralDashboardComponent,
    TransferComponent,
    CounterpartyComponent,
    SettingsComponent,
    NewsComponent
  ],
  imports: [
    CommonModule,
    GeneralRoutingModule,
    FormsModule
  ]
})
export class GeneralModule { }
