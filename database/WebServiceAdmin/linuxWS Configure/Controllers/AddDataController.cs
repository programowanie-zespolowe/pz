using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using WhereToGoEntities.WhereToGo.Models;

namespace WhereToGo.Admin.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class AddDataController : Controller
    {
        readonly ILogger<AddDataController> _log;
        public AddDataController(ILogger<AddDataController> log)
        {
            _log = log;
        }
        [Route("Buildings/{NameBuilding}/{scale}")]
        [HttpPost]
        public IActionResult PostBuildings(string NameBuilding, double scale, [FromForm] IFormFile ImageRead)
        {
            string deNameBuilding = System.Net.WebUtility.UrlDecode(NameBuilding);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Buildings mdlBuildings = new Buildings();
                mdlBuildings.IdUser = UserSettings.IdAdmin;
                mdlBuildings.NameBuilding = deNameBuilding;
                mdlBuildings.Scale = scale;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlBuildings.ImageBuilding = ms.GetBuffer();
                }
                else
                    mdlBuildings.ImageBuilding = null;
                whereToGo.Buildings.Add(mdlBuildings);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }
                return Ok(mdlBuildings.IdBuilding);

            }


        }
        [Route("BuildingImage/{idBuilding}/{BuildingLevel}/{NorthPointAngle}")]
        [HttpPost]
        public IActionResult PostBuildingImages(int idBuilding, int BuildingLevel, double NorthPointAngle, [FromForm] IFormFile ImageRead)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                BuildingImages mdlBuildings = new BuildingImages();
                mdlBuildings.IdBuilding = idBuilding;
                mdlBuildings.BuildingLevel = BuildingLevel;
                if(ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlBuildings.PathImage = ms.GetBuffer();
                }
                else
                    mdlBuildings.PathImage = null;
                mdlBuildings.NorthPointAngle = NorthPointAngle;
                whereToGo.BuildingImages.Add(mdlBuildings);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlBuildings.IdImage);
            }
        }
        [Route("Groups/{NameGroup}/{IdBuilding}")]
        [HttpPost]
        public IActionResult PostGroups(string NameGroup, int IdBuilding, [FromForm] IFormFile ImageRead)
        {
            string deNameGroup = System.Net.WebUtility.UrlDecode(NameGroup);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Groups mdlGroups = new Groups();
                mdlGroups.NameGroup = deNameGroup;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlGroups.ImageGroup = ms.GetBuffer();
                }
                else
                    mdlGroups.ImageGroup = null;
                mdlGroups.IdBuilding = IdBuilding;
                whereToGo.Groups.Add(mdlGroups);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlGroups.IdGroup);

            }

        }
        [Route("PointType/{TypePoint}")]
        [HttpPost]
        public IActionResult PostPointType(string TypePoint)
        {
            string deTypePoint = System.Net.WebUtility.UrlDecode(TypePoint);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointType mdlPointType = new PointType();
                mdlPointType.TypePoint = deTypePoint;
                whereToGo.PointType.Add(mdlPointType);
            try
            {
                whereToGo.SaveChanges();
            }
            catch (Exception)
            {
                return BadRequest(999);
            }

            return Ok(mdlPointType.IdPointType);

        }
        [Route("BuildingsImage/{idBuildingImage}/{X}/{Y}/{IdPointType}/{Direction}/{OnOffDirection}/Points")]
        [HttpPost]
        public IActionResult PostPointByBuildingImages(int idBuildingImage, double X, double Y, int IdPointType, double Direction, bool OnOffDirection)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                Points mdlPoints = new Points();
                mdlPoints.IdImage = idBuildingImage;
                mdlPoints.X = X;
                mdlPoints.Y = Y;
                mdlPoints.IdPointType = IdPointType;
                mdlPoints.ImagePoint = null;
                mdlPoints.Direction = Direction;
                mdlPoints.OnOffDirection = OnOffDirection;
                whereToGo.Points.Add(mdlPoints);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPoints.IdPoint);

            }


        }
        [Route("Points/{idPoint}/{NamePoint}/{IdGroup}/PointDetails")]
        [HttpPost]
        public IActionResult PostPoint(int idPoint, string NamePoint, int IdGroup, [FromForm] IFormFile ImageRead)
        {
            string deNamePoint = System.Net.WebUtility.UrlDecode(NamePoint);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsDetail mdlPointsDetails = new PointsDetail();
                mdlPointsDetails.IdPoint = idPoint;
                mdlPointsDetails.NamePoint = deNamePoint;
                mdlPointsDetails.IdGroup = IdGroup;
                if (ImageRead != null)
                {
                    ImageRead.CopyTo(ms);
                    mdlPointsDetails.ImagePoint = ms.GetBuffer();
                }
                else
                    mdlPointsDetails.ImagePoint = null;
                whereToGo.PointsDetail.Add(mdlPointsDetails);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPointsDetails.IdPointDetails);


            }

        }
        [Route("Points/{IdPointStart}/{IdPointEnd}/PointsConnection")]
        [HttpPost]
        public IActionResult PostPointConnection(int IdPointStart, int IdPointEnd)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                PointsConnection mdlPointsConnection = new PointsConnection();
                mdlPointsConnection.IdPointStart = IdPointStart;
                mdlPointsConnection.IdPointEnd = IdPointEnd;
                whereToGo.PointsConnection.Add(mdlPointsConnection);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlPointsConnection.IdPointConnection);

            }

        }
        [Route("OutdoorGame/{idBuilding}/{nameGame}/{StartDateGame}/{EndDateGame}/{idFirstPoint}")]
        [HttpPost]
        public IActionResult OutdoorGame(int idBuilding, string nameGame, DateTime StartDateGame, DateTime EndDateGame, int idFirstPoint, [FromForm] IFormFile ImageRead)
        {
            string denameGame = System.Net.WebUtility.UrlDecode(nameGame);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                OutdoorGame mdlOutdoorGame = new OutdoorGame();
                mdlOutdoorGame.IdBuilding = idBuilding;
                mdlOutdoorGame.NameGame = denameGame;
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
                    mdlOutdoorGame.ImageGame = null;
                whereToGo.OutdoorGame.Add(mdlOutdoorGame);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlOutdoorGame.IdOutdoorGame);

            }
        }
        [Route("OutdoorGamePath/{idOutdoorGame}/{idPoint}/{Question}/{Answer}/{idNextPoint}/{idHintPoint}")]
        [HttpPost]
        public IActionResult OutdoorGamePath(int idOutdoorGame, int idPoint, string Question, string Answer, int idNextPoint, int idHintPoint)
        {
            string deQuestion = System.Net.WebUtility.UrlDecode(Question);
            string deAnswer = System.Net.WebUtility.UrlDecode(Answer);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                OutdoorGamePath mdlOutdoorGamePath = new OutdoorGamePath();
                mdlOutdoorGamePath.IdOutdoorGame = idOutdoorGame;
                mdlOutdoorGamePath.IdPoint = idPoint;
                mdlOutdoorGamePath.Question = deQuestion;
                mdlOutdoorGamePath.Answer = deAnswer;
                if (idNextPoint == -1)
                    mdlOutdoorGamePath.IdNextPoint = null;
                else
                    mdlOutdoorGamePath.IdNextPoint = idNextPoint;
                if (idHintPoint == -1)
                    mdlOutdoorGamePath.IdHintPoint = null;
                else
                    mdlOutdoorGamePath.IdHintPoint = idHintPoint;
                whereToGo.OutdoorGamePath.Add(mdlOutdoorGamePath);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlOutdoorGamePath.IdQuestionPoint);

            }

        }
        [Route("OutdoorGameHints/{idOutdoorGame}/{idPoint}/{Hint}")]
        [HttpPost]
        public IActionResult OutdoorGameHints(int idOutdoorGame, int idPoint, string Hint)
        {
            string deHint = System.Net.WebUtility.UrlDecode(Hint);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using (var ms = new MemoryStream())
            {
                using WhereToGoContext whereToGo = new WhereToGoContext();
                OutdoorGameHints mdlOutdoorGameHint = new OutdoorGameHints();
                mdlOutdoorGameHint.IdOutdoorGame = idOutdoorGame;
                mdlOutdoorGameHint.IdPoint = idPoint;
                mdlOutdoorGameHint.Hint = deHint;
                whereToGo.OutdoorGameHints.Add(mdlOutdoorGameHint);
                try
                {
                    whereToGo.SaveChanges();
                }
                catch (Exception)
                {
                    return BadRequest(999);
                }

                return Ok(mdlOutdoorGameHint.IdHints);

            }

        }

    }
}