using Image_Service_GUI.ViewModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Image_Service_GUI.View
{
    /// <summary>
    /// Interaction logic for SettingsUserControl.xaml
    /// </summary>
    public partial class SettingsUserControl : UserControl, INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public void NotifyPropertyChanged(string propName)
        {
            if (this.PropertyChanged != null)
                this.PropertyChanged(this, new PropertyChangedEventArgs(propName));
        }

        private SettingsVM svm;
        public SettingsUserControl()
        {
            InitializeComponent();
            // Create SettingsVM to bind data from. import dirs from it.
            // and add method NotifyServerRemove to listen to each property (handler) change. 
            this.svm = new SettingsVM();
            PropertyChanged += this.svm.NotifyServerRemove;
            this.DataContext = this.svm;
            handlerList.ItemsSource = this.svm.HandlerDirsList;
        }

        private void btnRemoveDirClick(object sender, RoutedEventArgs e)
        {
            if (handlerList.SelectedItem != null)
            {
                this.NotifyPropertyChanged((handlerList.SelectedItem as HandlerDirectories).Path); // notify all the subscirbers about the removal
                // sends the server the removal command.
            }
        }

        private void handlerList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // revert the status of the Remove button (if it was disabled -> enable etc.)
            this.RemoveDirClick.IsEnabled = !(this.RemoveDirClick.IsEnabled);
        }
    }
}
