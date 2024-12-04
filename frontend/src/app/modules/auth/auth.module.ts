import { NgModule } from '@angular/core';

import {provideHttpClient, withFetch} from '@angular/common/http';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { AuthRoutingModule } from './auth-routing.module';

@NgModule({ imports: [AuthRoutingModule, AngularSvgIconModule.forRoot()], providers: [
    provideHttpClient(
      withFetch()  
    )
  ] })
export class AuthModule {}
