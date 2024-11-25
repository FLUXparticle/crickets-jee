import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
    success: string = '';
    error: string = '';
    subscriberCount: number = 0;
    subscribeForm = {
        server: '',
        creatorName: ''
    };

    constructor(private http: HttpClient) {}

    ngOnInit(): void {
        this.getProfile();
    }

    getProfile(): void {
        this.http.get<{ subscriberCount: number, error: string }>('/rest/api/profile')
            .subscribe({
                next: (data) => {
                    this.subscriberCount = data.subscriberCount;
                    if (data.error) {
                        this.error = `Server Error: ${data.error}`;
                    } else {
                        this.error = '';
                    }
                },
                error: (err) => {
                    this.success = '';
                    this.error = `Client Error: ${err.message}`;
                }
            });
    }

    onSubmit(): void {
        this.http.post<{ success: string, error: string }>('/rest/api/subscribe', this.subscribeForm)
            .subscribe({
                next: (data) => {
                    this.success = data.success || '';
                    if (data.error) {
                        this.error = `Server Error: ${data.error}`;
                    } else {
                        this.error = '';
                    }
                },
                error: (err) => {
                    this.success = '';
                    this.error = `Client Error: ${err.message}`;
                }
            });
    }
}
