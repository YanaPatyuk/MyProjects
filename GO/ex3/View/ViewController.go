package View

import (
	"bufio"
	"fmt"
	"os"

)

func SendReciveFromController(reciveLinkFromController chan string, sendQuerytoController chan string) {
	go func() {
		scanner := bufio.NewScanner(os.Stdin)
		var text string
		fmt.Print("Hello! Welcome to Sous Chef!\n" +
			"Write the food you want to prepare and we will give you a link to the recipes!\n" +
			"For example: if you wand salad, you will get the link: https://www.allrecipes.com/search/results/?wt=salad \n" +
			"Just click on it!" +
			"to exit press q\n\n")
		text = "y"
		for text != "q" { // break the loop if text == "q"-means to exit
			fmt.Print("What do you want to eat?\n")
			//get input from user
			scanner.Scan()
			text = scanner.Text()
			fmt.Println("You asked for: ", text, "please wait...")
			//send request to controller
			sendQuerytoController <- text
			//wait for answer from controller
			listOfrecepies := <-reciveLinkFromController
			fmt.Print( listOfrecepies, "\n")
		}
	}()
}