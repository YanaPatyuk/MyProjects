/*

 */

#ifndef COMMANDPROTOCOL_H_
#define COMMANDPROTOCOL_H_
#include <vector>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <iostream>
#include <stdio.h>
#include "CommandInfo.h"

using namespace std;

/**
 * Interface for commands
 */
class CommandProtocol {
public:
    /**
     * @param info CommandInfo struct with the needed data in order to execute the command
     */
	 virtual void Execute(struct CommandInfo info) = 0;
	 virtual ~CommandProtocol() {}
};



#endif /* COMMANDPROTOCOL_H_ */
