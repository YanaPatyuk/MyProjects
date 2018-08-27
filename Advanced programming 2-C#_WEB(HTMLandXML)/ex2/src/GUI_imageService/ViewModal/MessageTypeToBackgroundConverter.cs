using System;
using System.Windows.Data;
using System.Globalization;
using System.Windows.Media;
using Comunication.Event;
using Logging.Modal;

namespace Image_Service_GUI.ViewModel
{
    class MessageTypeToBackgroundConverter : IValueConverter
    {
        /// <summary>
        /// Convert MessageTypeEnum to matching background color to show with.
        /// </summary>
        /// <param name="value"></param>
        /// <param name="targetType"></param>
        /// <param name="parameter"></param>
        /// <param name="culture"></param>
        /// <returns></returns>
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (targetType != typeof(Brush))
            {
                throw new InvalidOperationException("Must convert to a brush!");
            }
            MessageTypeEnum msgType = (MessageTypeEnum)value;
            switch (msgType)
            {
                case MessageTypeEnum.WARNING:
                    return new SolidColorBrush(Colors.Yellow);
                case MessageTypeEnum.FAIL:
                    return new SolidColorBrush(Colors.LightCoral);
                case MessageTypeEnum.INFO:
                    return new SolidColorBrush(Colors.LightSeaGreen);
                default:
                    return new SolidColorBrush(Colors.White);
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
