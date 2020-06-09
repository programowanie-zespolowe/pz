using linuxWS.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WhereToGoEntities.WhereToGo.Models;

namespace linuxWS
{
    public interface IAuthService
    {
        bool IsValidUser(ModelAuthentitactions user, WhereToGoEntities.WhereToGo.Models.Authentications authentications);
    }
    public class AuthService : IAuthService
    {
        private readonly WhereToGoContext _context;
        public AuthService(WhereToGoContext context)
        {
            _context = context;
        }
        public bool IsValidUser(ModelAuthentitactions user, WhereToGoEntities.WhereToGo.Models.Authentications authentications)
        {
            return _context.Authentications.Any(x => x.MacId.Equals(user.MacId));
        }
    }
}
