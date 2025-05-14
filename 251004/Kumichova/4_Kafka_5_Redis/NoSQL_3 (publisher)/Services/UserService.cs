using LAB2.Data;
using LAB2.Domain;
using LAB2.DTOs;
using LAB2.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace LAB2.Services;

public class UserService
{
    private readonly IRepository<User> _repository;
    private readonly AppDbContext _context;

    public UserService(IRepository<User> repository, AppDbContext context)
    {
        _repository = repository;
        _context = context;
    }

    private async Task<bool> IsLoginUniqueAsync(string login, int? id = null)
    {
        var existingUser = await _context.Users
            .FirstOrDefaultAsync(u => u.Login == login && (id == null || u.id != id));
        return existingUser == null;
    }

    public async Task<IEnumerable<UserResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var users = await _repository.GetAllAsync(queryParams);
        return users.Select(u => new UserResponseTo
        {
            Id = u.id,
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
            Id = user.id,
            Login = user.Login,
            Firstname = user.Firstname,
            Lastname = user.Lastname
        };
    }

    public async Task<UserResponseTo> CreateAsync(UserRequestTo userRequest)
    {
        // Validation
        if (string.IsNullOrEmpty(userRequest.Login) || userRequest.Login.Length < 2 || userRequest.Login.Length > 64)
            throw new ArgumentException("Login must be between 2 and 64 characters");
            
        if (string.IsNullOrEmpty(userRequest.Password) || userRequest.Password.Length < 8 || userRequest.Password.Length > 128)
            throw new ArgumentException("Password must be between 8 and 128 characters");
            
        if (string.IsNullOrEmpty(userRequest.Firstname) || userRequest.Firstname.Length < 2 || userRequest.Firstname.Length > 64)
            throw new ArgumentException("Firstname must be between 2 and 64 characters");
            
        if (string.IsNullOrEmpty(userRequest.Lastname) || userRequest.Lastname.Length < 2 || userRequest.Lastname.Length > 64)
            throw new ArgumentException("Lastname must be between 2 and 64 characters");

        if (!await IsLoginUniqueAsync(userRequest.Login))
            throw new InvalidOperationException("Login must be unique");

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
            Id = user.id,
            Login = user.Login,
            Firstname = user.Firstname,
            Lastname = user.Lastname
        };
    }

    public async Task<UserResponseTo?> UpdateAsync(UserRequestTo userRequest)
    {
        // Validation (same as Create)
        // ...

        var user = await _repository.GetByIdAsync(userRequest.Id);
        if (user == null) return null;

        user.Login = userRequest.Login;
        user.Password = userRequest.Password;
        user.Firstname = userRequest.Firstname;
        user.Lastname = userRequest.Lastname;

        await _repository.UpdateAsync(user);
            
        return new UserResponseTo
        {
            Id = user.id,
            Login = user.Login,
            Firstname = user.Firstname,
            Lastname = user.Lastname
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var user = await _repository.GetByIdAsync(id);
        if (user == null) return false;

        // Load related topics
        await _context.Entry(user)
            .Collection(u => u.Topics)
            .LoadAsync();

        // Delete related topics with their messages and tags
        foreach (var topic in user.Topics.ToList())
        {
            await DeleteTopicWithRelations(topic);
        }

        return await _repository.DeleteAsync(id);
    }

    private async Task DeleteTopicWithRelations(Topic topic)
    {
        // Load related messages
        await _context.Entry(topic)
            .Collection(t => t.Messages)
            .LoadAsync();

        // Load related tags
        await _context.Entry(topic)
            .Collection(t => t.Tags)
            .LoadAsync();

        // Delete messages
        _context.Messages.RemoveRange(topic.Messages);

        // Delete topic-tag relations
        var topicTags = await _context.TopicTags
            .Where(tt => tt.TopicId == topic.id)
            .ToListAsync();
        _context.TopicTags.RemoveRange(topicTags);

        // Delete topic
        _context.Topics.Remove(topic);
        await _context.SaveChangesAsync();
    }
}