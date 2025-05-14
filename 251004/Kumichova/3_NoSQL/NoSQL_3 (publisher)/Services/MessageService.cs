using LAB2.Data;
using LAB2.Domain;
using LAB2.DTOs;
using LAB2.Interfaces;

namespace LAB2.Services;

public class MessageService
{
    private readonly IRepository<Message> _repository;
    private readonly IRepository<Topic> _topicRepository;
    private readonly AppDbContext _context;

    public MessageService(
        IRepository<Message> repository,
        IRepository<Topic> topicRepository,
        AppDbContext context)
    {
        _repository = repository;
        _topicRepository = topicRepository;
        _context = context;
    }

    public async Task<IEnumerable<MessageResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var messages = await _repository.GetAllAsync(queryParams);
        return messages.Select(m => new MessageResponseTo
        {
            Id = m.id,
            Content = m.Content,
            TopicId = m.TopicId
        });
    }

    public async Task<MessageResponseTo?> GetByIdAsync(int id)
    {
        var message = await _repository.GetByIdAsync(id);
        if (message == null) return null;
            
        return new MessageResponseTo
        {
            Id = message.id,
            Content = message.Content,
            TopicId = message.TopicId
        };
    }

    public async Task<MessageResponseTo> CreateAsync(MessageRequestTo messageRequest)
    {
        if (string.IsNullOrEmpty(messageRequest.Content))
            throw new ArgumentException("Content is required");
        if (messageRequest.Content.Length < 2 || messageRequest.Content.Length > 2048)
            throw new ArgumentException("Content must be between 2-2048 characters");
        if (messageRequest.TopicId <= 0)
            throw new ArgumentException("Invalid TopicId");

        var topic = await _topicRepository.GetByIdAsync(messageRequest.TopicId);
        if (topic == null)
            throw new ArgumentException("Topic not found");

        var message = new Message
        {
            Content = messageRequest.Content,
            TopicId = messageRequest.TopicId
        };

        await _repository.CreateAsync(message);
            
        return new MessageResponseTo
        {
            Id = message.id,
            Content = message.Content,
            TopicId = message.TopicId
        };
    }

    public async Task<MessageResponseTo?> UpdateAsync(MessageRequestTo messageRequest)
    {
        if (string.IsNullOrEmpty(messageRequest.Content))
            throw new ArgumentException("Content is required");
        if (messageRequest.Content.Length < 2 || messageRequest.Content.Length > 2048)
            throw new ArgumentException("Content must be between 2-2048 characters");
        if (messageRequest.TopicId <= 0)
            throw new ArgumentException("Invalid TopicId");

        var topic = await _topicRepository.GetByIdAsync(messageRequest.TopicId);
        if (topic == null)
            throw new ArgumentException("Topic not found");

        var message = await _repository.GetByIdAsync(messageRequest.Id);
        if (message == null) return null;

        message.Content = messageRequest.Content;
        message.TopicId = messageRequest.TopicId;

        await _repository.UpdateAsync(message);
            
        return new MessageResponseTo
        {
            Id = message.id,
            Content = message.Content,
            TopicId = message.TopicId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}