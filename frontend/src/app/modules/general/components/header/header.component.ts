import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  showLogout: boolean = false;
  user: any = { name: 'John', lastname: 'Doe' }; // Przykładowe dane użytkownika

  
  constructor(private router: Router){ }

  ngOnInit(): void {
  }

  showLogoutPanel(): void {
    this.showLogout = true;
  }

  hideLogoutPanel(): void {
    this.showLogout = false;
  }

  logout(): void {
    // Tutaj można dodać logikę wylogowania użytkownika
    this.router.navigate(['/login']);
  }
}
