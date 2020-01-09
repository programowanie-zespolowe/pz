using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using WhereToGoEntities.WhereToGo.Models;

namespace linuxWS.Controllers
{
    [Authorize]
    [ApiController]
    [Route("[controller]")]
    public class GetDataController : ControllerBase
    {

        private readonly ILogger<GetDataController> _logger;

        public GetDataController(ILogger<GetDataController> logger)
        {
            _logger = logger;
        }

        [HttpGet]
        [Route("Buildings")]
        public IEnumerable<Buildings> Get()
        {
            List<Buildings> buildingImages = new List<Buildings>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Buildings)
                {
                    buildingImages.Add(element);
                }
                return buildingImages.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/{idBuilding}")]
        public IEnumerable<BuildingImages> GetBuildings(int idBuilding)
        {
            List<BuildingImages> buildingImages = new List<BuildingImages>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.BuildingImages.Where(i => i.IdBuilding == idBuilding))
                {
                    buildingImages.Add(element);
                }
                return buildingImages.ToList();
            }
        }
        [HttpGet]
        [Route("Buildings/Points/{idImage}")]
        public IEnumerable<Points> GetPoints(int idImage)
        {
            List<Points> buildingPoints = new List<Points>();
            using (WhereToGoContext whereToGoEntities = new WhereToGoContext())
            {
                foreach (var element in whereToGoEntities.Points.Where(i => i.IdImage == idImage))
                {
                    buildingPoints.Add(element);
                }
                return buildingPoints.ToList();
            }
        }
    }
}