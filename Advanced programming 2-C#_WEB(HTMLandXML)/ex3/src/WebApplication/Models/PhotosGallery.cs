using System;
using System.Collections.Generic;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using System.Text.RegularExpressions;
using System.Text;


namespace WebApplication.Models
{
    public class PhotosGallery
    {
        private List<PhotoInfo> photosList = new List<PhotoInfo>();
        private Settings settings;
        private static Regex r = new Regex(":");
        private static PhotosGallery instance = null;

        public List<PhotoInfo> PhotoList { get { return photosList; } }
        // Singelton
        public static PhotosGallery Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new PhotosGallery();
                }
                return instance;
            }
        }

        private PhotosGallery()
        {
            this.settings = Settings.Instance;
            string outputFolder = this.settings.OutPutDur;
            // create photo list using actuall photos
            CreatePhotosList(outputFolder + '\\' + "Thumbnails");
        }


        /// <summary>
        /// Create photo list from photos in given folder
        /// </summary>
        /// <param name="searchFolder">given folder</param>
        public void CreatePhotosList(String searchFolder)
        {
            if (!Client.Instance.IsConnected()) return;
            Directory.CreateDirectory(searchFolder); // first make sure there's such directory

            // serch for image files ending with..
            var filters = new String[] { "jpg", "jpeg", "png", "gif", "tiff", "bmp" };
            // search recursively in subfolders
            var files = GetFilesFrom(searchFolder, filters, true);
            foreach (String filePath in files)
            {
                // file exist, we need to find out photo "taken time" or at least creation time (which anyfile should have)
                DateTime date = new DateTime();
                string fileNameWithoutExtention = Path.GetFileNameWithoutExtension(filePath);
                string fileExtention = Path.GetExtension(filePath);
                string fullFileName = fileNameWithoutExtention + fileExtention;

                // remove the "\\" + "Thumbnails" but keep the rest path
                // try to retrieve taken time from actual photo [not Thumnail]
                string originalPhotoPath = filePath.Replace(@"\\Thumbnails", "");
                try
                {
                    date = GetDateTakenFromImage(originalPhotoPath); 
                }
                catch
                {
                    try
                    {
                        date = File.GetCreationTime(originalPhotoPath); // try to retrieve created time
                    }
                    catch (IOException e)
                    {
                        Console.WriteLine("Couldn't retrieve photo date: {0}", e.Message);
                        throw;
                    }
                }

                // add photo to photos list-check first if not already exist
                PhotoInfo newPhoto = null;
                foreach (PhotoInfo photo in this.photosList)
                {
                    if (photo.Path.Equals(filePath))
                    {
                        newPhoto = photo;
                        break;
                    }
                }
                if (newPhoto == null)
                    this.photosList.Add(new PhotoInfo(filePath, date.ToLongDateString(), fileNameWithoutExtention));
            }
        }

        /// <summary>
        /// Searches all .extension files in main only / main and sub directories @stackoverflow
        /// </summary>
        /// <param name="searchFolder">given main folder</param>
        /// <param name="filters">array which contains.extnsion names, eg: "jpg" "png" etc.</param>
        /// <param name="searchSubFolders">search also in subfolders or not</param>
        /// <returns>list with all paths to .extension files</returns>
        public String[] GetFilesFrom(String searchFolder, String[] filters, bool searchSubFolders)
        {
            List<string> filesFound = new List<String>();
            var searchOption = searchSubFolders ? SearchOption.AllDirectories : SearchOption.TopDirectoryOnly;
            foreach (var filter in filters)
            {
                filesFound.AddRange(Directory.GetFiles(searchFolder, String.Format("*.{0}", filter), searchOption));
            }
            return filesFound.ToArray();
        }

        /// <summary>
        /// retrieves the taken time of image WITHOUT loading the whole image (way faster) @ stackoverflow
        /// </summary>
        public static DateTime GetDateTakenFromImage(string path)
        {
            using (FileStream fs = new FileStream(path, FileMode.Open, FileAccess.Read))
            using (Image myImage = Image.FromStream(fs, false, false))
            {
                PropertyItem propItem = myImage.GetPropertyItem(36867);
                string dateTaken = r.Replace(Encoding.UTF8.GetString(propItem.Value), "-", 2);
                return DateTime.Parse(dateTaken);
            }
        }

        /// <summary>
        /// Remove pic stored at given path
        /// </summary>
        /// <param name="path">path to pic to remove</param>
        /// <returns></returns>
        public string RemovePicFromComp(string path)
        {
            // iterate on the list and remove the photo with given path 
            // (note you cant edit + iterate on loop the same time)
            PhotoInfo photo = null;
            string temp = null;
            foreach (PhotoInfo p in photosList)
            {
                // convert photo p [in photoList] relative path to same represtation of given path to remove
                temp = p.RelativePath.Replace(@"\\", @"\");
                temp = temp.Replace(@"\", "/");
                temp = temp.Replace("~", "");

                if (path == temp) // if found photo p with the same path to photo to remove save reference to p
                {
                    photo = p;
                    break;
                }
            }

            // remove the photo [p] from the list
            if (photo != null)
            {
                photosList.Remove(photo);
                //return photo.Path;
                //return "im here?";
            }

            // remove thumbnail file from computer
            if (File.Exists(photo.Path))
            {
                File.Delete(photo.Path);
            }

            // remove the "\\" + "Thumbnails" but keep the rest path
            string cleanPath = photo.Path.Replace(@"\\Thumbnails", "");
            // now remove actual image [not Thumbnail]
            if (File.Exists(cleanPath))
            {
                File.Delete(cleanPath);
                return "successfully deleted files";
            }
            return "problem";
        }

    }


}