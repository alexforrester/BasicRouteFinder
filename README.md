# BasicRouteFinder
Plot route finder directions from origin to destination using the Google Maps and Directions APIs

The app uses Butter Knife for View injection and click listener handling, Retrofit for Networking and
services and receivers to dispatch work onto a background thread and update on main ui thread.

There are tests available which can be run with ./gradlew testDebug

Lint can be run with ./gradlew lint