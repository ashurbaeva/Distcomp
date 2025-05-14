using LAB1.Domain;
using LAB1.DTOs;
using LAB1.Interfaces;

namespace LAB1.Services;

public class UserService
{
    private readonly IRepository<User> _repository;

    public UserService(IRepository<User> repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<UserResponseTo>> GetAllAsync()
    {
        var users = await _repository.GetAllAsync();
        return users.Select(u => new UserResponseTo
        {
            Id = u.Id,
            Login = u.Login,
            Firstname = u.Firstname,
            Lastname = u.Lastname
        });
    }

    public async Task<UserResponseTo?> GetByIdAsync(int id)
    {
        var user = await _repository.GetByIdAsync(id);
        if (user == null) return null;

        return new UserResponseTo
        {
            Id = user.Id,
            Login = user.Login,
            Firstname = user.Firstname,
            Lastname = user.Lastname
        };
    }

    public async Task<UserResponseTo> CreateAsync(UserRequestTo userRequest)
    {
        if (string.IsNullOrEmpty(userRequest.Login))
            throw new ArgumentException("Login is required");
        if (string.IsNullOrEmpty(userRequest.Password))
            throw new ArgumentException("Password is required");
        if (userRequest.Login.Length < 2 || userRequest.Login.Length > 64)
            throw new ArgumentException("Login is not valid");
        if (userRequest.Firstname.Length < 2 || userRequest.Firstname.Length > 64)
            throw new ArgumentException("Login is not valid");
        if (userRequest.Lastname.Length < 2 || userRequest.Lastname.Length > 64)
            throw new ArgumentException("Login is not valid");

        var user = new User
        {
            Login = userRequest.Login,
            Password = userRequest.Password,
            Firstname = userRequest.Firstname,
            Lastname = userRequest.Lastname
        };

        await _repository.CreateAsync(user);
        
        return new UserResponseTo
        {
            Id = user.Id,
            Login = user.Login,
            Firstname = user.Firstname,
            Lastname = user.Lastname
        };
    }

    public async Task<UserResponseTo?> UpdateAsync(UserRequestTo userRequest)
    {
        var user = await _repository.GetByIdAsync(userRequest.Id);
        if (user == null) return null;

        if (string.IsNullOrEmpty(userRequest.Login))
            throw new ArgumentException("Login is required");
        if (string.IsNullOrEmpty(userRequest.Password))
            throw new ArgumentException("Password is required");
        if (userRequest.Login.Length < 2 || userRequest.Login.Length > 64)
            throw new ArgumentException("Login is not valid");
        if (userRequest.Firstname.Length < 2 || userRequest.Firstname.Length > 64)
            throw new ArgumentException("Login is not valid");
        if (userRequest.Lastname.Length < 2 || userRequest.Lastname.Length > 64)
            throw new ArgumentException("Login is not valid");

        user.Login = userRequest.Login;
        user.Password = userRequest.Password;
        user.Firstname = userRequest.Firstname;
        user.Lastname = userRequest.Lastname;

        await _repository.UpdateAsync(user);
        
        return new UserResponseTo
        {
            Id = user.Id,
            Login = user.Login,
            Firstname = user.Firstname,
            Lastname = user.Lastname
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}