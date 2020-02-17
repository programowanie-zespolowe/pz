using System;
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
                MdlBuildings mdlBuildings = new MdlBuildings();
                foreach (var element in whereToGoEntities.Buildings.Where(i => i.IdUser == UserSettings.IdAdmin))
                {
                    mdlBuildings.IdBuilding = element.IdBuilding;
                    mdlBuildings.IdUser = element.IdUser;
                    mdlBuildings.NameBuilding = element.NameBuilding;
                    mdlBuildings.ImageBuilding = element.ImageBuilding;
                    buildingImages.Add(mdlBuildings);
                }
                return buildingImages.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}")]
        public IEnumerable<MdlBuildingImages> GetBuildings(int idBuilding)
        {
            List<MdlBuildingImages> buildingImages = new List<MdlBuildingImages>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                MdlBuildingImages mdlBuildingImages = new MdlBuildingImages();
                foreach (var element in whereToGoEntities.BuildingImages.Where(i => i.IdBuilding == idBuilding))
                {
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
        [HttpGet]
        [Route("Buildings/Points")]
        public IEnumerable<MdlPoints> GetPoints()
        {
            List<MdlPoints> buildingPoints = new List<MdlPoints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                MdlPoints mdlPoints = new MdlPoints();
                foreach (var element in whereToGoEntities.Points)
                {
                    mdlPoints.IdPoint = element.IdPoint;
                    mdlPoints.IdImage = element.IdImage;
                    mdlPoints.NamePoint = element.NamePoint;
                    mdlPoints.X = element.X;
                    mdlPoints.Y = element.Y;
                    mdlPoints.IdPointType = element.IdPointType;
                    mdlPoints.IdGroup = element.IdGroup;
                    mdlPoints.ImagePoint = element.ImagePoint;

                    buildingPoints.Add(mdlPoints);
                }
                return buildingPoints.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Points/{idImage}")]
        public IEnumerable<MdlPoints> GetPointByImage(int idImage)
        {
            List<MdlPoints> buildingPoints = new List<MdlPoints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                MdlPoints mdlPoints = new MdlPoints();
                foreach (var element in whereToGoEntities.Points.Where(i => i.IdImage == idImage))
                {
                    mdlPoints.IdPoint = element.IdPoint;
                    mdlPoints.IdImage = element.IdImage;
                    mdlPoints.NamePoint = element.NamePoint;
                    mdlPoints.X = element.X;
                    mdlPoints.Y = element.Y;
                    mdlPoints.IdPointType = element.IdPointType;
                    mdlPoints.IdGroup = element.IdGroup;
                    mdlPoints.ImagePoint = element.ImagePoint;

                    buildingPoints.Add(mdlPoints);
                }
                return buildingPoints.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Point/{idPoint}")]
        public IEnumerable<MdlPoints> GetPoint(int idPoint)
        {
            List<MdlPoints> buildingPoints = new List<MdlPoints>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                MdlPoints mdlPoints = new MdlPoints();
                foreach (var element in whereToGoEntities.Points.Where(i => i.IdPoint == idPoint))
                {
                    mdlPoints.IdPoint = element.IdPoint;
                    mdlPoints.IdImage = element.IdImage;
                    mdlPoints.NamePoint = element.NamePoint;
                    mdlPoints.X = element.X;
                    mdlPoints.Y = element.Y;
                    mdlPoints.IdPointType = element.IdPointType;
                    mdlPoints.IdGroup = element.IdGroup;
                    mdlPoints.ImagePoint = element.ImagePoint;

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
                MdlGroups mdlGroups = new MdlGroups();
                foreach (var element in whereToGoEntities.Groups)
                {
                    mdlGroups.IdGroup = element.IdGroup;
                    mdlGroups.NameGroup = element.NameGroup;
                    mdlGroups.ImageGroup = element.ImageGroup;

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
                MdlGroups mdlGroups = new MdlGroups();
                foreach (var element in whereToGoEntities.Groups.Where(i=>i.IdGroup == idGroup))
                {
                    mdlGroups.IdGroup = element.IdGroup;
                    mdlGroups.NameGroup = element.NameGroup;
                    mdlGroups.ImageGroup = element.ImageGroup;

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
                MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                foreach (var element in whereToGoEntities.PointsDetail)
                {
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.Detail1 = element.Detail1;
                    mdlPointsDetails.Detail2 = element.Detail2;
                    mdlPointsDetails.Detail3 = element.Detail3;
                    mdlPointsDetails.Detail4 = element.Detail4;
                    mdlPointsDetails.Detail5 = element.Detail5;

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
                MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                foreach (var element in whereToGoEntities.PointsDetail.Where(i => i.IdPoint == idPoint))
                {
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.Detail1 = element.Detail1;
                    mdlPointsDetails.Detail2 = element.Detail2;
                    mdlPointsDetails.Detail3 = element.Detail3;
                    mdlPointsDetails.Detail4 = element.Detail4;
                    mdlPointsDetails.Detail5 = element.Detail5;

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
                MdlPointDetails mdlPointsDetails = new MdlPointDetails();
                foreach (var element in whereToGoEntities.PointsDetail.Where(i => i.IdPointDetails == idPointDetails))
                {
                    mdlPointsDetails.IdPointDetails = element.IdPointDetails;
                    mdlPointsDetails.IdPoint = element.IdPoint;
                    mdlPointsDetails.NamePoint = element.NamePoint;
                    mdlPointsDetails.Detail1 = element.Detail1;
                    mdlPointsDetails.Detail2 = element.Detail2;
                    mdlPointsDetails.Detail3 = element.Detail3;
                    mdlPointsDetails.Detail4 = element.Detail4;
                    mdlPointsDetails.Detail5 = element.Detail5;

                    listPointsDetails.Add(mdlPointsDetails);
                }
                return listPointsDetails.ToList();
            }
        }
    }
}