
using Logging.Modal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Logging
{
    public class LoggingService : ILoggingService
    {
        public event EventHandler<MessageRecievedEventArgs> MessageRecieved;
        /// <summary>
        /// Log recevied, raise the event (call) the subscribed methods.
        /// - calls the function needed to be called when new message has been logged. 
        /// </summary>
        /// <param name="message">string contain the message</param>
        /// <param name="type">what kind of message (INFO, WARNING, FAIL)</param>
        public void Log(string message, MessageTypeEnum type)
        {
            MessageRecieved?.Invoke(this, new MessageRecievedEventArgs(message, type));
        }
    }
}
