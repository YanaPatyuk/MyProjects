using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    public class ConfigController : Controller
    {
        public static Settings settings =  Settings.Instance;
        // GET: Config
        [HttpGet]
        public ActionResult Config()
        {
            ViewBag.message = "you are here1!!!";
            settings.GetData();
            return View(settings);
        }

        // GET: Config
        [HttpGet]
        public ActionResult Delete(string path)
        {
            string path1 = path.Replace(@"\", @"\\");
            ViewBag.path = path1;
            Console.WriteLine(path1);
            Console.WriteLine(path);
            return View();
        }
        // GET: DeleteHandler
        [HttpGet]
        public string DeleteHandler(string path)
        {
            string tamp = path.Replace(@"\\", @"\");
            settings.RemoveHandler(tamp);
            return "handler deleted!";
        }
    }
}