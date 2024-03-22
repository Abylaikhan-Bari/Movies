package com.aikei.movies.util

import com.aikei.movies.data.api.model.Movie
import com.aikei.movies.data.api.model.MovieDetails
import com.aikei.movies.data.db.entities.PopularMovie
import com.aikei.movies.data.api.model.Genre
import com.aikei.movies.presentation.model.PresentationMovie

object MovieMapper {

    fun Movie.toPopularMovieEntity(): PopularMovie {
        return PopularMovie(
            id = this.id,
            title = this.title,
            posterUrl = this.posterUrl, // Ensure this matches the field in the PopularMovie entity
            overview = this.overview,
            releaseDate = this.releaseDate,
            rating = this.rating
        )
    }


    fun PopularMovie.toPresentationMovie(): PresentationMovie {
        return PresentationMovie(
            id = this.id,
            title = this.title,
            posterUrl = this.posterUrl,
            overview = this.overview,
            // Provide a default value for releaseDate if it's null
            releaseDate = this.releaseDate ?: "Not Available",
            rating = this.rating,
            genres = emptyList(), // Placeholder for genre conversion if needed
            runtime = 0 // Placeholder for runtime if needed
        )
    }

    fun MovieDetails.mapToPresentation(): PresentationMovie = PresentationMovie(
        id = this.id,
        title = this.title,
        posterUrl = this.posterUrl, // Make sure the posterPath matches the field in PresentationMovie
        overview = this.overview,
        releaseDate = this.releaseDate,
        rating = this.rating,
        genres = this.genres.map { genre -> Genre(genre.id, genre.name) }, // Map Genre from MovieDetails to Genre in PresentationMovie
        runtime = this.runtime
    )
}
