import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

interface Post {
    id: number;
    creatorName: string;
    content: string;
    createdAt: string;
    likes: number;
}

@Component({
    selector: 'app-timeline',
    templateUrl: './timeline.component.html',
    styleUrls: ['./timeline.component.css']
})
export class TimelineComponent implements OnInit, OnDestroy {
    errorPost: string = '';
    errorSearch: string = '';
    newPostContent: string = '';
    timeline: Post[] = [];
    searchServer: string = '';
    searchQuery: string = '';
    searchResults: Post[] = [];
    private eventSource: EventSource | null = null;

    constructor(private http: HttpClient, private ngZone: NgZone) {
    }

    ngOnInit(): void {
        this.subscribeToTimeline();
    }

    ngOnDestroy() {
        if (this.eventSource) {
            this.eventSource.close();
        }
    }

    subscribeToTimeline() {
        this.eventSource = new EventSource('/rest/api/timeline');
        this.eventSource.onmessage = (event) => {
            this.ngZone.run(() => {
                const updatedPost = JSON.parse(event.data) as Post;
                const index = this.timeline.findIndex(post => post.id === updatedPost.id /*&& post.server === updatedPost.server*/);
                if (index !== -1) {
                    // Post aktualisieren
                    this.timeline[index] = updatedPost;
                } else {
                    // Neuen Post hinzufügen
                    this.timeline.push(updatedPost);
                }
            });
        };
        this.eventSource.onerror = (event) => {
            console.error("EventSource failed:", event);
            // Versuche die Verbindung nach einer kurzen Pause wiederherzustellen
            setTimeout(() => {
                // TODO this.subscribeToTimeline();
            }, 5000); // 5 Sekunden warten
        };
    }

    createPost(): void {
        let content = this.newPostContent.trim();
        if (content !== '') {
            this.http.post<{ success: string, error: string }>('/rest/api/post', { content: content }).subscribe({
                next: (data) => {
                    if (data.error) {
                        this.errorPost = `Server Error: ${data.error}`;
                    } else {
                        this.newPostContent = '';
                        this.errorPost = '';
                    }
                },
                error: (err) => {
                    this.errorPost = `Client Error: ${err.message}`;
                }
            });
        } else {
            this.errorPost = "Post content cannot be empty.";
        }
    }

    searchPosts(): void {
        let server = this.searchServer.trim();
        let query = this.searchQuery.trim();
        if (query !== '') {
            this.searchResults = [];
            this.http.get<{ searchResults: Post[], error: string }>(`/rest/api/search?s=${server}&q=${query}`).subscribe({
                next: (data) => {
                    if (data.error) {
                        this.errorSearch = `Server Error: ${data.error}`;
                    } else {
                        this.errorSearch = '';
                        this.searchResults = data.searchResults || [];
                    }
                },
                error: (err) => {
                    this.errorSearch = `Client Error: ${err.message}`;
                }
            });
        } else {
            this.errorSearch = "Search query cannot be empty.";
        }
    }

    likePost(post: Post): void {
        this.http.post<{ success: string, error: string }>('/rest/api/like', {
            postId: post.id,
            creatorName: post.creatorName,
        }).subscribe({
            next: (data) => {
                if (data.error) {
                    this.errorSearch = `Server Error: ${data.error}`;
                } else {
                    this.errorSearch = '';
                }
            },
            error: (err) => {
                this.errorSearch = `Client Error: ${err.message}`;
            }
        });
    }

}
