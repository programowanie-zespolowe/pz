﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using linuxWS.Models;
using LinuxWS.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using WhereToGoEntities.WhereToGo.Models;
using static LinuxWS.Models.FindPath;

namespace linuxWS.Controllers
{
    [Authorize]
    [ApiController]
    [Route("[controller]")]
    public class GetDataController : ControllerBase
    {
        [HttpGet]
        [Route("Buildings")]
        public IEnumerable<MdlBuildings> Get()
        {
            List<MdlBuildings> buildingImages = new List<MdlBuildings>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Buildings)
                {
                    MdlBuildings mdlBuildings = new MdlBuildings();
                    mdlBuildings.IdBuilding = element.IdBuilding;
                    mdlBuildings.IdUser = element.IdUser;
                    mdlBuildings.NameBuilding = element.NameBuilding;
                    mdlBuildings.ImageBuilding = element.ImageBuilding;
                    mdlBuildings.Scale = element.Scale;
                    buildingImages.Add(mdlBuildings);
                }
            }
            return buildingImages.ToList();
        }
        [HttpGet]
        [Route("Buildings/Selected/{idBuilding}")]
        public IEnumerable<MdlBuildings> GetAllBuilding(int idBuilding)
        {
            List<MdlBuildings> buildingImages = new List<MdlBuildings>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Buildings.Where(i => i.IdBuilding == idBuilding))
                {
                    MdlBuildings mdlBuildings = new MdlBuildings();
                    mdlBuildings.IdBuilding = element.IdBuilding;
                    mdlBuildings.IdUser = element.IdUser;
                    mdlBuildings.NameBuilding = element.NameBuilding;
                    mdlBuildings.ImageBuilding = element.ImageBuilding;
                    mdlBuildings.Scale = element.Scale;
                    buildingImages.Add(mdlBuildings);
                }
            }
            return buildingImages.ToList();
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}")]
        public IEnumerable<MdlBuildingImages> GetBuildings(int idBuilding)
        {
            List<MdlBuildingImages> buildingImages = new List<MdlBuildingImages>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.BuildingImages.Where(i => i.IdBuilding == idBuilding))
                {
                    MdlBuildingImages mdlBuildingImages = new MdlBuildingImages();
                    mdlBuildingImages.IdBuilding = element.IdBuilding;
                    mdlBuildingImages.IdImage = element.IdImage;
                    mdlBuildingImages.PathImage = element.PathImage;
                    mdlBuildingImages.BuildingLevel = element.BuildingLevel;
                    mdlBuildingImages.NorthPointAngle = element.NorthPointAngle;
                    buildingImages.Add(mdlBuildingImages);
                }
                return buildingImages.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}/Points")]
        public IEnumerable<MdlPoints> GetPointByBuilding(int idBuilding)
        {
            List<MdlPoints> buildingPoints = new List<MdlPoints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.BuildingImages.Where(j => j.IdBuilding == idBuilding))
                {
                    foreach (var elementIn in whereToGoEntities.Points.Where(i => i.IdImage == element.IdImage))
                    {
                        MdlPoints mdlPoints = new MdlPoints();
                        mdlPoints.IdPoint = elementIn.IdPoint;
                        mdlPoints.IdImage = elementIn.IdImage;
                        mdlPoints.X = elementIn.X;
                        mdlPoints.Y = elementIn.Y;
                        mdlPoints.IdPointType = elementIn.IdPointType;
                        mdlPoints.ImagePoint = elementIn.ImagePoint;

                        buildingPoints.Add(mdlPoints);
                    }
                }
                return buildingPoints.ToList();
            }
        }
        [HttpGet]
        [Route("BuildingsImage/{idImage}/Points")]
        public IEnumerable<MdlPoints> GetPointByImage(int idImage)
        {
            List<MdlPoints> buildingPoints = new List<MdlPoints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Points.Where(i => i.IdImage == idImage))
                {
                    MdlPoints mdlPoints = new MdlPoints();
                    mdlPoints.IdPoint = element.IdPoint;
                    mdlPoints.IdImage = element.IdImage;
                    mdlPoints.X = element.X;
                    mdlPoints.Y = element.Y;
                    mdlPoints.IdPointType = element.IdPointType;
                    mdlPoints.ImagePoint = element.ImagePoint;

                    buildingPoints.Add(mdlPoints);
                }
                return buildingPoints.ToList();
            }
        }
        [HttpGet]
        [Route("BuildingsImage/{idImage}/Point/{idPoint}")]
        public IEnumerable<MdlPoints> GetPoint(int idImage, int idPoint)
        {
            List<MdlPoints> buildingPoints = new List<MdlPoints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var elementIn in whereToGoEntities.Points.Where(i => i.IdPoint == idPoint && i.IdImage == idImage))
                {
                    MdlPoints mdlPoints = new MdlPoints();
                    mdlPoints.IdPoint = elementIn.IdPoint;
                    mdlPoints.IdImage = elementIn.IdImage;
                    mdlPoints.X = elementIn.X;
                    mdlPoints.Y = elementIn.Y;
                    mdlPoints.IdPointType = elementIn.IdPointType;
                    mdlPoints.ImagePoint = elementIn.ImagePoint;

                    buildingPoints.Add(mdlPoints);
                }
                return buildingPoints.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Groups")]
        public IEnumerable<MdlGroups> GetGroups()
        {
            List<MdlGroups> listGroups = new List<MdlGroups>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Groups)
                {
                    MdlGroups mdlGroups = new MdlGroups();
                    mdlGroups.IdGroup = element.IdGroup;
                    mdlGroups.NameGroup = element.NameGroup;
                    mdlGroups.ImageGroup = element.ImageGroup;
                    mdlGroups.IdBuilding = element.IdBuilding;

                    listGroups.Add(mdlGroups);
                }
                return listGroups.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Groups/{idGroup}")]
        public IEnumerable<MdlGroups> GetGroup(int idGroup)
        {
            List<MdlGroups> listGroups = new List<MdlGroups>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Groups.Where(i => i.IdGroup == idGroup))
                {
                    MdlGroups mdlGroups = new MdlGroups();
                    mdlGroups.IdGroup = element.IdGroup;
                    mdlGroups.NameGroup = element.NameGroup;
                    mdlGroups.ImageGroup = element.ImageGroup;
                    mdlGroups.IdBuilding = element.IdBuilding;

                    listGroups.Add(mdlGroups);
                }
                return listGroups.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}/Groups")]
        public IEnumerable<MdlGroups> GetGroupByBuilding(int idBuilding)
        {
            List<MdlGroups> listGroups = new List<MdlGroups>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Groups.Where(i => i.IdBuilding == idBuilding))
                {
                    MdlGroups mdlGroups = new MdlGroups();
                    mdlGroups.IdGroup = element.IdGroup;
                    mdlGroups.NameGroup = element.NameGroup;
                    mdlGroups.ImageGroup = element.ImageGroup;
                    mdlGroups.IdBuilding = element.IdBuilding;

                    listGroups.Add(mdlGroups);
                }
                return listGroups.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}/PointsDetails")]
        public IEnumerable<MdlPointDetails> GetPointsDetails(int idBuilding)
        {
            List<MdlPointDetails> listPointsDetails = new List<MdlPointDetails>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.PointsDetail.Where(i => i.IdPointNavigation.IdImageNavigation.IdBuilding == idBuilding))
                {
                    MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.IdGroup = element.IdGroup;

                    listPointsDetails.Add(mdlPointsDetails);
                }
                return listPointsDetails.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Points/{idPoint}/PointsDetails")]
        public IEnumerable<MdlPointDetails> GetPointsDetailsByPoint(int idPoint)
        {
            List<MdlPointDetails> listPointsDetails = new List<MdlPointDetails>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.PointsDetail.Where(i => i.IdPoint == idPoint))
                {
                    MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.IdGroup = element.IdGroup;
                    mdlPointsDetails.ImagePoint = element.ImagePoint;

                    listPointsDetails.Add(mdlPointsDetails);
                }
                return listPointsDetails.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Groups/{idGroup}/PointsDetails")]
        public IEnumerable<MdlPointDetails> GetPointsDetailsByGroup(int idGroup)
        {
            List<MdlPointDetails> listPointsDetails = new List<MdlPointDetails>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.PointsDetail.Where(i => i.IdGroup == idGroup))
                {
                    MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.IdGroup = element.IdGroup;
                    mdlPointsDetails.ImagePoint = element.ImagePoint;

                    listPointsDetails.Add(mdlPointsDetails);
                }
                return listPointsDetails.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/PointsDetails/{idPointDetails}")]
        public IEnumerable<MdlPointDetails> GetPointDetails(int idPointDetails)
        {
            List<MdlPointDetails> listPointsDetails = new List<MdlPointDetails>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.PointsDetail.Where(i => i.IdPointDetails == idPointDetails))
                {
                    MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.IdGroup = element.IdGroup;
                    mdlPointsDetails.ImagePoint = element.ImagePoint;

                    listPointsDetails.Add(mdlPointsDetails);
                }
                return listPointsDetails.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/PointsConnection")]
        public IEnumerable<MdlPointsConnection> GetPointConnection()
        {
            List<MdlPointsConnection> listPointsConnection = new List<MdlPointsConnection>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.PointsConnection)
                {
                    MdlPointsConnection mdlPointsConnection = new MdlPointsConnection();
                    mdlPointsConnection.IdPointStart = element.IdPointStart;
                    mdlPointsConnection.IdPointEnd = element.IdPointEnd;

                    listPointsConnection.Add(mdlPointsConnection);
                }
                return listPointsConnection.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}/{idPrevPoint}/{idActualPoint}/{idDestPoint}")]
        public NextPoint GetRoutePoints(int idBuilding, int idPrevPoint, int idActualPoint, int idDestPoint)
        {
            List<MdlPoints> mdlPointsList = new List<MdlPoints>();
            List<MdlPointsConnection> mdlPointsConnectionsList = new List<MdlPointsConnection>();
            using WhereToGoContext whereToGoEntities = new WhereToGoContext();

            foreach (var item in whereToGoEntities.Points.Where(i => i.IdImageNavigation.IdBuilding == idBuilding))
            {
                MdlPoints mdlPoint = new MdlPoints();
                mdlPoint.IdPoint = item.IdPoint;
                mdlPoint.IdImage = item.IdImage;
                mdlPoint.X = item.X;
                mdlPoint.Y = item.Y;
                mdlPoint.IdPointType = item.IdPointType;
                mdlPoint.ImagePoint = item.ImagePoint;
                mdlPoint.Direction = item.Direction;
                mdlPoint.OnOffDirection = item.OnOffDirection;

                foreach (var element in whereToGoEntities.PointsConnection.Where(i => i.IdPointStart == item.IdPoint || i.IdPointEnd == item.IdPoint))
                {
                    MdlPointsConnection mdlPointsConnection = new MdlPointsConnection();
                    mdlPointsConnection.IdPointConnection = element.IdPointConnection;
                    mdlPointsConnection.IdPointStart = element.IdPointStart;
                    mdlPointsConnection.IdPointEnd = element.IdPointEnd;

                    var connections =
                        mdlPointsConnectionsList.Where(i => i.IdPointConnection == element.IdPointConnection);
                    if (!connections.Any())
                    {
                        mdlPointsConnectionsList.Add(mdlPointsConnection);
                    }
                }

                mdlPointsList.Add(mdlPoint);
            }

            var idLevel = (int)whereToGoEntities.Points.FirstOrDefault(i => i.IdPoint == idDestPoint).IdImage;
            var scale = (double)whereToGoEntities.Buildings.FirstOrDefault(i => i.IdBuilding == idBuilding).Scale;
            var destPointLevel = (int)whereToGoEntities.BuildingImages.FirstOrDefault(i => i.IdImage == idLevel).BuildingLevel;

            var idCurrentLevel = (int)whereToGoEntities.Points.FirstOrDefault(i => i.IdPoint == idActualPoint).IdImage;
            var currentLevel = (int)whereToGoEntities.BuildingImages.FirstOrDefault(i => i.IdImage == idCurrentLevel).BuildingLevel;

            return FindPath.GetNextPoint(idPrevPoint == -1 ? null : (int?)idPrevPoint,
                idActualPoint,
                idDestPoint,
                mdlPointsList,
                mdlPointsConnectionsList,
                scale,
                destPointLevel,
                currentLevel);
        }
        [HttpGet]
        [Route("Buildings/OutdoorGame/{idBuilding}")]
        public IEnumerable<MdlOutdoorGame> GetAllOutdoorGame(int idBuilding)
        {
            List<MdlOutdoorGame> listOutdoorGame = new List<MdlOutdoorGame>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.OutdoorGame.Where(i => i.IdBuilding == idBuilding && i.EndDateGame >= DateTime.Now))
                {
                    MdlOutdoorGame mdlOutdoorGame = new MdlOutdoorGame();
                    mdlOutdoorGame.IdOutdoorGame = element.IdOutdoorGame;
                    mdlOutdoorGame.IdBuilding = element.IdBuilding;
                    mdlOutdoorGame.NameGame = element.NameGame;
                    mdlOutdoorGame.ImageGame = element.ImageGame;
                    mdlOutdoorGame.StartDateGame = element.StartDateGame;
                    mdlOutdoorGame.EndDateGame = element.EndDateGame;
                    mdlOutdoorGame.IdFirstPoint = element.IdFirstPoint;

                    listOutdoorGame.Add(mdlOutdoorGame);
                }
                return listOutdoorGame.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/OutdoorGame/Game/{idOutdoorGame}/{idNextPoint}")]
        public IEnumerable<MdlOutdoorGamePath> GetOutdoorGame(int idOutdoorGame, int idNextPoint)
        {
            List<MdlOutdoorGamePath> listOutdoorGamePath = new List<MdlOutdoorGamePath>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                if(idNextPoint == -1)
                {
                    foreach (var element in whereToGoEntities.OutdoorGame.Where(i => i.IdOutdoorGame == idOutdoorGame))
                    {
                        MdlOutdoorGame mdlOutdoorGame = new MdlOutdoorGame();
                        mdlOutdoorGame.IdOutdoorGame = element.IdOutdoorGame;
                        mdlOutdoorGame.IdFirstPoint = element.IdFirstPoint;
                        foreach (var item in whereToGoEntities.OutdoorGamePath.Where(i => i.IdOutdoorGame == mdlOutdoorGame.IdOutdoorGame && i.IdQuestionPoint == mdlOutdoorGame.IdFirstPoint))
                        {
                            MdlOutdoorGamePath mdlOutdoorGamePath = new MdlOutdoorGamePath();
                            mdlOutdoorGamePath.IdQuestionPoint = item.IdQuestionPoint;
                            mdlOutdoorGamePath.IdOutdoorGame = item.IdOutdoorGame;
                            mdlOutdoorGamePath.IdPoint = item.IdPoint;
                            mdlOutdoorGamePath.Question = item.Question;
                            mdlOutdoorGamePath.Answer = item.Answer;
                            mdlOutdoorGamePath.IdNextPoint = item.IdNextPoint;
                            mdlOutdoorGamePath.IdHintPoint = item.IdHintPoint;
                            listOutdoorGamePath.Add(mdlOutdoorGamePath);
                        }
                    }
                    return listOutdoorGamePath.ToList();
                }
                else
                {
                    foreach (var item in whereToGoEntities.OutdoorGamePath.Where(i => i.IdOutdoorGame == idOutdoorGame && i.IdQuestionPoint == idNextPoint))
                    {
                        MdlOutdoorGamePath mdlOutdoorGamePath = new MdlOutdoorGamePath();
                        mdlOutdoorGamePath.IdQuestionPoint = item.IdQuestionPoint;
                        mdlOutdoorGamePath.IdOutdoorGame = item.IdOutdoorGame;
                        mdlOutdoorGamePath.IdPoint = item.IdPoint;
                        mdlOutdoorGamePath.Question = item.Question;
                        mdlOutdoorGamePath.Answer = item.Answer;
                        mdlOutdoorGamePath.IdNextPoint = item.IdNextPoint;
                        mdlOutdoorGamePath.IdHintPoint = item.IdHintPoint;
                        listOutdoorGamePath.Add(mdlOutdoorGamePath);
                    }
                    return listOutdoorGamePath.ToList();
                }
            }
        }
        [HttpGet]
        [Route("Buildings/OutdoorGame/Hint/{idOutdoorGame}/{idHintPoint}")]
        public IEnumerable<MdlOutdoorGameHints> GetOutdoorGameHint(int idOutdoorGame, int idHintPoint)
        {
            List<MdlOutdoorGameHints> listOutdoorGameHints = new List<MdlOutdoorGameHints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.OutdoorGameHints.Where(i => i.IdOutdoorGame == idOutdoorGame && i.IdHints == idHintPoint))
                {
                    MdlOutdoorGameHints mdlOutdoorGameHints = new MdlOutdoorGameHints();
                    mdlOutdoorGameHints.IdHints = element.IdHints;
                    mdlOutdoorGameHints.IdOutdoorGame = element.IdOutdoorGame;
                    mdlOutdoorGameHints.IdPoint = element.IdPoint;
                    mdlOutdoorGameHints.Hint = element.Hint;

                    listOutdoorGameHints.Add(mdlOutdoorGameHints);
                }
                return listOutdoorGameHints.ToList();
            }
        }
        [HttpPost]
        [Route("Buildings/OutdoorGame/RecordTime/{idOutdoorGame}/{name}/{MAC}/{start}")]
        public IActionResult PostOutdoorGameRecord(int idOutdoorGame, string name, string MAC, bool start)
        {
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                OutdoorGameRecordTime mdlOutdoorGameRecord = new OutdoorGameRecordTime();
                if (start == true)
                {
                    if (whereToGoEntities.OutdoorGameRecordTime.Where(o => o.IdOutdoorGame == idOutdoorGame && o.Mac == MAC).Count() > 0)
                    {
                        return Ok(1);
                    }
                    if (whereToGoEntities.OutdoorGameRecordTime.Where(o => o.IdOutdoorGame == idOutdoorGame && o.Name == name).Count() > 0)
                    {
                        return Ok(2);
                    }

                    mdlOutdoorGameRecord.IdOutdoorGame = idOutdoorGame;
                    mdlOutdoorGameRecord.Name = name;
                    mdlOutdoorGameRecord.Mac = MAC;
                    mdlOutdoorGameRecord.StartDate = DateTime.Now;
                    mdlOutdoorGameRecord.EndDate = null;
                    whereToGoEntities.OutdoorGameRecordTime.Add(mdlOutdoorGameRecord);
                    whereToGoEntities.SaveChanges();
                    return Ok(mdlOutdoorGameRecord.StartDate);
                }
                else
                {
                    mdlOutdoorGameRecord = whereToGoEntities.OutdoorGameRecordTime.Where(i => i.IdOutdoorGame == idOutdoorGame && i.Mac == MAC && i.Name == name).FirstOrDefault();
                    mdlOutdoorGameRecord.EndDate = DateTime.Now;
                    whereToGoEntities.OutdoorGameRecordTime.Update(mdlOutdoorGameRecord);
                    whereToGoEntities.SaveChanges();

                    double time = (mdlOutdoorGameRecord.EndDate - mdlOutdoorGameRecord.StartDate).Value.TotalSeconds;
                    return Ok(time);
                }
            }
        }
        [HttpGet]
        [Route("Buildings/OutdoorGame/RecordTime/{idOutdoorGame}")]
        public IEnumerable<MdlOutdoorGameRecord> GetOutdoorGameRecord(int idOutdoorGame)
        {
            List<MdlOutdoorGameRecord> listOutdoorGameRecord = new List<MdlOutdoorGameRecord>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.OutdoorGameRecordTime.Where(i => i.IdOutdoorGame == idOutdoorGame && i.EndDate != null))
                {
                    MdlOutdoorGameRecord mdlOutdoorGameRecord = new MdlOutdoorGameRecord();
                    mdlOutdoorGameRecord.IdRecord = element.IdRecord;
                    mdlOutdoorGameRecord.IdOutdoorGame = element.IdOutdoorGame;
                    mdlOutdoorGameRecord.Name = element.Name;
                    mdlOutdoorGameRecord.Mac = element.Mac;
                    mdlOutdoorGameRecord.StartDate = element.StartDate;
                    mdlOutdoorGameRecord.EndDate = element.EndDate;
                    mdlOutdoorGameRecord.Time = (int)(element.EndDate - element.StartDate).Value.TotalSeconds;

                    listOutdoorGameRecord.Add(mdlOutdoorGameRecord);
                }
                List<MdlOutdoorGameRecord> listOutdoorGameRecordTop10 = new List<MdlOutdoorGameRecord>();
                foreach (var item in listOutdoorGameRecord.OrderBy(i => i.Time).Take(10))
                {
                    listOutdoorGameRecordTop10.Add(item);
                }
                return listOutdoorGameRecordTop10.ToList();
            }
        }
    }
}