using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WhereToGoEntities.WhereToGo.Models;

namespace WhereToGo.Admin.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class EditDataController : Controller
    {
        [HttpPost]
        [Route("Buildings/{idBuilding}/{NameBuilding}")]
        public IActionResult PostBuildings(int  idBuilding, string NameBuilding, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Buildings mdlBuildings = new Buildings();
                mdlBuildings = whereToGo.Buildings.Where(i => i.IdBuilding == idBuilding).FirstOrDefault();

                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = NameBuilding;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlBuildings.ImageBuilding = ms.GetBuffer();
                }
                else
                    mdlBuildings.ImageBuilding = mdlBuildings.ImageBuilding;
                whereToGo.Buildings.Update(mdlBuildings);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idBuilding);

            }
        }
        [HttpPost]
        [Route("Buildings/{idBuilding}/BuildingImages/{idImage}/{BuildingLevel}/{Scale}/{NorthPointAngle}")]
        public IActionResult PostBuildingImage(int idBuilding, int idImage, int BuildingLevel, double Scale, double NorthPointAngle, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                BuildingImages mdlBuildings = new BuildingImages();
                mdlBuildings = whereToGo.BuildingImages.Where(i => i.IdImage == idImage && i.IdBuilding == idBuilding).FirstOrDefault();

                mdlBuildings.IdBuilding = idBuilding;
                mdlBuildings.BuildingLevel = BuildingLevel;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlBuildings.PathImage = ms.GetBuffer();
                }
                else
                    mdlBuildings.PathImage = mdlBuildings.PathImage;
                mdlBuildings.Scale = Scale;
                mdlBuildings.NorthPointAngle = NorthPointAngle;
                whereToGo.BuildingImages.Update(mdlBuildings);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idImage);

            }
        }
        [HttpPost]
        [Route("Groups/{idGroup}/{NameGroup}/{IdBuilding}")]
        public IActionResult PostGroup(int idGroup, string NameGroup, int IdBuilding, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Groups mdlGroups = new Groups();
                mdlGroups = whereToGo.Groups.Where(i => i.IdGroup == idGroup).FirstOrDefault();
                mdlGroups.NameGroup = NameGroup;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlGroups.ImageGroup = ms.GetBuffer();
                }
                else
                    mdlGroups.ImageGroup = mdlGroups.ImageGroup;
                mdlGroups.IdBuilding = IdBuilding;
                whereToGo.Groups.Update(mdlGroups);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idGroup);

            }
        }
        [HttpPost]
        [Route("PointType/{idPointType}/{TypePoint}")]
        public IActionResult PostPointType(int idPointType, string TypePoint)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointType mdlPointType = new PointType();
                mdlPointType = whereToGo.PointType.Where(i => i.IdPointType == idPointType).FirstOrDefault();
                mdlPointType.TypePoint = TypePoint;
                whereToGo.PointType.Update(mdlPointType);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idPointType);

            }
        }
        [HttpPost]
        [Route("Buildings/{idBuilding}/BuildingImages/{idImage}/Points/{idPoint}/{X}/{Y}/{IdPointType}/{Direction}/{OnOffDirection}")]
        public IActionResult PostPoint(int idBuilding, int idImage, int idPoint, double X, double Y, int IdPointType, double Direction, bool OnOffDirection, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Points mdlPoints = new Points();
                mdlPoints = whereToGo.Points.Where(i => i.IdPoint == idPoint && i.IdImage == idImage).FirstOrDefault();
                mdlPoints.IdImage = idImage;
                mdlPoints.X = X;
                mdlPoints.Y = Y;
                mdlPoints.IdPointType = IdPointType;
                mdlPoints.Direction = Direction;
                mdlPoints.OnOffDirection = OnOffDirection;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlPoints.ImagePoint = ms.GetBuffer();
                }
                else
                    mdlPoints.ImagePoint = mdlPoints.ImagePoint;
                whereToGo.Points.Update(mdlPoints);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idPoint);

            }
        }
        [HttpPost]
        [Route("Points/{idPoint}/PointsDetail/{idPointDetail}/{NamePoint}/{IdGroup}")]
        public IActionResult PostPointDetail(int idPoint, int idPointDetail, string NamePoint, int IdGroup, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsDetail mdlPointDetails = new PointsDetail();
                mdlPointDetails = whereToGo.PointsDetail.Where(i => i.IdPointDetails == idPointDetail && i.IdPoint == idPoint).FirstOrDefault();
                mdlPointDetails.IdPoint = idPoint;
                mdlPointDetails.NamePoint = NamePoint;
                mdlPointDetails.IdGroup = IdGroup;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlPointDetails.ImagePoint = ms.GetBuffer();
                }
                else
                    mdlPointDetails.ImagePoint = mdlPointDetails.ImagePoint;
                whereToGo.PointsDetail.Update(mdlPointDetails);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idPointDetail);

            }
        }
    }
}