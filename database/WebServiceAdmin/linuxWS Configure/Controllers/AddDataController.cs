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
                Buildings mdlBuildings = new Buildings();
                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = buildings.NameBuilding;
                ImageRead.CopyTo(ms);
                mdlBuildings.ImageBuilding = ms.GetBuffer();
                whereToGo.Buildings.Add(mdlBuildings);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }
                return Ok(mdlBuildings);

            }


        }
        [Route("BuildingImage/{idBuilding}")]
        [HttpPost]
        public IActionResult PostBuildingImages(int idBuilding, [FromForm] BuildingImages buildingImages, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                BuildingImages mdlBuildings = new BuildingImages();
                mdlBuildings.IdBuilding = idBuilding;
                mdlBuildings.BuildingLevel = buildingImages.BuildingLevel;
                ImageRead.CopyTo(ms);
                mdlBuildings.PathImage = ms.GetBuffer();
                mdlBuildings.Scale = buildingImages.Scale;
                mdlBuildings.NorthPointAngle = buildingImages.NorthPointAngle;
                whereToGo.BuildingImages.Add(mdlBuildings);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlBuildings);
            }


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
                Groups mdlGroups = new Groups();
                mdlGroups.NameGroup = groups.NameGroup;
                ImageRead.CopyTo(ms);
                mdlGroups.ImageGroup = ms.GetBuffer();
                mdlGroups.IdBuilding = groups.IdBuilding;
                whereToGo.Groups.Add(mdlGroups);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlGroups);

            }

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
            try
            {
                whereToGo.SaveChanges();
            }
            catch (Exception)
            {
                return BadRequest(999);
            }

            return Ok(mdlPointType);

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
                mdlPoints.X = points.X;
                mdlPoints.Y = points.Y;
                mdlPoints.IdPointType = points.IdPointType;
                ImageRead.CopyTo(ms);
                mdlPoints.ImagePoint = ms.GetBuffer();
                whereToGo.Points.Add(mdlPoints);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPoints);

            }


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
                mdlPointsDetails.IdGroup = pointsDetail.IdGroup;
                whereToGo.PointsDetail.Add(mdlPointsDetails);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPointsDetails);


            }

        }
        [Route("Points/PointsConnection")]
        [HttpPost]
        public IActionResult PostPointConnection([FromBody] PointsConnection pointsConnection)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsConnection mdlPointsConnection = new PointsConnection();
                mdlPointsConnection.IdPointStart = pointsConnection.IdPointStart;
                mdlPointsConnection.IdPointEnd = pointsConnection.IdPointEnd;
                whereToGo.PointsConnection.Add(mdlPointsConnection);
                try
                {
                    whereToGo.SaveChanges();
                }catch(Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPointsConnection);

            }

        }
    }
}