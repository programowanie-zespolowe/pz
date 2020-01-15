using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using WhereToGoEntities.WhereToGo.Models;

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
            List <MdlBuildings> buildingImages = new List<MdlBuildings>();
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
        [Route("Buildings/Points/{idImage}")]
        public IEnumerable<MdlPoints> GetPoints(int idImage)
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
    }
}