using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class OutdoorGameHints
    {
        public OutdoorGameHints()
        {
            OutdoorGamePath = new HashSet<OutdoorGamePath>();
        }

        public int IdHints { get; set; }
        public int? IdOutdoorGame { get; set; }
        public int? IdPoint { get; set; }
        public string Hint { get; set; }

        public virtual OutdoorGame IdOutdoorGameNavigation { get; set; }
        public virtual Points IdPointNavigation { get; set; }
        public virtual ICollection<OutdoorGamePath> OutdoorGamePath { get; set; }
    }
}
