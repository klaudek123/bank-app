import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SocketIoConfig, SocketIoModule  } from 'ngx-socket-io';
import { environment } from '../environments/environment';
import { RouterModule } from '@angular/router';


const socketConfig: SocketIoConfig = {
    url: `${environment.apiUrl}/ws`,
    options: {
      transports: ['websocket'], 
      reconnection: true,
      reconnectionAttempts: 10,
      reconnectionDelay: 1000,
    },
  };
  
@NgModule({ 
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
        NotFoundComponent
    ],
    bootstrap: [AppComponent], 
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        RouterModule,
        SocketIoModule.forRoot(socketConfig)], 
    providers: [provideHttpClient(withInterceptorsFromDi())],
    })
export class AppModule { }
