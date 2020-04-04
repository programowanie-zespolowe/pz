using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class OutdoorGamePath
    {
        public int IdQuestionPoint { get; set; }
        public int? IdOutdoorGame { get; set; }
        public int? IdPoint { get; set; }
        public string Question { get; set; }
        public string Answer { get; set; }
        public int? IdNextPoint { get; set; }
        public int? IdHintPoint { get; set; }

        public virtual Points IdHintPointNavigation { get; set; }
        public virtual Points IdNextPointNavigation { get; set; }
        public virtual OutdoorGame IdOutdoorGameNavigation { get; set; }
        public virtual Points IdPointNavigation { get; set; }
    }
}
