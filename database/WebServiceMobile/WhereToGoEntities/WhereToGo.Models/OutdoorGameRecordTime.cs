using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class OutdoorGameRecordTime
    {
        public int IdRecord { get; set; }
        public int? IdOutdoorGame { get; set; }
        public string Mac { get; set; }
        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }
        public string Name { get; set; }

        public virtual OutdoorGame IdOutdoorGameNavigation { get; set; }
    }
}
