# PopularMovies
Udacity Movie Project (Stage 1)

The PopularMovies project is made and used for code reviewing in the Udacity Nanodegree course as Movie Project Stage 1 phase.

[![Popular Movies Video](https://j.gifs.com/mGQkpo.gif)](https://youtu.be/L0DmJv6pHOY)

#Instalation

The project retrieves the movie data from http://themoviedb.com. The app requires your API key (which you generated on themoviedb.com) to work properly. When you obtain API key, replace YOUR_API_KEY with your API key in the build.gradle file.
      
       buildTypes.each {
            it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', '"YOUR_API_KEY"'
       }


