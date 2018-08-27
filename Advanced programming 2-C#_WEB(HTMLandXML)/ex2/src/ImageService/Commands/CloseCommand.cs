using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ImageService.Commands;
using ImageService.Controller.Handlers;
using Infrastructure.Enums;
using Logging;
using ImageService.Modal;
using ImageService.Server;
using Comunication.Event;
using Logging.Modal;

namespace ImageService.Commands
{
    class CloseCommand : ICommand
    {
        private ILoggingService m_service;
        /// <summary>
        /// save ILoggingService as paramter to send log to notify changes.
        /// </summary>
        /// <param name="service"></param>
        public CloseCommand(ILoggingService service)
        {
            m_service = service;
        }

        /// <summary>
        /// close recevied from client. we delete the handler by the given path and
        /// notify logger(withch will notify other clients)
        /// </summary>
        /// <param name="args">args for the command</param>
        /// <param name="result">reference to result flag to update</param>
        /// <returns>an *error message* / folder pathbeing closed</returns>
        public string Execute(string[] args, out bool result)
        {
            // The String Will Return the New Path if result = true, and will return the error message otherwise
            result = true;
            HanddlersController handlersList = HanddlersController.Instance;
            MessageTypeEnum resultOfDeleting = MessageTypeEnum.FAIL;
            string status = handlersList.DeleteHandller(args[1] + ":" + args[2]);
            //if(status != )
            m_service.Log("close handler:" + args[1] + ":"+ args[2], MessageTypeEnum.INFO);
            AppCongigSettings settings = AppCongigSettings.Instance;
            
            settings.Handlers = settings.Handlers.Replace(args[1] + ":" + args[2] + ";", "");
            if (handlersList.Handdlers.Count() == 0) settings.Handlers = "";
            if (status.Equals("deleted"))
            {
                resultOfDeleting = MessageTypeEnum.INFO;
            }
            ServerDataReciecedEventArgs a = new ServerDataReciecedEventArgs("Log", (int)resultOfDeleting + ":your reqest was:" + status);

            return a.ToJSON();

        }
    }
}
