﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WhereToGo.Admin.Models;
using WhereToGoEntities.WhereToGo.Models;
using static System.Net.Mime.MediaTypeNames;

namespace linuxWS_Configure.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class GetDataController : ControllerBase
    {
        [HttpGet]
        [Route("Buildings")]
        public IEnumerable<MdlBuildings> Get()
        {
            List<MdlBuildings> buildingImages = new List<MdlBuildings>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Buildings.Where(i => i.IdUser == UserSettings.IdAdmin))
                {
                    MdlBuildings mdlBuildings = new MdlBuildings();
                    mdlBuildings.IdBuilding = element.IdBuilding;
                    mdlBuildings.IdUser = element.IdUser;
                    mdlBuildings.NameBuilding = element.NameBuilding;
                    mdlBuildings.ImageBuilding = element.ImageBuilding;
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
                    mdlBuildingImages.Scale = element.Scale;
                    mdlBuildingImages.NorthPointAngle = element.NorthPointAngle;
                    buildingImages.Add(mdlBuildingImages);
                }
                return buildingImages.ToList();
            }
        }
        //[HttpGet]
        //[Route("Buildings/Points")]
        //public IEnumerable<MdlPoints> GetPoints()
        //{
        //    List<MdlPoints> buildingPoints = new List<MdlPoints>();
        //    using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
        //    {
        //        MdlPoints mdlPoints = new MdlPoints();
        //        foreach (var element in whereToGoEntities.Points)
        //        {
        //            mdlPoints.IdPoint = element.IdPoint;
        //            mdlPoints.IdImage = element.IdImage;
        //            mdlPoints.NamePoint = element.NamePoint;
        //            mdlPoints.X = element.X;
        //            mdlPoints.Y = element.Y;
        //            mdlPoints.IdPointType = element.IdPointType;
        //            mdlPoints.IdGroup = element.IdGroup;
        //            mdlPoints.ImagePoint = element.ImagePoint;

        //            buildingPoints.Add(mdlPoints);
        //        }
        //        return buildingPoints.ToList();
        //    }
        //}
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
        [Route("Buildings/PointsDetails")]
        public IEnumerable<MdlPointDetails> GetPointsDetails()
        {
            List<MdlPointDetails> listPointsDetails = new List<MdlPointDetails>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.PointsDetail)
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
    }
}