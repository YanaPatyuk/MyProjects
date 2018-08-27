using ImageService.Modal;
using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Infrastructure;
using Infrastructure.Enums;
using Logging;
using Logging.Modal;
using System.Text.RegularExpressions;

namespace ImageService.Controller.Handlers
{
    public class DirectoyHandler : IDirectoryHandler
    {
        #region Members
        private IImageController m_controller;              // The Image Processing Controller
        private ILoggingService m_logging;
        private FileSystemWatcher m_dirWatcher;             // The Watcher of the Dir
        private string m_path;                              // The Path of directory
        #endregion

        public event EventHandler<DirectoryCloseEventArgs> DirectoryClose;              // The Event That Notifies that the Directory is being closed

        /// <summary>
        /// C'tor, handles src directory changes
        /// </summary>
        /// <param name="imageController">imageController</param>
        /// <param name="logger">Logging Service</param>
        public DirectoyHandler(IImageController imageController, ILoggingService logger, string dirPath)
        {
            this.m_controller = imageController;
            this.m_logging = logger;
            StartHandleDirectory(dirPath);
        }
        public string GetDirectory() { return this.m_path; }
        /// <summary>
        /// Recevies a src directory to track
        /// </summary>
        /// <param name="dirPath">directory path</param>
        public void StartHandleDirectory(string dirPath)
        {
            // initialize tracker
            this.m_path = dirPath;
            this.m_dirWatcher = new FileSystemWatcher(dirPath);
            // ***in case something goes wrong we might need to use new FileSystemEventHandler(DirChanged)***
            // add trackers to execute when directory changes
          //  this.m_dirWatcher.Changed += DirChanged;
            this.m_dirWatcher.Created += DirChanged;
            this.m_dirWatcher.EnableRaisingEvents = true;
        }

        /// <summary>
        /// Excute when directory changes
        /// </summary>
        /// <param name="sender">object who triggered the event</param>
        /// <param name="e">event args</param>
        private void DirChanged(object sender, FileSystemEventArgs e)
        {
            // configure args array, for now only one arg but may changed in future
            string[] args = new string[1];
            args[0] = e.FullPath;
            // make sure its legal image file aka *.jpg,*.png,*.gif,*.bmp
            string fileNameWithoutExtention = Path.GetFileNameWithoutExtension(e.FullPath);
            string fileExtention = Path.GetExtension(e.FullPath.ToLower());
            string fullFileName = fileNameWithoutExtention + fileExtention;
            string[] legalExtentionsArr = { ".jpg", ".png", ".gif", ".bmp" };
            // if fileExtention exist in legalExtentionsArr
            if (Array.IndexOf(legalExtentionsArr, fileExtention) >= 0)
            {
                // its an image file, preform the commands
                // m_path since we need the directory and not full image file path
                CommandRecievedEventArgs cmd = new CommandRecievedEventArgs((int)CommandEnum.NewFileCommand, args, m_path);
                this.OnCommandRecieved(this, cmd);
            }
            // else its not an image file - might be some other file, we don't do a thing.
        }

        /// <summary>
        /// Execute the command and inform the logger
        /// </summary>
        /// <param name="sender">object who sent the command</param>
        /// <param name="e">command args</param>
        public void OnCommandRecieved(object sender, CommandRecievedEventArgs e)
        {
            // if not close command 
            if (e.CommandID != (int)CommandEnum.CloseCommand)
            {
                bool resultSuccesful; // in order to send it to the command

                // if command directory and currnet dictonary are the same try to execute the command
                if (e.RequestDirPath.Equals(this.m_path))
                {
                    string message = m_controller.ExecuteCommand(e.CommandID, e.Args, out resultSuccesful);
                    // check command status in order to inform logger
                    if (resultSuccesful)
                    {
                        m_logging.Log("Command ID: " + e.CommandID + " executed successfully to:"+ message, MessageTypeEnum.INFO);
                    }
                    else
                    {
                        // command failed
                        m_logging.Log("Command ID: " + e.CommandID + " has failed, '" + message + "' args were: ", MessageTypeEnum.FAIL);
                        foreach (string arg in e.Args)
                        {
                            m_logging.Log(arg, MessageTypeEnum.FAIL);
                        }
                    }
                }
            }
            else
            {
                // close command
                m_logging.Log("Recevied Close Command for path:" + this.m_path, MessageTypeEnum.INFO);
                // finish handle the directory
                CloseDirectory();
                return;
            }
        }

        /// <summary>
        /// Close the Directory, stop the tracking of dirWatcher
        /// </summary>
        private void CloseDirectory()
        {
            // try to unsubscribe 
            try
            {
                // ***in case something goes wrong we might need to use new FileSystemEventHandler(DirChanged)***
                this.m_dirWatcher.Created -= DirChanged;
             //   this.m_dirWatcher.Changed -= DirChanged;
                this.m_dirWatcher.EnableRaisingEvents = false;
                //inform event logger of seccessfull close
                m_logging.Log("Directory " + this.m_path + "closed successfully", MessageTypeEnum.INFO);
                //m_logging.Log("close handler:" + this.m_path, MessageTypeEnum.INFO);
            }
            catch (IOException e)
            {
                m_logging.Log("Something went wrong while unsubscribing from watching directory: " + e.Message, MessageTypeEnum.FAIL);
            }
            // unsubscribed successfully. inform logger (DirectoryCloseEventArgs does that)
            DirectoryClose?.Invoke(this, new DirectoryCloseEventArgs(this.m_path, "directory " + this.m_path + "closed successfully"));
        }
    }
}
