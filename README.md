# Letterboxd Clone

> This README is still in progress and will be updated as the project develops.

A Java Swing desktop application for discovering movies, maintaining a watchlist,
recording ratings and opinions, and exploring recommendations and viewing statistics.

## Team Members 
Aarit Dua  
Bayan Abou Rekbeh  
Benjamin Kaganovitch  
Christian Cavaliere  
Shariah Mirza  

## User Stories 
### As a user I want to ..
Search movies by title and select a result  
Save movies to watch, track watched movies, and record ratings and opinions  
Discover movies based on films I rated highly  
See a movie’s poster, title, year, genres, director, cast, and my watched status and rating  
See my highest-rated genre, total minutes watched, and minutes watched per month  

## MVP

Search for a movie, view its details, save it to a watchlist or viewing history,
and use saved ratings and activity to show basic recommendations and statistics.
Each teammate owns the corresponding feature above.

## Use Cases

### Use Case 3: Get Recommendations (MVP, lead: Benjamin Kaganovitch)

**Actor:** User

**Main flow:**
1. The user opens the Recommendations page and asks for recommendations.
2. The app finds the movies the user rated 4 stars or higher (up to their 5 best rated).
3. For each of those movies, the app looks up similar movies.
4. The app leaves out movies the user has already rated and lists each movie only once.
5. The app ranks the movies, putting first the ones recommended by more (and higher-rated) favourites.
6. The app shows the top 10, each with the favourite it came from ("because you liked ...").

**Alternative flows:**
- 2a. The user has not rated any movie 4 stars or higher: the app asks them to rate some movies 4 stars or higher first.
- 3a. The movie information cannot be loaded (for example, no internet connection): the app shows an error message and the user can try again.
- 4a. Every suggested movie has already been rated: the app says there are no new recommendations and suggests rating more movies.

## Technology and API

- **Java 11**, **Swing**, and **Maven**.
- **[TMDB API](https://developer.themoviedb.org/docs/getting-started)** 


## How to Run


## Design and Progress


