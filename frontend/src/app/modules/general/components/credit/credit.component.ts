import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AxiosService } from '../../../../services/axios.service';


@Component({
  selector: 'app-credit',
  templateUrl: './credit.component.html',
  styleUrl: './credit.component.css'
})
export class CreditComponent {
  loanForm: FormGroup = new FormGroup({});
  loanInterest: number = 10;
  minLoanAmount: number = 5000;
  maxLoanAmount: number = 50000;
  minLoanDurationMonths: number = 12;
  maxLoanDurationMonths: number = 36;

  constructor(private fb: FormBuilder, private axiosService: AxiosService) {
    this.createForm();
  }

  createForm() {
    // Utwórz obiekt daty rok do przodu
    const endDateValue = new Date();
    endDateValue.setFullYear(endDateValue.getFullYear() + 1);
  
    this.loanForm = this.fb.group({
      loanAmount: [this.minLoanAmount, [Validators.required, Validators.min(this.minLoanAmount), Validators.max(this.maxLoanAmount)]],
      loanDurationMonths: [this.minLoanDurationMonths, [Validators.required, Validators.min(this.minLoanDurationMonths), Validators.max(this.maxLoanDurationMonths)]],
      startDate: [new Date(), Validators.required],
      endDate: [{ value: endDateValue, disabled: true }, Validators.required]
    });
    this.onChanges();
  }

  onChanges() {
    this.loanForm.get('loanDurationMonths')?.valueChanges.subscribe(months => {
      const startDate = new Date();
      const endDate = new Date(startDate);
      endDate.setMonth(startDate.getMonth() + months);
      this.loanForm.get('endDate')?.setValue(endDate);
    });
  }
  

  onSubmit() {
    const idAccount = localStorage.getItem('idAccount');
    if (this.loanForm.valid) {
      const loanDto = {
        amount: this.loanForm.get('loanAmount')?.value,
        interestRate: this.loanInterest,
        startDate: this.formatDate(this.loanForm.get('startDate')?.value), 
        endDate: this.formatDate(this.loanForm.get('endDate')?.value) 
      };

      this.axiosService.request('POST', `http://localhost:8080/accounts/${idAccount}/loans`, loanDto)
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
    } else {
      window.alert('Błędnie wybrane wartości pożyczki!');
    }
  }


  formatDate(date: string): string {
    const formattedDate = new Date(date);
    return formattedDate.toISOString().slice(0, 19).replace('T', ' ');
  }
}
