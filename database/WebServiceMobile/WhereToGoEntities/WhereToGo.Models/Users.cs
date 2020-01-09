using System;
using System.Collections.Generic;

namespace WhereToGoEntities.WhereToGo.Models
{
    public partial class Users
    {
        public Users()
        {
            Buildings = new HashSet<Buildings>();
        }

        public int IdUser { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }

        public virtual ICollection<Buildings> Buildings { get; set; }
    }
}
