# SWAPI test Android application

## Description

Clean architecture is used,

Android module contains Android UI elements

Other modules can be used for other types of operating systems supported by [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
For this, [Ktor](https://ktor.io/) is used for networking, which supports many operating systems

Pagination is used for the list of persons.

[SWAPI DEV API](https://swapi.dev/api/) is used to obtain data

The application uses separate Data, Network and UI models and mappers to convert them

To display all lists, the library [AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates) is used, which allows you to flexibly use RecyclerView by adding elements of different types to the list.

In the future, [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) can be used as a universal UI option
Now I did not use it, since in a short period of time it was not possible to make a working pagination with correct updating by search word

Time spent: 15.28 - 19.39 with 2 breaks, totaling about 30 minutes

For Jenkins- check /jenkins folder