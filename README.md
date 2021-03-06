
# Movies Demo App

The XML UI version of the Movies demo app. 
Built this app as a demo to show to the interviewers. 
This demo app fetches the data related to a movies catalogue and show it in the UI.

## PS
I'm leaving the api key hardcoded in the ApiKeyInterceptor class to simplify the usage of the demo app, i'm usually using Secrets Gradle Plugin to hide the api key in the local.properties file, and expose it via a variable BuildConfig.apiKey 

#### Get popular movies

```http
  GET /movie/popular
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `API_KEY` | `string` | **Required**. TMDB Api key, i'm leaving mine here to be deleted after i finish the interviews|

#### Returns the details for a specific movie

```http
  GET /movie/{movieId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of movie to fetch |

## Features

- Show list of popular movie
- Show details related to a movie
- Add a movie to favourites 
- set nightmode via settings



## Android Architecture Components samples

Android Architecture Components used
- Room
- Retrofit
- Pager 3 
- Viewmodels
- Flow 
- Navigation
- ViewBinding 
- DataStore
