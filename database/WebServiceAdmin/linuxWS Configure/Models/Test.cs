using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WhereToGo.Admin.Models
{
    public class Test
    {
        public string Name { get; set; }
        public IFormFile Image { get; set; }
    }
}
