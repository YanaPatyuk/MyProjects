using ImageService.Controller;
using ImageService.Controller.Handlers;
using ImageService.Infrastructure.Enums;
using ImageService.Logging;
using ImageService.Modal;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ImageService.Server
{
    public class ImageServer
    {
        #region Members
        private IImageController m_controller;
        private ILoggingService m_logging;
        #endregion

        #region Properties
        public event EventHandler<CommandRecievedEventArgs> CommandRecieved;          // The event that notifies about a new Command being recieved
        #endregion

       /// <summary>
       /// C'tor of server.
       /// creates handler's for each path from given app config. 
       /// listen to each handler.
       /// </summary>
       /// <param name="controller"></param>
       /// <param name="logging"></param>
        public ImageServer(IImageController controller, ILoggingService logging)
        {
            this.m_logging = logging;
            this.m_controller = controller;
            //create list of paths to listen them.
            string[] handlerListPaths = ConfigurationManager.AppSettings["Handler"].Split(';');
            //create handler to each path and listen to its updates.
            foreach (var path in handlerListPaths)
            {
                IDirectoryHandler handler = new DirectoyHandler(this.m_controller, this.m_logging, path);
                handler.DirectoryClose += CloseHandler;
                this.CommandRecieved += handler.OnCommandRecieved;
            }
        }
        /// <summary>
        /// stop listen to handler and prevent handller to listen to server.
        /// </summary>
        /// <param name="sender"></param> DirectoryCloseEventArgs
        /// <param name="e"></param> handler.
        public void CloseHandler(object sender, DirectoryCloseEventArgs e)
        {
            ((IDirectoryHandler)sender).DirectoryClose -= CloseHandler;
            this.CommandRecieved -= ((IDirectoryHandler)sender).OnCommandRecieved;
        }
        /// <summary>
        /// send to all handlers command to close.
        /// </summary>
        /// <param name="args"></param>
        public void CloseAllHndlers (CommandRecievedEventArgs args)
        {
            this.CommandRecieved(this, args);
        }


    }
}
