# News App

A simple News App built with modern Android development practices, including MVVM architecture, Jetpack Compose for UI, Hilt for dependency injection, and Retrofit for network operations. The app supports dark and light themes and incorporates Firebase Analytics and network connectivity monitoring.

## Features

- **News Headlines**: Fetch and display the latest news headlines.
- **Search Articles**: Search for articles by keyword.
- **Save Favorites**: Save and manage favorite articles locally.
- **Dark/Light Theme**: Automatically adapts to the system's theme.
- **Network Connectivity**: Monitors and displays real-time network status.
- **Secure API Management**: API keys are securely managed using the `local.properties` file.
- **Firebase Analytics**: Tracks user interactions for Viewing, Searching, Adding/removing favorites.

## Tech Stack

- **Programming Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Networking**: Retrofit
- **Local Storage**: Room Database
- **Build System**: Gradle


## Project Main Structure

- **Data Layer**: local, network, repository
- **DI**: Modules
- **Domain**: Model, Repository, Usecase
- **Presentation**: UI, ViewModels