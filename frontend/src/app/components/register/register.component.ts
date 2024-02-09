import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  redirectToLogin: boolean = false;
  firstname: string = "";
  lastname: string = "";
  email: string = "";
  password: string = "";
  passwordCheck: string = "";
  acceptTerms: boolean = false;

  // constructor(private router: Router, private http: HttpClient) {}

  // handleFormSubmit(): void {
  //   if (this.password !== this.passwordCheck) {
  //     window.alert("Hasło i potwierdzenie hasła nie są identyczne");
  //     return;
  //   }

  //   const client = { firstname: this.firstname, lastname: this.lastname, email: this.email, password: this.password };

  //   this.http.post<any>('http://localhost:8080/api/v1/auth/register', client)
  //     .subscribe(
  //       () => {
  //         this.redirectToLogin = true;
  //       },
  //       error => {
  //         console.error('Błąd podczas rejestracji.', error);
  //       }
  //     );
  // }
}
