using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Controller
{
    public interface IImageController
    {
        /// <summary>
        /// Execute given command
        /// </summary>
        /// <param name="commandID">to determine which command to execute from dictionary </param>
        /// <param name="args">array with args for the command</param>
        /// <param name="result">reference to result flag to update</param>
        /// <returns>string returned by the command</returns>
        string ExecuteCommand(int commandID, string[] args, out bool result);          // Executing the Command Requet
    }
}
