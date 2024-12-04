import {provideHttpClient, withFetch} from '@angular/common/http';
import { NgModule } from '@angular/core';
import { AngularSvgIconModule } from 'angular-svg-icon';

import { LayoutRoutingModule } from './layout-routing.module';
@NgModule({ imports: [LayoutRoutingModule, AngularSvgIconModule.forRoot()], providers: [provideHttpClient(withFetch())] })
export class LayoutModule {}
