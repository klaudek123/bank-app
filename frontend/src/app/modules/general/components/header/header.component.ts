import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../services/axios.service';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  showLogout: boolean = false;
  user: any = { name: '', lastname: '' }; 

  
  constructor(private router: Router, private axiosService: AxiosService){ }

  ngOnInit(): void {
     const idAccount = localStorage.getItem('idAccount'); 
     console.log("idAccount = "+idAccount); 
     if (idAccount) {
       this.axiosService.request('GET', `${environment.apiUrl}/users/${idAccount}`, {})
        .then(
           (response) => {
            console.log(response);
             this.user.name = response.data.firstname;
             this.user.lastname = response.data.lastname;
           },
           (error) => {
            if (error.response.status === 401) {
              this.axiosService.setAuthToken(null);
            } else {
                this.user.name = error.response.code;
            }
            console.error('Błąd podczas pobierania danych użytkownika.', error);
           }
         );
     }
  }

  showLogoutPanel(): void {
    this.showLogout = true;
  }

  hideLogoutPanel(): void {
    this.showLogout = false;
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
