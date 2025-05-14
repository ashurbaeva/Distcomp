using AutoMapper;
using LabsRV_Articles.Data;
using LabsRV_Articles.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.Linq.Expressions;
using System.Xml;

namespace LabsRV_Articles.Repositories
{
    public class AuthorRepository : Repository<Author>
    {
        public AuthorRepository(ApplicationDbContext context):
            base(context)
        { }

        // Реализация индивидуального метода проверки уникальности логина для авторов
        public async Task<bool> UniqueLoginExistsAsync(string login)
        {
            // Выполняется запрос к таблице авторов, без отслеживания, для проверки,
            // существует ли уже автор с заданным логином.
            return await _dbSet.AsNoTracking().AnyAsync(author => (author.login == login) );
        }
    }
}
