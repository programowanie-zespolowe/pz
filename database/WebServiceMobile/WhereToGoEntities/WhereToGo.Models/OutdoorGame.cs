using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class OutdoorGame
    {
        public OutdoorGame()
        {
            OutdoorGameHints = new HashSet<OutdoorGameHints>();
            OutdoorGamePath = new HashSet<OutdoorGamePath>();
            OutdoorGameRecordTime = new HashSet<OutdoorGameRecordTime>();
        }

        public int IdOutdoorGame { get; set; }
        public int? IdBuilding { get; set; }
        public string NameGame { get; set; }
        public byte[] ImageGame { get; set; }
        public DateTime? StartDateGame { get; set; }
        public DateTime? EndDateGame { get; set; }
        public int? IdFirstPoint { get; set; }

        public virtual Buildings IdBuildingNavigation { get; set; }
        public virtual ICollection<OutdoorGameHints> OutdoorGameHints { get; set; }
        public virtual ICollection<OutdoorGamePath> OutdoorGamePath { get; set; }
        public virtual ICollection<OutdoorGameRecordTime> OutdoorGameRecordTime { get; set; }
    }
}
