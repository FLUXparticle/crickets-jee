### Profil-Diagramm

``` mermaid
classDiagram
    %% Klassen
    class AuthResource {
        +login(credentials: Credentials, request: HttpServletRequest) Response
    }

    class Credentials {
        -username: String
        -password: String
        +getUsername() String
        +setUsername(username: String) void
        +getPassword() String
        +setPassword(password: String) void
    }

    class UserResource {
        +getUsername() Response
        +getUser() User
    }

    class UserService {
        -VALID_API_KEYS: Set<String>
        +getUser(username: String) User
        +checkApiKey(apiKey: String) boolean
    }

    class ProfileResource {
        +getProfile() Response
        +subscribe(request: SubscribeRequest) Response
    }

    class ProfileService {
        +subscriberCount(creatorId: int) int
        +subscribe(subscriber: User, server: String, creatorName: String) String
    }

    class SubscriptionRepository {
        +findAll() List~Subscription~
        +findByCreatorId(creatorId: int) List~Subscription~
        +findBySubscriberServerAndSubscriberId(subscriberServer: String, subscriberId: int) List~Subscription~
        +save(subscription: Subscription) void
    }

    class UserRepository {
        +findByUsername(username: String) User
        +save(user: User) void
    }

    %% Verbindungen
    AuthResource --> Credentials : verwendet
    AuthResource --> UserService : login()
    UserResource --> UserService : getUser()
    ProfileResource --> ProfileService : getProfile(), subscribe()
    ProfileService --> SubscriptionRepository : findByCreatorId(), save()
    ProfileService --> UserRepository : findByUsername()
    UserService --> UserRepository : findByUsername()
```

### Timeline-Diagramm

``` mermaid
classDiagram
    %% Klassen
    class TimelineResource {
        +search(server: String, query: String) Response
        +post(request: PostRequest, securityContext: SecurityContext) Response
        +timeline(securityContext: SecurityContext) Response
        +likePost(request: LikePostRequest, securityContext: SecurityContext) Response
    }

    class PostRequest {
        -content: String
        +getContent() String
        +setContent(content: String) void
    }

    class LikePostRequest {
        -postId: long
        -creatorName: String
        +getPostId() long
        +setPostId(postId: long) void
        +getCreatorName() String
        +setCreatorName(creatorName: String) void
    }

    class TimelineService {
        +getTimelineUpdates(subscriberID: int) BlockingQueue~Post~
        +post(creatorName: String, content: String) void
        +search(server: String, query: String) List~Post~
        +likePost(postID: long, creatorName: String) void
        +getRemoteClient(server: String) TimelineRemoteService
    }

    class TimelineRemoteService {
        <<interface>>
        +post(creatorName: String, content: String) void
        +likePost(postId: long, creatorName: String) void
        +sendUpdate(post: Post) void
        +search(server: String, query: String) List~Post~
    }

    class TimelineRemoteServiceImpl {
        +post(creatorName: String, content: String) void
        +likePost(postId: long, creatorName: String) void
        +sendUpdate(post: Post) void
        +search(server: String, query: String) List~Post~
    }

    class PostRepository {
        +findById(id: long) Post
        +save(post: Post) void
        +findByContentContains(query: String) List~Post~
    }

    class UserRepository {
        +findByUsername(username: String) User
    }

    class SubscriptionRepository {
        +findBySubscriberServerAndSubscriberId(subscriberServer: String, subscriberId: int) List~Subscription~
    }

    class PubSub {
        +subscribe(topic: String) BlockingQueue~Post~
        +unsubscribe(topic: String, queue: BlockingQueue~Post~) void
        +publish(topic: String, post: Post) void
        +aggregate(queues: List~BlockingQueue~Post~) BlockingQueue~Post~
    }

    %% Verbindungen
    TimelineResource --> TimelineService : search(), post(), timeline(), likePost()
    TimelineService --> PostRepository : findById(), save(), findByContentContains()
    TimelineService --> SubscriptionRepository : findBySubscriberServerAndSubscriberId()
    TimelineService --> UserRepository : findByUsername()
    TimelineService --> PubSub : subscribe(), publish()
    TimelineService --> TimelineRemoteService : getRemoteClient()
    TimelineRemoteServiceImpl ..|> TimelineRemoteService

