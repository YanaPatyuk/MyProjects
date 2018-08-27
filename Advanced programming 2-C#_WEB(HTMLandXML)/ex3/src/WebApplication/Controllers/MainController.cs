using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Hosting;
using System.Web.Mvc;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    public class MainController : Controller
    {
        private Client tcpClient = Client.Instance;
        //create student property
         private static StudentsInfoList students = new StudentsInfoList();

        // GET: Main
        [HttpGet]
        public ActionResult ImageWeb()
        {
            //try to connect to server
            tcpClient.Connect();
            Settings.Instance.GetData();
            //get status of connection
            Boolean status = tcpClient.IsConnected();
            if (status) ViewBag.Status = "Connected";
            else ViewBag.Status = "dis-Connected";
            ViewBag.picNumber = PhotosGallery.Instance.PhotoList.Count();
            return View(students.Students);
        }


        // Get: Photos gallery page
        [HttpGet]
        public ActionResult Photos()
        {
            tcpClient.Connect(); // try to connect to server
            Settings.Instance.GetData(); // read settings data
            PhotosGallery gallery = PhotosGallery.Instance;
            gallery.CreatePhotosList(Settings.Instance.OutPutDur + '\\' + "Thumbnails"); // generate gallery
            return View(gallery.PhotoList);
        }

        // Remove pic from gallery using given path (sent from gallery view)
        [HttpGet]
        public string RemovePic(string path)
        {
            PhotosGallery gallery = PhotosGallery.Instance;
            return gallery.RemovePicFromComp(path);
        }

        /// <summary>
        /// Get photo details (sent from gallery view), and send the data to ViewFullPhoto screen
        /// </summary>
        /// <param name="name">photo name</param>
        /// <param name="timeTaken">photo time taken</param>
        /// <param name="path">photo thumbnail full path</param>
        /// <param name="relPath">photo thumbnail relative path</param>
        /// <returns></returns>
        public ActionResult ViewFullPhoto(string name, string timeTaken, string path, string relPath)
        {
            // cleanPath represents the original photo path cleaned from /Thumbnails
            string cleanPath = relPath.Replace("/Thumbnails", "");
            // send thumnail path, timeTaken, name and CleanPath as full resolution photo relative path
            return View(new PhotoInfo(path, timeTaken, name, cleanPath));
        }
    }
}