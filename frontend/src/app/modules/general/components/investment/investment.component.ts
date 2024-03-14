import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AxiosService } from '../../../../services/axios.service';
import { Router } from '@angular/router';

interface InterestRateMap {
  [key: string]: number;
}


@Component({
  selector: 'app-investment',
  templateUrl: './investment.component.html',
  styleUrl: './investment.component.css'
})
export class InvestmentComponent {
  investmentForm: FormGroup = new FormGroup({});
  minAmount: number = 1000; // Przykładowe minimalne i maksymalne wartości
  maxAmount: number = 100000;
  minDurationMonths: number = 3;
  maxDurationMonths: number = 60;
  interestRateMap: InterestRateMap = {
    'FUND': 3,
    'GOLD': 2,
    'SILVER': 2.5
  };

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private axiosService: AxiosService,
  ) { 
    this.createForm();
  }


  createForm() {
    // Utwórz obiekt daty rok do przodu
    const today = new Date();
    const endDateValue = new Date(today);
    endDateValue.setMonth(endDateValue.getMonth() + 3);
  
    this.investmentForm = this.fb.group({
      type: ['FUND', Validators.required],
      amount: [1000, [Validators.required, Validators.min(this.minAmount), Validators.max(this.maxAmount)]],
      // interestRate: [5, [Validators.required, Validators.min(0)]],
      interestRate: [{ value: this.interestRateMap['FUND'], disabled: true }, Validators.required],
      durationMonths: [this.minDurationMonths, [Validators.required, Validators.min(this.minDurationMonths), Validators.max(this.maxDurationMonths)]],
      startDate: [today, Validators.required],
      endDate: [{ value: endDateValue, disabled: true }, Validators.required] // Ustawienie daty zakończenia
    });
    this.onChanges();
  }

  onChanges() {
    this.investmentForm.get('type')?.valueChanges.subscribe(type => {
      const newInterestRate = this.interestRateMap[type];
      this.investmentForm.get('interestRate')?.setValue(newInterestRate);
    });

    this.investmentForm.get('durationMonths')?.valueChanges.subscribe(months => {
      const startDate = new Date();
      const endDate = new Date(startDate);
      endDate.setMonth(startDate.getMonth() + months);
      this.investmentForm.get('endDate')?.setValue(endDate.toISOString()); // Ustaw datę zakończenia w formacie "yyyy-MM-dd"
    });
  }

  onSubmit(): void {
    if (this.investmentForm.valid) {
      // Przygotowanie danych do wysłania na serwer lub inna logika
      const investmentDto = {
        type: this.investmentForm.get('type')?.value,
        amount: this.investmentForm.get('amount')?.value,
        interestRate: this.investmentForm.get('interestRate')?.value,
        startDate: this.investmentForm.get('startDate')?.value,
        endDate: this.investmentForm.get('endDate')?.value,
        idAccount: localStorage.getItem('idAccount')
      };
      
      console.log('Wysłanie danych inwestycji:', investmentDto);
      this.axiosService.request('POST', `http://localhost:8080/investments`, investmentDto)
      .then((response: any) => {
        if (response && response.status === 200) {
          window.alert(response.data);

        }
      })
      .catch(error => {
        if(error.response.status == 409){
          window.alert(error.response.data);
        }
        else{
          window.alert("Wystąpił niespodziewany błąd, spróbuj ponownie później!")
        }
        console.log(error.response)
      });
    }
    else{
      window.alert("Wypełnij wszystkie pola formularzu inwestycji!")
    }

  }
}
