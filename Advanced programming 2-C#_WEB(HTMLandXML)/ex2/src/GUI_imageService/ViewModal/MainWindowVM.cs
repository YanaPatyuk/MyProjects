using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Comunication.Client;
using System.Windows.Media;

namespace Image_Service_GUI.ViewModel
{
    class MainWindowVM
    {
        // MainWindow backgroundColor to bind
        private Brush backgroundColor;
        public Brush VM_BackgroundColor
        {
            get { return backgroundColor; }
        }

        public MainWindowVM()
        {
            // try to connect to server
            // If connected to ImageService return white (background) otherwise return grey
            GuiClient.Instance.Connect();
            if (GuiClient.Instance.ConnectedToServer)
            {
                this.backgroundColor = new SolidColorBrush(Colors.White);
            }
            else
            {
                this.backgroundColor = new SolidColorBrush(Colors.Gray);
            }
        }
    }
}
