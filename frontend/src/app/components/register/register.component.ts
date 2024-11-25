import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})

export class RegisterComponent {
  redirectToLogin: boolean = false;
  personalId: string = "";
  firstname: string = "";
  lastname: string = "";
  dateOfBirth: string = "";
  email: string = "";
  password: string = "";
  passwordCheck: string = "";
  address: string = "";
  acceptTerms: boolean = false;
  
  constructor(private router: Router, private http: HttpClient) {}

    



  handleFormSubmit(): void {

    if (!validateLength(this.firstname, 2)) {
      window.alert("Imie musi mieć co najmniej 2 znaki.");
      return;
    }

    if (!validateLength(this.lastname, 2)) {
      window.alert("Nazwisko musi mieć co najmniej 2 znaki.");
      return;
    }


    if(!validatePESEL(this.personalId)){
      window.alert("Błędy pesel, musi się składać z 11 cyfr!" + this.personalId);
    }

    if (!this.dateOfBirth) {
      window.alert("Proszę wybrać datę urodzenia.");
      return;
    }

    if (!validateEmail(this.email)) {
      window.alert("Niepoprawny adres email.");
      return;
    }

    // if (!validateLength(this.address, 2)) {
    //   window.alert("Adres musi mieć co najmniej 2 znaki.");
    //   return;
    // }

    if (!validateLength(this.password, 3)) {
      window.alert("Hasło musi mieć co najmniej 3 znaki.");
      return;
    }
    if (this.password !== this.passwordCheck) {
      window.alert("Hasło i potwierdzenie hasła nie są identyczne");
      return;
    }

    if (!this.acceptTerms) {
      window.alert("Proszę zaakceptować regulamin.");
      return;
    }
    

    const registerDTO = {
      personalId: this.personalId, 
      firstname: this.firstname,
      lastname: this.lastname,
      dateOfBirth: this.dateOfBirth,
      email: this.email,
      address: this.address, 
      password: this.password
    };

    console.log(registerDTO);

    this.http.post<any>(`${environment.apiUrl}/users`, registerDTO)
      .subscribe(
        (response) => {
          window.alert("Udało się założyć konto. Numer klienta to: "+ response);
          this.redirectToLogin = true;
          this.router.navigateByUrl('/login'); 
        },
        error => {
          window.alert("Błąd podczas rejestracji: "+ error);
          console.error('Błąd podczas rejestracji: ', error);
        }
      );
  }
}

function validatePESEL(pesel: string): boolean {
  if (pesel.length !== 11) {
    return false;
  }

  // Wszystkie znaki muszą być cyframi
  if (!/^\d+$/.test(pesel)) {
    return false;
  }

  return true;
}

function validateEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

function validateLength(value: string, minLength: number): boolean {
  return value.length >= minLength;
}

