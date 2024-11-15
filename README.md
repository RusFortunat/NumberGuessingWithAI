# Number Guessing with AI

A Spring Boot MVC web application that uses a pre-trained neural network to guess handwritten numbers that user provides. Click [here](https://number-guessing-app-c2a0dae4c4g0cxas.westeurope-01.azurewebsites.net/write-number) to try it out.

![Short demo:](https://github.com/RusFortunat/NumberGuessingWithAI/blob/main/src/main/resources/media/demo.gif)

# Description

## Model

The app uses a pre-trained neural network to process user image and produce the answer. The neural networks was trained on MNIST handwritten digits datasets with use of a [Machine Learning package](https://github.com/RusFortunat/java_ML_library) that I have written myself in Java. The network's guessing accuracy is about 89%: it guesses well 0-5 digits, and almost never guesses 7 correctly.. For better results, try drawing numbers in the middle of the drawing field and avoid touching the borders.


## View

The main HTML page uses Thymeleaf engine to relate the Model attributes with the page elements, and AJAX, Fetch methods to send the client requests to the server. The HTML document tree structure consists of the root element and a fragment node that is responsible for fetching and displaying the data received from the server. In addition to the networks answer, the page also shows how confident the network is in the answer it provided by showing all ten digits with different opacity levels. The more certain the network is about the image corresponding to a certain digit, the brigter this digit appears on the page. This feature was achieved with dynamically applying CSS styling. 


## Controller

Communication between the client and the server was implemented with use of Spring Boot open-source Java framework. The Controller has two mappings -- GetMapping for accessing root endpoint and PostMapping for handling POST requests that carry raw pixel data. After receiving a POST request, the controller calls methtods for processing image data and getting the neural network's prediction. After that the controller calls a method for saving the image in the database and returns predictions to a fragment HTML node.


## Basic CRUD 

All user images that have been sent to the server are saved in the H2 Database that is stored in the project's resource folder. At the moment, only Create, Read, and Delete methods have been implemented, and I plan to add Update method as well for users to be able to add labels to the images that are stored in the database. 


## Author
Ruslan Mukhamadiarov

## License
This project is licensed under the MIT License - see the LICENSE.md file for details
