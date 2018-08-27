using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Comunication.Server
{
    public interface IClientHandler
    {
        void HandleClient(TcpClient client);
    }
}
