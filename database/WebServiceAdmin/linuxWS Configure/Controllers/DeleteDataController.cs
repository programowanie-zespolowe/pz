using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using linuxWS_Configure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using WhereToGoEntities.WhereToGo.Models;

namespace WhereToGo.Admin.Controllers
{
    [Authorize]
    [ApiController]
    [Route("Admin/[controller]")]
    public class DeleteDataController : Controller
    {
        [HttpDelete("Buildings/{idBuilding}")]
        public IActionResult DeleteBuilding(int idBuilding)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlBuilding = whereToGo.Buildings.Where(i => i.IdBuilding == idBuilding)
                                                        .Include(p => p.Groups).ThenInclude(v => v.PointsDetail)
                                                        .Include(x => x.BuildingImages).ThenInclude(c => c.Points).ThenInclude(b => b.PointsConnectionIdPointStartNavigation)
                                                        .Include(x => x.BuildingImages).ThenInclude(c => c.Points).ThenInclude(b => b.PointsConnectionIdPointEndNavigation)

                                                        .FirstOrDefault();
            if (mdlBuilding == null)
                return NotFound();
            try
            {
                whereToGo.Buildings.Remove(mdlBuilding);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete builiding image connected with this builiding before deleting");
                    }
                }
            }
            return Ok(mdlBuilding);
        }
        [HttpDelete("BuildingImages/{idBuildingImages}")]
        public IActionResult DeleteBuildingImages(int idBuildingImages)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlBuilding = whereToGo.BuildingImages.Where(i => i.IdImage == idBuildingImages)
                                                        .Include(p => p.Points).ThenInclude(x => x.PointsDetail)
                                                        .Include(p => p.Points).ThenInclude(x => x.PointsConnectionIdPointStartNavigation)
                                                        .Include(p => p.Points).ThenInclude(x => x.PointsConnectionIdPointEndNavigation)
                                                        .FirstOrDefault();
            if (mdlBuilding == null)
                return NotFound();
            try
            {
                whereToGo.BuildingImages.Remove(mdlBuilding);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete points connected with this builiding image before deleting");
                    }
                }
            }
            return Ok(mdlBuilding);
        }
        [HttpDelete("Groups/{idGroup}")]
        public IActionResult DeleteGroup(int idGroup)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlBuilding = whereToGo.Groups.Where(i => i.IdGroup == idGroup)
                                                .Include(x => x.PointsDetail)
                                                .FirstOrDefault();
            if (mdlBuilding == null)
                return NotFound();
            try
            {
                whereToGo.Groups.Remove(mdlBuilding);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete points which are connected with this group before deleting");
                    }
                }
            }

            return Ok(mdlBuilding);
        }
        [HttpDelete("Point/{idPoint}")]
        public IActionResult DeletePoint(int idPoint)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlPoint = whereToGo.Points.Where(i => i.IdPoint == idPoint)
                                                                            .Include(x => x.PointsDetail)
                                                                            .Include(c => c.PointsConnectionIdPointStartNavigation)
                                                                            .Include(c => c.PointsConnectionIdPointEndNavigation)
                                                                            .FirstOrDefault();
            if (mdlPoint == null)
                return NotFound();
            try
            {
                whereToGo.Points.Remove(mdlPoint);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete groups, point types and image builidings which are connected with this Point before deleting");
                    }
                }
            }

            return Ok(mdlPoint);
        }
        [HttpDelete("PointsDetails/{idPointDetails}")]
        public IActionResult DeletePointDetailsByPoint(int idPointDetails)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlPoint = whereToGo.PointsDetail.Where(i => i.IdPointDetails == idPointDetails).FirstOrDefault();
            if (mdlPoint == null)
                return NotFound();
            try
            {
                whereToGo.PointsDetail.Remove(mdlPoint);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete Point which are connected with this Point Details before deleting");
                    }
                }
            }

            return Ok(mdlPoint);
        }
        [HttpDelete("PointType/{idPointType}")]
        public IActionResult DeletePointType(int idPointType)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlPointType = whereToGo.PointType.Where(i => i.IdPointType == idPointType).FirstOrDefault();
            if (mdlPointType == null)
                return NotFound();
            try
            {
                whereToGo.PointType.Remove(mdlPointType);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete points which are connected with this PointType before deleting");
                    }
                }
            }

            return Ok(mdlPointType);
        }
        [HttpDelete("PointsConnection/{IdPointConnection}")]
        public IActionResult DeleteIdPointConnection(int IdPointConnection)
        {
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var mdlIdPointConnection = whereToGo.PointsConnection.Where(i => i.IdPointConnection == IdPointConnection).FirstOrDefault();
            if (mdlIdPointConnection == null)
                return NotFound();
            try
            {
                whereToGo.PointsConnection.Remove(mdlIdPointConnection);
                whereToGo.SaveChanges();
            }
            catch (DbUpdateException ex)
            {
                var sqlException = ex.GetBaseException() as SqlException;

                if (sqlException != null)
                {
                    var number = sqlException.Number;

                    if (number == 547)
                    {
                        return BadRequest($"Must delete points which are connected with this PointType before deleting");
                    }
                }
            }

            return Ok(mdlIdPointConnection);
        }
    }
}