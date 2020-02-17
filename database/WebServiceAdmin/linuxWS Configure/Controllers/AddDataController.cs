using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WhereToGoEntities.WhereToGo.Models;

namespace WhereToGo.Admin.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class AddDataController : Controller
    {
        [Route("Buildings")]
        [HttpPost]
        public IActionResult PostBuildings([FromForm] Buildings buildings, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                var ImageTemp = ImageRead;
                Buildings mdlBuildings = new Buildings();
                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = buildings.NameBuilding;
                ImageTemp.CopyTo(ms);
                mdlBuildings.ImageBuilding = ms.GetBuffer();
                whereToGo.Buildings.Add(mdlBuildings);
                whereToGo.SaveChanges();
                return Ok(new { status = true });

            }


        }
        [Route("{idBuilding}/Buildings")]
        [HttpPost]
        public IActionResult PostBuildingImages(int idBuilding, [FromForm] BuildingImages buildingImages, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                var ImageTemp = ImageRead;
                BuildingImages mdlBuildings = new BuildingImages();
                mdlBuildings.IdBuilding = idBuilding;
                mdlBuildings.BuildingLevel = buildingImages.BuildingLevel;
                ImageTemp.CopyTo(ms);
                mdlBuildings.PathImage = ms.GetBuffer();
                mdlBuildings.Scale = buildingImages.Scale;
                mdlBuildings.NorthPointAngle = buildingImages.NorthPointAngle;
                whereToGo.BuildingImages.Add(mdlBuildings);
                whereToGo.SaveChanges();

            }

            return Ok(new { status = true });

        }
        [Route("Groups")]
        [HttpPost]
        public IActionResult PostGroups([FromForm] Groups groups, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                var ImageTemp = ImageRead;
                Groups mdlGroups = new Groups();
                mdlGroups.NameGroup = groups.NameGroup;
                ImageTemp.CopyTo(ms);
                mdlGroups.ImageGroup = ms.GetBuffer();
                whereToGo.Groups.Add(mdlGroups);
                whereToGo.SaveChanges();

            }

            return Ok(new { status = true });
        }
        [Route("PointType")]
        [HttpPost]
        public IActionResult PostPointType([FromBody] PointType pointType)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointType mdlPointType = new PointType();
                mdlPointType.TypePoint = pointType.TypePoint;
                whereToGo.PointType.Add(mdlPointType);
                whereToGo.SaveChanges();
            return Ok(new { status = true });
        }
        [Route("BuildingsImage/{idBuildingImage}/Points")]
        [HttpPost]
        public IActionResult PostPointByBuildingImages(int idBuildingImage, [FromForm] Points points, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Points mdlPoints = new Points();
                mdlPoints.IdImage = idBuildingImage;
                mdlPoints.NamePoint = points.NamePoint;
                mdlPoints.X = points.X;
                mdlPoints.Y = points.Y;
                mdlPoints.IdPointType = points.IdPointType;
                mdlPoints.IdGroup = points.IdGroup;
                ImageRead.CopyTo(ms);
                mdlPoints.ImagePoint = ms.GetBuffer();
                whereToGo.Points.Add(mdlPoints);
                whereToGo.SaveChanges();

            }

            return Ok(new { status = true });

        }
        [Route("Points/{idPoint}/PointDetails")]
        [HttpPost]
        public IActionResult PostPoint(int idPoint, [FromBody] PointsDetail pointsDetail)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsDetail mdlPointsDetails = new PointsDetail();
                mdlPointsDetails.IdPoint = idPoint;
                mdlPointsDetails.NamePoint = pointsDetail.NamePoint;
                mdlPointsDetails.Detail1 = pointsDetail.Detail1;
                mdlPointsDetails.Detail2 = pointsDetail.Detail2;
                mdlPointsDetails.Detail3 = pointsDetail.Detail3;
                mdlPointsDetails.Detail4 = pointsDetail.Detail4;
                mdlPointsDetails.Detail5 = pointsDetail.Detail5;
                whereToGo.PointsDetail.Add(mdlPointsDetails);
                whereToGo.SaveChanges();

            }

            return Ok(new { status = true });

        }
    }
}