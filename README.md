# PopularMovies
Udacity Movie Project (Stage 1)

This project is made and used for code reviewing in Udacity Nanodegree course.

#Instalation

The project retrieves a movie data from http://themoviedb.com. The app requires your API key (which you generated from themoviedb.com) to work properly. When you obtain API key, replace YOUR_API_KEY with your API key in the build.properties file.
      
       buildTypes.each {
            it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', '"YOUR_API_KEY"'
       }


