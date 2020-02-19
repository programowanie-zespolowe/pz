﻿using System;
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
        [Route("Buildings/{idBuilding}")]
        public IActionResult PostBuildings(int idBuilding, [FromForm] Buildings buildings, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Buildings mdlBuildings = new Buildings();
                mdlBuildings = whereToGo.Buildings.Where(i => i.IdBuilding == idBuilding).FirstOrDefault();

                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = buildings.NameBuilding;
                ImageRead.CopyTo(ms);
                mdlBuildings.ImageBuilding = ms.GetBuffer();
                whereToGo.Buildings.Update(mdlBuildings);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return NoContent();

            }
        }
        [HttpPost]
        [Route("Buildings/{idBuilding}/BuildingImages/{idImage}")]
        public IActionResult PostBuildingImage(int idBuilding, int idImage, [FromForm] BuildingImages buildingImages, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                BuildingImages mdlBuildings = new BuildingImages();
                mdlBuildings = whereToGo.BuildingImages.Where(i => i.IdImage == idImage && i.IdBuilding == idBuilding).FirstOrDefault();

                mdlBuildings.IdBuilding = idBuilding;
                mdlBuildings.BuildingLevel = buildingImages.BuildingLevel;
                ImageRead.CopyTo(ms);
                mdlBuildings.PathImage = ms.GetBuffer();
                mdlBuildings.Scale = buildingImages.Scale;
                mdlBuildings.NorthPointAngle = buildingImages.NorthPointAngle;
                whereToGo.BuildingImages.Update(mdlBuildings);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return NoContent();

            }
        }
        [HttpPost]
        [Route("Groups/{idGroup}")]
        public IActionResult PostGroup(int idGroup, [FromForm] Groups groups, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Groups mdlGroups = new Groups();
                mdlGroups = whereToGo.Groups.Where(i => i.IdGroup == idGroup).FirstOrDefault();
                mdlGroups.NameGroup = groups.NameGroup;
                ImageRead.CopyTo(ms);
                mdlGroups.ImageGroup = ms.GetBuffer();
                mdlGroups.IdBuilding = groups.IdBuilding;
                whereToGo.Groups.Update(mdlGroups);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return NoContent();

            }
        }
        [HttpPost]
        [Route("PointType/{idPointType}")]
        public IActionResult PostPointType(int idPointType, [FromBody] PointType pointType)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointType mdlPointType = new PointType();
                mdlPointType = whereToGo.PointType.Where(i => i.IdPointType == idPointType).FirstOrDefault();
                mdlPointType.TypePoint = pointType.TypePoint;
                whereToGo.PointType.Update(mdlPointType);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return NoContent();

            }
        }
        [HttpPost]
        [Route("Buildings/{idBuilding}/BuildingImages/{idImage}/Points/{idPoint}")]
        public IActionResult PostPoint(int idBuilding, int idImage, int idPoint, [FromForm] Points points, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Points mdlPoints = new Points();
                mdlPoints = whereToGo.Points.Where(i => i.IdPoint == idPoint && i.IdImage == idImage).FirstOrDefault();
                mdlPoints.IdImage = idImage;
                mdlPoints.X = points.X;
                mdlPoints.Y = points.Y;
                mdlPoints.IdPointType = points.IdPointType;
                ImageRead.CopyTo(ms);
                mdlPoints.ImagePoint = ms.GetBuffer();
                whereToGo.Points.Update(mdlPoints);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return NoContent();

            }
        }
        [HttpPost]
        [Route("Points/{idPoint}/PointsDetail/{idPointDetail}")]
        public IActionResult PostPointDetail(int idPoint, int idPointDetail, [FromBody] PointsDetail pointsDetail)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsDetail mdlPointDetails = new PointsDetail();
                mdlPointDetails = whereToGo.PointsDetail.Where(i => i.IdPointDetails == idPointDetail && i.IdPoint == idPoint).FirstOrDefault();
                mdlPointDetails.IdPoint = idPoint;
                mdlPointDetails.NamePoint = pointsDetail.NamePoint;
                mdlPointDetails.IdGroup = pointsDetail.IdGroup;
                whereToGo.PointsDetail.Update(mdlPointDetails);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return NoContent();

            }
        }
    }
}