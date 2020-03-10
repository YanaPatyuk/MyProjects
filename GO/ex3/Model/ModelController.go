package Model

import (
	"log"
	"net/url"
)

func ReciveAndSendToController(ReciveQuryFromController chan string, sendQuerytoController chan string) {
	go func() {
		var text string
		for text != "q" { // break the loop if text == "q"
			text := <-ReciveQuryFromController //get the food to find
			//The database is the next website. create it's url
			if text != "q" {
				u, err := url.Parse("https://www.allrecipes.com/search/results")
				if err != nil {
					log.Fatal(err)
				}
				u.Scheme = "https"
				u.Host = "allrecipes.com"
				q := u.Query()
				//set the query to find
				q.Set("wt", text)
				u.RawQuery = q.Encode()
				//return the link as a string to the controller
				sendQuerytoController <- u.String()
			} else {//if we need to quit-update controller for quit message
				sendQuerytoController <-text
			}
		}
	}()
}

