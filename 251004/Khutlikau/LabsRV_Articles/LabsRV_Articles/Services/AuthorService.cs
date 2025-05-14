using AutoMapper;
using LabsRV_Articles.Models.Domain;
using LabsRV_Articles.Models.DTO;
using LabsRV_Articles.Models.Exceptions;
using LabsRV_Articles.Repositories;
using Microsoft.EntityFrameworkCore;
using static Dapper.SqlMapper;

namespace LabsRV_Articles.Services
{
    public class AuthorService : Service<Author, AuthorRequestDto, AuthorResponseDto>
    {
        protected new readonly AuthorRepository _repository;

        public AuthorService(AuthorRepository repository, IMapper mapper)
            : base(repository, mapper) 
        {
            this._repository = repository;
        }

        public async Task<AuthorResponseDto> CreateAsync(AuthorRequestDto request)
        {
            Validate(request);
            // Проверяем, существует ли уже автор с таким логином
            bool exists = await _repository.UniqueLoginExistsAsync(request.Login);

            if (exists)
            {
                // Можно выбросить исключение с нужным сообщением,
                // которое далее перехватится в контроллере и будет возвращено с кодом 403.
                throw new AlreadyExistsException($"Логин '{request.Login}' уже существует.");
            }

            // Если логина нет – мапим DTO в доменную модель и сохраняем
            var entity = _mapper.Map<Author>(request);
            var created = _repository.Add(entity);
            return _mapper.Map<AuthorResponseDto>(created);
        }

        public override void Validate(AuthorRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Login) || request.Login.Length < 2 || request.Login.Length > 64)
                throw new ArgumentException("Login must be between 2 and 64 characters.");
            if (string.IsNullOrWhiteSpace(request.Password) || request.Password.Length < 8 || request.Password.Length > 128)
                throw new ArgumentException("Password must be between 8 and 128 characters.");
            if (string.IsNullOrWhiteSpace(request.FirstName) || request.FirstName.Length < 2 || request.FirstName.Length > 64)
                throw new ArgumentException("FirstName must be between 2 and 64 characters.");
            if (string.IsNullOrWhiteSpace(request.LastName) || request.LastName.Length < 2 || request.LastName.Length > 64)
                throw new ArgumentException("LastName must be between 2 and 64 characters.");
        }
    }
}