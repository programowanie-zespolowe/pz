using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WhereToGo.Admin.Models
{
    public class MdlOutdoorGamePath
    {
        public int IdQuestionPoint { get; set; }
        public int? IdOutdoorGame { get; set; }
        public int? IdPoint { get; set; }
        public string Question { get; set; }
        public string Answer { get; set; }
        public int? IdNextPoint { get; set; }
        public int? IdHintPoint { get; set; }
    }
}
