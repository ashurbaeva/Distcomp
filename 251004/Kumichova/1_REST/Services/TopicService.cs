using LAB1.Domain;
using LAB1.DTOs;
using LAB1.Interfaces;

namespace LAB1.Services;

public class TopicService
{
    private readonly IRepository<Topic> _repository;

    public TopicService(IRepository<Topic> repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<TopicResponseTo>> GetAllAsync()
    {
        var topics = await _repository.GetAllAsync();
        return topics.Select(t => new TopicResponseTo
        {
            Id = t.Id,
            Title = t.Title,
            Content = t.Content,
            UserId = t.UserId
        });
    }

    public async Task<TopicResponseTo?> GetByIdAsync(int id)
    {
        var topic = await _repository.GetByIdAsync(id);
        if (topic == null) return null;
        
        return new TopicResponseTo
        {
            Id = topic.Id,
            Title = topic.Title,
            Content = topic.Content,
            UserId = topic.UserId
        };
    }

    public async Task<TopicResponseTo> CreateAsync(TopicRequestTo topicRequest)
    {
        if (string.IsNullOrEmpty(topicRequest.Title))
            throw new ArgumentException("Title is required");
        if (topicRequest.UserId <= 0)
            throw new ArgumentException("Invalid UserId");

        var topic = new Topic
        {
            Title = topicRequest.Title,
            Content = topicRequest.Content,
            UserId = topicRequest.UserId
        };

        await _repository.CreateAsync(topic);
        
        return new TopicResponseTo
        {
            Id = topic.Id,
            Title = topic.Title,
            Content = topic.Content,
            UserId = topic.UserId
        };
    }

    public async Task<TopicResponseTo?> UpdateAsync(TopicRequestTo topicRequest)
    {
        var topic = await _repository.GetByIdAsync(topicRequest.Id);
        if (topic == null) return null;

        if (string.IsNullOrEmpty(topicRequest.Title))
            throw new ArgumentException("Title is required");
        if (topicRequest.UserId <= 0)
            throw new ArgumentException("Invalid UserId");
        if(topicRequest.Title.Length < 2 || topicRequest.Title.Length > 64)
            throw new ArgumentException("Invalid title");

        topic.Title = topicRequest.Title;
        topic.Content = topicRequest.Content;
        topic.UserId = topicRequest.UserId;

        await _repository.UpdateAsync(topic);
        
        return new TopicResponseTo
        {
            Id = topic.Id,
            Title = topic.Title,
            Content = topic.Content,
            UserId = topic.UserId
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}