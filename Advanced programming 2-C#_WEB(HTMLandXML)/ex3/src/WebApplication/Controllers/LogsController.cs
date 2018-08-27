using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApplication.Models;

namespace WebApplication.Controllers
{
    public class LogsController : Controller
    {
        private static LogModel logs = new LogModel();
        // GET: Logs
     

        [HttpGet]
        public ActionResult Logs()
        {
            logs.CheckIfServerReconnected();
            return View(logs.LogList);
        }
    }
}