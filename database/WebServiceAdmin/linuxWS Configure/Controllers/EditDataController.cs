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
        [Route("Buildings/{idBuilding}/{NameBuilding}/{scale}")]
        public IActionResult PostBuildings(int  idBuilding, string NameBuilding, double scale, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Buildings mdlBuildings = new Buildings();
                mdlBuildings = whereToGo.Buildings.Where(i => i.IdBuilding == idBuilding).FirstOrDefault();

                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = NameBuilding;
                mdlBuildings.Scale = scale;
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
        [Route("Buildings/{idBuilding}/BuildingImages/{idImage}/{BuildingLevel}/{NorthPointAngle}")]
        public IActionResult PostBuildingImage(int idBuilding, int idImage, int BuildingLevel, double NorthPointAngle, [FromForm] IFormFile ImageRead)
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
        [HttpPost]
        [Route("OutdoorGame/{idOutdoorGame}/{idBuilding}/{nameGame}/{StartDateGame}/{EndDateGame}/{idFirstPoint}")]
        public IActionResult OutdoorGame(int idOutdoorGame, int idBuilding, string nameGame, DateTime StartDateGame, DateTime EndDateGame, int idFirstPoint, [FromForm] IFormFile ImageRead)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                OutdoorGame mdlOutdoorGame = new OutdoorGame();
                mdlOutdoorGame = whereToGo.OutdoorGame.Where(i => i.IdOutdoorGame == idOutdoorGame).FirstOrDefault();
                mdlOutdoorGame.IdBuilding = idBuilding;
                mdlOutdoorGame.NameGame = nameGame;
                mdlOutdoorGame.StartDateGame = StartDateGame;
                mdlOutdoorGame.EndDateGame = EndDateGame;
                if (idFirstPoint == -1)
                    mdlOutdoorGame.IdFirstPoint = null;
                else
                    mdlOutdoorGame.IdFirstPoint = idFirstPoint;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlOutdoorGame.ImageGame = ms.GetBuffer();
                }
                else
                    mdlOutdoorGame.ImageGame = mdlOutdoorGame.ImageGame;
                whereToGo.OutdoorGame.Update(mdlOutdoorGame);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idOutdoorGame);

            }
        }
        [HttpPost]
        [Route("OutdoorGamePath/{idQuestionPoint}/{idOutdoorGame}/{idPoint}/{Question}/{Answer}/{idNextPoint}/{idHintPoint}")]
        public IActionResult OutdoorGamePath(int idQuestionPoint, int idOutdoorGame, int idPoint, string Question, string Answer, int idNextpoint, int idHintPoint)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                OutdoorGamePath mdlOutdoorGamePath = new OutdoorGamePath();
                mdlOutdoorGamePath = whereToGo.OutdoorGamePath.Where(i => i.IdQuestionPoint == idQuestionPoint).FirstOrDefault();
                mdlOutdoorGamePath.IdOutdoorGame = idOutdoorGame;
                mdlOutdoorGamePath.IdPoint = idPoint;
                mdlOutdoorGamePath.Question = Question;
                mdlOutdoorGamePath.Answer = Answer;
                if (idNextpoint == -1)
                    mdlOutdoorGamePath.IdNextPoint = null;
                else
                    mdlOutdoorGamePath.IdNextPoint = idNextpoint;
                if (idHintPoint == -1)
                    mdlOutdoorGamePath.IdHintPoint = null;
                else
                    mdlOutdoorGamePath.IdHintPoint = idHintPoint;
                whereToGo.OutdoorGamePath.Update(mdlOutdoorGamePath);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idQuestionPoint);

            }
        }
        [HttpPost]
        [Route("OutdoorGameHints/{idHints}/{idOutdoorGame}/{idPoint}/{Hint}")]
        public IActionResult OutdoorGameHints(int idHints, int idOutdoorGame, int idPoint, string Hint)
        {
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                OutdoorGameHints mdlOutdoorGameHint = new OutdoorGameHints();
                mdlOutdoorGameHint = whereToGo.OutdoorGameHints.Where(i => i.IdHints == idHints).FirstOrDefault();
                mdlOutdoorGameHint.IdOutdoorGame = idOutdoorGame;
                mdlOutdoorGameHint.IdPoint = idPoint;
                mdlOutdoorGameHint.Hint = Hint;
                whereToGo.OutdoorGameHints.Update(mdlOutdoorGameHint);

                try
                {
                    whereToGo.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    return NotFound();
                }

                return Ok(idHints);

            }
        }
    }
}