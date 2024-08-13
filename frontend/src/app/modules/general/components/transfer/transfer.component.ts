import { Component } from '@angular/core';
import { AxiosService } from '../../../../services/axios.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.css'
})
export class TransferComponent {
  recipient: number = NaN;
  title: string = "";
  amountZloty: number = NaN;
  amountGrosz: number = NaN;
  idAccount: number = 0;

  constructor(private router: Router, private axiosService: AxiosService) {}

  onSubmit(): void {
    if (!validateNumberLength(this.recipient, 10)) {
      window.alert("Numer konta odbiorcy musi mieć co najmniej 10 cyfr.");
      return;
    }
    if (!validateLength(this.title, 2)) {
      window.alert("Uzupełnij tytuł!");
      return;
    }
    if (!this.amountZloty && !this.amountGrosz) {
      window.alert("Uzupełnij sumę!");
      return;
    }
    if(this.amountGrosz > 99){
      window.alert("Popraw wartość groszy!")
      return;
    }

  const amount = this.amountZloty + this.amountGrosz / 100;
  
  const idAccount = localStorage.getItem("idAccount")
  
  const transferDTO = {
    recipient: this.recipient,
    sender: localStorage.getItem("accountNumer"),
    title: this.title,
    amount: amount
  };

  console.log(transferDTO)

  this.axiosService.request('POST', `http://localhost:8080/accounts/${idAccount}/transfers`, transferDTO)
  .then(
    (response) => {
      console.log(response);

      if(response.status === 200){
        window.alert("Przelew został wykonany pomyślnie.");
        this.router.navigateByUrl('/general/account');
      }

    },
    (error) => {
      if (error.response.status === 401) {
        this.axiosService.setAuthToken(null);
      } else {
        window.alert("Wystąpił nieznany błąd!");
      }
      console.error('Błąd podczas wysyłania przelewu.', error);
     }

  )

  }
}


function validateLength(value: string, minLength: number): boolean {
  return value.length >= minLength;
}

function validateNumberLength(value: number, minLength: number): boolean {
  return value.toString().length >= minLength;
}