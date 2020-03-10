package main

import (
	"Sous_Chef/Model"
	"Sous_Chef/View"
)



func main() {
	SendRequestToserver := make(chan string)       //create control to server chan
	ReciveRequestToserver := make(chan string)       //create Server to control  chan
	ReciveRequestFromClient := make(chan string)       //create control to view  chan
	SendAnswersToClient := make(chan string)       //create view to control  chan

	//start running the view and the model.
	View.SendReciveFromController(SendAnswersToClient,ReciveRequestFromClient)
	Model.ReciveAndSendToController(SendRequestToserver,ReciveRequestToserver)


	var data string
	//manage the information between Model and View
	//each data from client will sent to server.

	for data != "q" {
		data = <-ReciveRequestFromClient //get the food the client want
		SendRequestToserver<-data //send to the server to get the recepies from data-base
		ans:= <-ReciveRequestToserver //get the recpies from the server
		SendAnswersToClient<-ans //send recepies back to client
	}
}

