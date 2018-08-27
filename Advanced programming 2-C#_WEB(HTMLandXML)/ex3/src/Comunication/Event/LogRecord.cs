//using Comunication.Event;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Comunication.Event
{
   public  class LogRecord : EventArgs
    {
        private MessageTypeEnum type;
        private string msg;

        public MessageTypeEnum Type { get { return type; } }
        public string Message { get { return msg; } }

        public LogRecord(MessageTypeEnum type, string msg)
        {
            this.type = type;
            this.msg = msg;
        }
    }
}
