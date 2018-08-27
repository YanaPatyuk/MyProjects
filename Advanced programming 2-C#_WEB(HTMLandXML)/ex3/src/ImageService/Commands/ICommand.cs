using Infrastructure.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Commands
{
    public interface ICommand
    {
        /// <summary>
        /// Execute the command
        /// </summary>
        /// <param name="args">args for the command</param>
        /// <param name="result">reference to result flag to update</param>
        /// <returns>string returned by the command</returns>
        string Execute(string[] args, out bool result);          // The Function That will Execute The 
    }
}
