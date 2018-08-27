using ImageService.Commands;
using ImageService.Infrastructure;
using ImageService.Infrastructure.Enums;
using ImageService.Modal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Controller
{
    public class ImageController : IImageController
    {
        private IImageServiceModal m_modal;                      // The Modal Object
        private Dictionary<int, ICommand> commands;

        /// <summary>
        /// C'tor
        /// </summary>
        /// <param name="modal">IImageServiceModal object to control</param>
        public ImageController(IImageServiceModal modal)
        {
            m_modal = modal;                    // Storing the Modal Of The System
            commands = new Dictionary<int, ICommand>()
            {
                // For Now will contain NEW_FILE_COMMAND
                {(int) CommandEnum.NewFileCommand, new NewFileCommand(this.m_modal) }
            };
        }
        /// <summary>
        /// Execute given command
        /// </summary>
        /// <param name="commandID">to determine which command to execute from dictionary </param>
        /// <param name="args">array with args for the command</param>
        /// <param name="resultSuccesful">reference to result flag to update</param>
        /// <returns>string returned by the command / error</returns>
        public string ExecuteCommand(int commandID, string[] args, out bool resultSuccesful)
        {
            if (commands.ContainsKey(commandID))
            {
                return commands[commandID].Execute(args, out resultSuccesful);
            }
            else
            {
                resultSuccesful = false;
                return "No such command";
            }
        }
    }
}
