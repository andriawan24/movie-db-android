
# Movie DB Android

[![MovieDB CI/CD](https://github.com/andriawan24/movie-db-android/actions/workflows/actions.yml/badge.svg)](https://github.com/andriawan24/movie-db-android/actions/workflows/actions.yml)

The app is to showcase list-to-detail movie app

## Tech Stack

* **100% Kotlin**, using flow and coroutines for asynchronous background thread
* Networking with **Retrofit**
* **Dagger hilt** for dependency injection
* **Paging 3** to handle pagination easily
* **Kover** for unit test code coverage
* **Github Actions** for CI/CD

## Folder Structures
The structure module used follows the guidelines of a clean architecture by dividing it into several folders with the following details
- adapter : Recycler view adapters
- base : Handle base class for activity
- data : Used to handle all of the API calling in the application
- domain : Bridge between UI layer and data layer
- di : Stands for Dependency Injection, use to inject dependencies that a class will used
- ui : Handle user interface (divided into components and screens), every screens has subfolder and every subfolder contains at least viewmodel, state, and screen
- utils : Define helper const and functions

## Demonstration
<ul>
  <img width="200" alt="Screenshot_20251008_182353" src="https://github.com/user-attachments/assets/1c5a9fa7-ea15-489c-90df-6517c9bdc532" />
  <img width="200" style="margin-left: 2rem;margin-top:2rem" alt="Screenshot_20251008_182410" src="https://github.com/user-attachments/assets/ace8de10-66f6-4564-87a1-e0292d70c7b2" />
</ul>


## How to run the app
1. Clone this project using ```git clone https://github.com/andriawan24/movie-db-android```
2. Open the project using latest android studio
3. Open local.properties and set ```MOVIE_API_KEY={your_key_here}```
4. Choose emulator and run the app
5. Optional for device with low memory like me: Take a deep breath, make an coffee while waiting for the gradle to finished ><

## Contributing

There is heavy chance that the code may/may not be correct/holding best practices. I request you to contribute/raise issues/send PRs so I can learn too. You can use the Github Discussion to discuss and ask questions. Or you can reach out to me on email fawaznaufal23@gmail.com

## Feedback

If you have any feedback, please reach out me at fawaznaufal23@gmail.com

