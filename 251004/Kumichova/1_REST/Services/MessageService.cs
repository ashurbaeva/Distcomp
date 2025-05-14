using LAB1.Domain;
using LAB1.DTOs;
using LAB1.Interfaces;

namespace LAB1.Services;

public class MessageService
{
    private readonly IRepository<Message> _repository;

    public MessageService(IRepository<Message> repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<MessageResponseTo>> GetAllAsync()
    {
        var messages = await _repository.GetAllAsync();
        return messages.Select(m => new MessageResponseTo
        {
            Id = m.Id,
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
            Id = message.Id,
            Content = message.Content,
            TopicId = message.TopicId
        };
    }

    public async Task<MessageResponseTo> CreateAsync(MessageRequestTo messageRequest)
    {
        if (string.IsNullOrEmpty(messageRequest.Content))
            throw new ArgumentException("Content is required");
        if (messageRequest.TopicId <= 0)
            throw new ArgumentException("Invalid TopicId");

        var message = new Message
        {
            Content = messageRequest.Content,
            TopicId = messageRequest.TopicId
        };

        await _repository.CreateAsync(message);
        
        return new MessageResponseTo
        {
            Id = message.Id,
            Content = message.Content,
            TopicId = message.TopicId
        };
    }

    public async Task<MessageResponseTo?> UpdateAsync(int id, MessageRequestTo messageRequest)
    {
        var message = await _repository.GetByIdAsync(id);
        if (message == null) return null;

        if (string.IsNullOrEmpty(messageRequest.Content))
            throw new ArgumentException("Content is required");
        if (messageRequest.TopicId <= 0)
            throw new ArgumentException("Invalid TopicId");

        message.Content = messageRequest.Content;
        message.TopicId = messageRequest.TopicId;

        await _repository.UpdateAsync(message);
        
        return new MessageResponseTo
        {
            Id = message.Id,
            Content = message.Content,
            TopicId = message.TopicId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}