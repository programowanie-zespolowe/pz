using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class OutdoorGamePath
    {
        public OutdoorGamePath()
        {
            InverseIdNextPointNavigation = new HashSet<OutdoorGamePath>();
            OutdoorGame = new HashSet<OutdoorGame>();
        }

        public int IdQuestionPoint { get; set; }
        public int? IdOutdoorGame { get; set; }
        public int? IdPoint { get; set; }
        public string Question { get; set; }
        public string Answer { get; set; }
        public int? IdNextPoint { get; set; }
        public int? IdHintPoint { get; set; }

        public virtual OutdoorGameHints IdHintPointNavigation { get; set; }
        public virtual OutdoorGamePath IdNextPointNavigation { get; set; }
        public virtual OutdoorGame IdOutdoorGameNavigation { get; set; }
        public virtual Points IdPointNavigation { get; set; }
        public virtual ICollection<OutdoorGamePath> InverseIdNextPointNavigation { get; set; }
        public virtual ICollection<OutdoorGame> OutdoorGame { get; set; }
    }
}
