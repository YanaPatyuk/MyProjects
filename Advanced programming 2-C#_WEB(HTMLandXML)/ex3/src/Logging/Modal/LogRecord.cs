using Logging.Modal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Logging.Modal
{
   public  class LogRecord : EventArgs
    {
        private MessageTypeEnum m_type;
        private string m_msg;

        public MessageTypeEnum Type { get { return m_type; } }
        public string Message { get { return m_msg; } }
        /// <summary>
        /// save the inforametion about the lof.
        /// </summary>
        /// <param name="type">MessageTypeEnum</param>
        /// <param name="msg">message of the log</param>
        public LogRecord(MessageTypeEnum type, string msg)
        {
            this.m_type = type;
            this.m_msg = msg;
        }
    }
}
