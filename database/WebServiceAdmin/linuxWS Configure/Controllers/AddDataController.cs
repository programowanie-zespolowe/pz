using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using WhereToGoEntities.WhereToGo.Models;

namespace WhereToGo.Admin.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class AddDataController : Controller
    {
        readonly ILogger<AddDataController> _log;
        public AddDataController(ILogger<AddDataController> log)
        {
            _log = log;
        }
        [Route("Buildings/{NameBuilding}/{scale}")]
        [HttpPost]
        public IActionResult PostBuildings(string NameBuilding, double scale, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Buildings mdlBuildings = new Buildings();
                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = NameBuilding;
                mdlBuildings.Scale = scale;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlBuildings.ImageBuilding = ms.GetBuffer();
                }
                else
                    mdlBuildings.ImageBuilding = null;
                whereToGo.Buildings.Add(mdlBuildings);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }
                return Ok(mdlBuildings.IdBuilding);

            }


        }
        [Route("BuildingImage/{idBuilding}/{BuildingLevel}/{NorthPointAngle}")]
        [HttpPost]
        public IActionResult PostBuildingImages(int idBuilding, int BuildingLevel, double NorthPointAngle, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                BuildingImages mdlBuildings = new BuildingImages();
                mdlBuildings.IdBuilding = idBuilding;
                mdlBuildings.BuildingLevel = BuildingLevel;
                if(ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlBuildings.PathImage = ms.GetBuffer();
                }
                else
                    mdlBuildings.PathImage = null;
                mdlBuildings.NorthPointAngle = NorthPointAngle;
                whereToGo.BuildingImages.Add(mdlBuildings);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlBuildings.IdImage);
            }
        }
        [Route("Groups/{NameGroup}/{IdBuilding}")]
        [HttpPost]
        public IActionResult PostGroups(string NameGroup, int IdBuilding, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Groups mdlGroups = new Groups();
                mdlGroups.NameGroup = NameGroup;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlGroups.ImageGroup = ms.GetBuffer();
                }
                else
                    mdlGroups.ImageGroup = null;
                mdlGroups.IdBuilding = IdBuilding;
                whereToGo.Groups.Add(mdlGroups);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlGroups.IdGroup);

            }

        }
        [Route("PointType/{TypePoint}")]
        [HttpPost]
        public IActionResult PostPointType(string TypePoint)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointType mdlPointType = new PointType();
                mdlPointType.TypePoint = TypePoint;
                whereToGo.PointType.Add(mdlPointType);
            try
            {
                whereToGo.SaveChanges();
            }
            catch (Exception)
            {
                return BadRequest(999);
            }

            return Ok(mdlPointType.IdPointType);

        }
        [Route("BuildingsImage/{idBuildingImage}/{X}/{Y}/{IdPointType}/{Direction}/{OnOffDirection}/Points")]
        [HttpPost]
        public IActionResult PostPointByBuildingImages(int idBuildingImage, double X, double Y, int IdPointType, double Direction, bool OnOffDirection)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Points mdlPoints = new Points();
                mdlPoints.IdImage = idBuildingImage;
                mdlPoints.X = X;
                mdlPoints.Y = Y;
                mdlPoints.IdPointType = IdPointType;
                mdlPoints.ImagePoint = null;
                mdlPoints.Direction = Direction;
                mdlPoints.OnOffDirection = OnOffDirection;
                whereToGo.Points.Add(mdlPoints);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPoints.IdPoint);

            }


        }
        [Route("Points/{idPoint}/{NamePoint}/{IdGroup}/PointDetails")]
        [HttpPost]
        public IActionResult PostPoint(int idPoint, string NamePoint, int IdGroup, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsDetail mdlPointsDetails = new PointsDetail();
                mdlPointsDetails.IdPoint = idPoint;
                mdlPointsDetails.NamePoint = NamePoint;
                mdlPointsDetails.IdGroup = IdGroup;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlPointsDetails.ImagePoint = ms.GetBuffer();
                }
                else
                    mdlPointsDetails.ImagePoint = null;
                whereToGo.PointsDetail.Add(mdlPointsDetails);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPointsDetails.IdPointDetails);


            }

        }
        [Route("Points/{IdPointStart}/{IdPointEnd}/PointsConnection")]
        [HttpPost]
        public IActionResult PostPointConnection(int IdPointStart, int IdPointEnd)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsConnection mdlPointsConnection = new PointsConnection();
                mdlPointsConnection.IdPointStart = IdPointStart;
                mdlPointsConnection.IdPointEnd = IdPointEnd;
                whereToGo.PointsConnection.Add(mdlPointsConnection);
                try
                {
                    whereToGo.SaveChanges();
                }catch(Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPointsConnection.IdPointConnection);

            }

        }
    }
}