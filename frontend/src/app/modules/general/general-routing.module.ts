import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DesktopComponent } from './components/desktop/desktop.component';
import { GeneralDashboardComponent } from './components/general-dashboard/general-dashboard.component';
import { CreditComponent } from './components/credit/credit.component';
import { AccountComponent } from './components/account/account.component';
import { InvestmentComponent } from './components/investment/investment.component';
import { CurrencyComponent } from './components/currency/currency.component';
import { CounterpartyComponent } from './components/counterparty/counterparty.component';
import { SettingsComponent } from './components/settings/settings.component';
import { NewsComponent } from './components/news/news.component';

const routes: Routes = [
  { path: '', component: GeneralDashboardComponent, children: [
    {path: 'desktop', component: DesktopComponent},
    {path: 'account', component: AccountComponent},
    {path: 'investment', component: InvestmentComponent},
    {path: 'credit', component: CreditComponent},
    {path: 'currency', component: CurrencyComponent},
    {path: 'counterparty', component: CounterpartyComponent},
    {path: 'settings', component: SettingsComponent},
    {path: 'news', component: NewsComponent},
    {path: '', redirectTo: '/general/desktop', pathMatch:'full'},
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GeneralRoutingModule { }
