﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LinuxWS.Models
{
    public class MdlGroups
    {
        public int IdGroup { get; set; }
        public string NameGroup { get; set; }
        public byte[] ImageGroup { get; set; }
        public int? IdBuilding { get; set; }
    }
}
