using LAB2.Data;
using LAB2.Domain;
using LAB2.DTOs;
using LAB2.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace LAB2.Services;

public class TopicService
{
    private readonly IRepository<Topic> _repository;
    private readonly IRepository<User> _userRepository;
    private readonly IRepository<Tag> _tagRepository;
    private readonly AppDbContext _context;

    public TopicService(
        IRepository<Topic> repository,
        IRepository<User> userRepository,
        IRepository<Tag> tagRepository,
        AppDbContext context)
    {
        _repository = repository;
        _userRepository = userRepository;
        _tagRepository = tagRepository;
        _context = context;
    }

    public async Task<IEnumerable<TopicResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var topics = await _repository.GetAllAsync(queryParams);
        return topics.Select(t => new TopicResponseTo
        {
            Id = t.id,
            Title = t.Title,
            Content = t.Content,
            //Created = t.Created,
            //Modified = t.Modified,
            UserId = t.UserId
        });
    }

    public async Task<TopicResponseTo?> GetByIdAsync(int id)
    {
        var topic = await _repository.GetByIdAsync(id);
        if (topic == null) return null;
            
        return new TopicResponseTo
        {
            Id = topic.id,
            Title = topic.Title,
            Content = topic.Content,
            //Created = topic.Created,
            //Modified = topic.Modified,
            UserId = topic.UserId
        };
    }

    private async Task<bool> IsTitleUniqueAsync(string title, int? id = null)
    {
        var topics = await _repository.GetAllAsync();
        return !topics.Any(w => w.Title == title && (id == null || w.id != id));
    }
    
    public async Task<TopicResponseTo> CreateAsync(TopicRequestTo topicRequest)
    {
        if (string.IsNullOrEmpty(topicRequest.Title))
            throw new ArgumentException("Title is required");
        if (topicRequest.Title.Length < 2 || topicRequest.Title.Length > 64)
            throw new ArgumentException("Title must be between 2-64 characters");
        if (string.IsNullOrEmpty(topicRequest.Content))
            throw new ArgumentException("Content is required");
        if (topicRequest.Content.Length < 4 || topicRequest.Content.Length > 2048)
            throw new ArgumentException("Content must be between 4-2048 characters");
        if (topicRequest.UserId <= 0)
            throw new ArgumentException("Invalid UserId");

        var user = await _userRepository.GetByIdAsync(topicRequest.UserId);
        if (user == null)
            throw new ArgumentException("User not found");
        
        if (!await IsTitleUniqueAsync(topicRequest.Title))
        {
            throw new InvalidOperationException("Login must be unique.");
        }

        var topic = new Topic
        {
            Title = topicRequest.Title,
            Content = topicRequest.Content,
            UserId = topicRequest.UserId,
            Created = DateTime.UtcNow,
            Modified = DateTime.UtcNow
        };
        
        if (topic.Tags != null)
        {
            foreach (var tagName in topicRequest.Tags)
            {
                // Ищем стикер по имени или создаем новый
                var tag = await _tagRepository.GetQueryable().FirstOrDefaultAsync(s => s.Name == tagName);
                    
                if (tag == null)
                {
                    tag = new Tag() { Name = tagName };
                    await _tagRepository.CreateAsync(tag); // Сохраняем стикер в базе данных
                }
                
                
                // Добавляем связь Issue-Sticker
                topic.Tags.Add(tag);
            }
        }

        await _repository.CreateAsync(topic);
            
        return new TopicResponseTo
        {
            Id = topic.id,
            Title = topic.Title,
            Content = topic.Content,
            //Created = topic.Created,
            //Modified = topic.Modified,
            UserId = topic.UserId
        };
    }

    public async Task<TopicResponseTo?> UpdateAsync(TopicRequestTo topicRequest)
    {
        if (string.IsNullOrEmpty(topicRequest.Title))
            throw new ArgumentException("Title is required");
        if (topicRequest.Title.Length < 2 || topicRequest.Title.Length > 64)
            throw new ArgumentException("Title must be between 2-64 characters");
        if (string.IsNullOrEmpty(topicRequest.Content))
            throw new ArgumentException("Content is required");
        if (topicRequest.Content.Length < 4 || topicRequest.Content.Length > 2048)
            throw new ArgumentException("Content must be between 4-2048 characters");
        if (topicRequest.UserId <= 0)
            throw new ArgumentException("Invalid UserId");

        var user = await _userRepository.GetByIdAsync(topicRequest.UserId);
        if (user == null)
            throw new ArgumentException("User not found");

        var topic = await _repository.GetByIdAsync(topicRequest.Id);
        if (topic == null) return null;

        topic.Title = topicRequest.Title;
        topic.Content = topicRequest.Content;
        topic.UserId = topicRequest.UserId;
        topic.Modified = DateTime.UtcNow;

        await _repository.UpdateAsync(topic);
            
        return new TopicResponseTo
        {
            Id = topic.id,
            Title = topic.Title,
            Content = topic.Content,
            //Created = topic.Created,
            //Modified = topic.Modified,
            UserId = topic.UserId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var topic = await _repository.GetByIdAsync(id);
        if (topic == null) return false;

        await DeleteTopicWithRelations(topic);
        return true;
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
        _context.Tags.RemoveRange(topic.Tags);

        // Delete topic
        _context.Topics.Remove(topic);
        await _context.SaveChangesAsync();
    }
}