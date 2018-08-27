using ImageService.Infrastructure;
using ImageService.Modal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Commands
{
    public class NewFileCommand : ICommand
    {
        private IImageServiceModal m_modal;

        public NewFileCommand(IImageServiceModal modal)
        {
            m_modal = modal;            // Storing the Modal
        }

        /// <summary>
        /// NewFile recevied into logged folder, we need to execute something (in this case AddFile method)
        /// </summary>
        /// <param name="args">args for the command</param>
        /// <param name="result">reference to result flag to update</param>
        /// <returns>an *error message* / image path (if you use it it will open the image)</returns>
        public string Execute(string[] args, out bool result)
        {
            // The String Will Return the New Path if result = true, and will return the error message otherwise
            // args[0] - since we need only the first parameter
            // out result will update the given result flag to true/false according to AddFile
            return m_modal.AddFile(args[0], out result);
        }
    }
}
