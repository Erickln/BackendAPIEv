# BackendAPIEv

## Backend Technical Assessment

Develop a Java middleware API that integrates with the [TVmaze API](https://www.tvmaze.com/api) and provides show search, show details, MongoDB caching, and user comments and ratings.

## Requirements

- **Language:** Java.
- **Version control:** Use Git and create a separate commit for each development milestone.
- **Repository:** Host the project in a private GitHub repository and grant Pinwox access.
- **Code quality:** Follow clean code principles and maintain a clear, maintainable architecture.
- **Database:** Create a free MongoDB Atlas instance without IP restrictions, as requested by the assessment.

## Implementation Tasks

Complete the tasks incrementally, committing each milestone before proceeding to the next.

### 1. Show Search Endpoint

Create an endpoint that searches for TV shows using a search query.

**TVmaze API:** `GET https://api.tvmaze.com/search/shows?q={query}`

**Input:**
- `search_query`: Search criteria.

**Response:**

Return an array of shows containing the following attributes:

- `id`
- `name`
- `channel`: Network name or web channel name.
- `summary`
- `genres`

### 2. Show Details Endpoint

Create an endpoint that retrieves a show's complete information using its ID.

**TVmaze API:** `GET https://api.tvmaze.com/shows/{show_id}`

**Input:**
- `show_id`: The show's unique identifier.

**Response:**

Return the complete show object provided by TVmaze.

### 3. Show Details Caching

Modify the show details endpoint to cache show information in MongoDB.

**Expected behavior:**

1. Search MongoDB for the requested show ID.
2. If the show exists in the database, return the cached object without calling TVmaze.
3. If the show does not exist, retrieve it from TVmaze.
4. Save the retrieved show in MongoDB.
5. Return the show object.

### 4. Comments and Ratings Endpoint

Create an endpoint that stores comments and ratings for a specific show in a MongoDB collection.

**Input:**
- `show_id`: The ID of the associated show.
- `comment`: The user's comment.
- `rating`: A numeric rating between 0 and 5, inclusive.

**Response:**

Return the status of the operation.

### 5. Include Comments in Show Search Results

Modify the show search endpoint to include the comments and ratings stored in MongoDB for each show returned by TVmaze.

**Response:**

Return an array of shows with the following attributes:

- `id`
- `name`
- `channel`: Network name or web channel name.
- `summary`
- `genres`
- `comments`: An array containing the comments associated with the show.

Each comment object must contain:

- `comment`
- `rating`

Example response:

```json
[
  {
    "id": 1,
    "name": "Under the Dome",
    "channel": "CBS",
    "summary": "Show summary",
    "genres": ["Drama", "Science-Fiction"],
    "comments": [
      {
        "comment": "Great show!",
        "rating": 5
      }
    ]
  }
]
```

### 6. Include Comments in Show Details

Modify the show details endpoint to include the comments and ratings stored in MongoDB for the requested show.

**Expected behavior:**

1. Retrieve the show using the caching logic described above.
2. Retrieve all comments associated with the show ID.
3. Add the comments array to the show object.
4. Return the enriched show object.

## External Resources

- [TVmaze API Documentation](https://www.tvmaze.com/api)
- [MongoDB Atlas](https://www.mongodb.com/atlas)

## Development Guidelines

- Implement the requirements incrementally.
- Create a Git commit for each completed milestone.
- Keep controllers, business logic, and database access responsibilities separated.
- Validate incoming requests, including rating values.
- Handle errors from external API calls and database operations appropriately.
- Avoid unnecessary code duplication.
- Document the endpoints and their expected inputs and outputs.

> "Clean code always looks like it was written by someone who cares."
