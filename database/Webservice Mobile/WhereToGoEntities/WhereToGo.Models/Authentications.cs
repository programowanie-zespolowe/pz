using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Authentications
    {
        public int IdAuthentication { get; set; }
        public string NameDevice { get; set; }
        public string MacId { get; set; }
        public bool? IsBlocked { get; set; }
    }
}
