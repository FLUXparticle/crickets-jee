import {Injectable, NgZone} from '@angular/core';
import {Observable, Subject} from 'rxjs';

export interface Message {
    creator_name: string;
    content: string;
    created_at: string;
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {
    private ws: WebSocket;
    private messages: Message[] = [];
    private messagesSubject: Subject<Message[]> = new Subject<Message[]>();
    public messages$: Observable<Message[]> = this.messagesSubject.asObservable();

    constructor(private ngZone: NgZone) {
        // Den WebSocket-Pfad konstruieren, der relativ zur aktuellen URL ist
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const host = window.location.hostname;
        const port = window.location.port ? ':' + window.location.port : '';

        this.ws = new WebSocket(`${protocol}//${host}${port}/ws/chat`);

        this.ws.onmessage = (event: MessageEvent) => {
            try {
                // JSON-String in ein Message-Objekt umwandeln
                console.log('event.data:', event.data);
                const message: Message = JSON.parse(event.data);
                console.log('message:', message);
                this.ngZone.run(() => {
                    this.messages.push(message);
                    this.messagesSubject.next(this.messages);
                });
            } catch (error) {
                console.error('Fehler beim Dekodieren der Nachricht:', error);
            }
        };

        this.ws.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        this.ws.onclose = () => {
            console.log('WebSocket connection closed');
        };
    }

    sendMessage(message: string) {
        if (this.ws.readyState === WebSocket.OPEN) {
            this.ws.send(JSON.stringify({ content: message }));
        } else {
            console.error('WebSocket is not open');
        }
    }

    closeSocket() {
     this.ws.close()
    }
}
