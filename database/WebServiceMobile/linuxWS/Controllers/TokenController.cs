using linuxWS.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using System;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using WhereToGoEntities.WhereToGo.Models;

namespace linuxWS.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TokenController : ControllerBase
    {
        private readonly AuthOptions _authOptions;
        private Authentications mdlAuthentitactions;
        public TokenController(IAuthService service, IOptions<AuthOptions> authOptionsAccessor)
        {
            _authOptions = authOptionsAccessor.Value;
        }

        [HttpPost]
        public IActionResult Post([FromBody] Authentications authentications)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            using WhereToGoContext whereToGo = new WhereToGoContext();
            var item = whereToGo.Authentications.Where(i => i.MacId == authentications.MacId).FirstOrDefault();

            if (item != null)
            {
                if (item.IsBlocked == true)
                {
                    return Unauthorized();
                }
                if (item.NameDevice != authentications.NameDevice)
                {
                    var entity = whereToGo.Authentications.FirstOrDefault(item => item.MacId == authentications.MacId);
                    entity.MacId = item.MacId;
                    entity.NameDevice = authentications.NameDevice;
                    entity.IsBlocked = item.IsBlocked;
                    whereToGo.Authentications.Update(entity);
                    whereToGo.SaveChanges();

                }
                var authClaims = new[]
                {
                    new Claim(JwtRegisteredClaimNames.Sub, authentications.MacId),
                    new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
                };

                var token = new JwtSecurityToken(
                    issuer: _authOptions.Issuer,
                    audience: _authOptions.Audience,
                    expires: DateTime.Now.AddHours(_authOptions.ExpiresInMinutes),
                    claims: authClaims,
                    signingCredentials: new SigningCredentials(new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_authOptions.SecureKey)),
                        SecurityAlgorithms.HmacSha256Signature)
                    );

                return Ok(new
                {
                    token = new JwtSecurityTokenHandler().WriteToken(token),
                    expiration = token.ValidTo
                });
            } 
            else
            {
                mdlAuthentitactions = new Authentications();
                mdlAuthentitactions.MacId = authentications.MacId;
                mdlAuthentitactions.NameDevice = authentications.NameDevice;
                mdlAuthentitactions.IsBlocked = false;
                whereToGo.Authentications.Add(mdlAuthentitactions);
                whereToGo.SaveChanges();

                var authClaims = new[]
                {
                    new Claim(JwtRegisteredClaimNames.Sub, authentications.MacId),
                    new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
                };

                var token = new JwtSecurityToken(
                    issuer: _authOptions.Issuer,
                    audience: _authOptions.Audience,
                    expires: DateTime.Now.AddHours(_authOptions.ExpiresInMinutes),
                    claims: authClaims,
                    signingCredentials: new SigningCredentials(new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_authOptions.SecureKey)),
                        SecurityAlgorithms.HmacSha256Signature)
                    );

                return Ok(new
                {
                    token = new JwtSecurityTokenHandler().WriteToken(token),
                    expiration = token.ValidTo
                });
            }
        }
    }
}