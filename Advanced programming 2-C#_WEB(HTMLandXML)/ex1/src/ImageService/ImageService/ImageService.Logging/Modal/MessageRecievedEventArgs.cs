using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Logging.Modal
{
    public class MessageRecievedEventArgs : EventArgs
    {
        public MessageTypeEnum Status { get; set; }
        public string Message { get; set; }

        /// <summary>
        /// C'tor for MessageRecievedEventArgs, recevies message and type of her
        /// </summary>
        /// <param name="message">string contain the message</param>
        /// <param name="status">what kind of message (INFO, WARNING, FAIL)</param>
        public MessageRecievedEventArgs(string message, MessageTypeEnum status)
        {
            this.Message = message;
            this.Status = status;
        }
    }
}
