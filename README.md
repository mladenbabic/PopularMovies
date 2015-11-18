## Popular Movies
Udacity Movie Project (Stage 1 & 2)

The Popular Movies project is made and used for code reviewing in the Udacity Android Nanodegree course for Movie Project 1 & 2 phases. 
It is the functional project version based on the project requirements.

[![Popular Movies Video](https://j.gifs.com/mGQkpo.gif)](https://youtu.be/L0DmJv6pHOY)

## Instalation

The project retrieves the movie data from [The Movie Database](https://www.themoviedb.org/documentation/api). The app requires your API key (which you generated on [The Movie Database](https://www.themoviedb.org/documentation/api)) to work properly. When you obtain API key, replace YOUR_API_KEY with your API key in the build.gradle file.
      
       buildTypes.each {
            it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', '"YOUR_API_KEY"'
       }


## Screens

![screen](../art/phone-movies.png)

![screen](../master/art/phone-details.png)

![screen](../master/art/tablet-port.png)

![screen](../master/art/tablet-land.png)

## Third-party Libraries

* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Retrofit](https://github.com/square/retrofit)
* [Picasso](https://github.com/square/picasso)
* [Google Gson](https://github.com/google/gson)
* [Facebook Stetho](https://github.com/facebook/stetho)
* [Cupboard](https://bitbucket.org/littlerobots/cupboard)


## Licence 

          Copyright (C) 2015  Mladen Babic - Popular Movies Project for Udacity Android Nanodegree
        
          Licensed under the Apache License, Version 2.0 (the "License");
          you may not use this file except in compliance with the License.
          You may obtain a copy of the License at
        
              http://www.apache.org/licenses/LICENSE-2.0
        
          Unless required by applicable law or agreed to in writing, software
          distributed under the License is distributed on an "AS IS" BASIS,
          WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
          See the License for the specific language governing permissions and
          limitations under the License.