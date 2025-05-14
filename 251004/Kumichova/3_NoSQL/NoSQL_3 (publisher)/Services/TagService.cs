using LAB2.Data;
using LAB2.Domain;
using LAB2.DTOs;
using LAB2.Interfaces;

namespace LAB2.Services;

public class TagService
{
    private readonly IRepository<Tag> _repository;
    private readonly AppDbContext _context;

    public TagService(IRepository<Tag> repository, AppDbContext context)
    {
        _repository = repository;
        _context = context;
    }

    public async Task<IEnumerable<TagResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var tags = await _repository.GetAllAsync(queryParams);
        return tags.Select(t => new TagResponseTo
        {
            Id = t.id,
            Name = t.Name
        });
    }

    public async Task<TagResponseTo?> GetByIdAsync(int id)
    {
        var tag = await _repository.GetByIdAsync(id);
        if (tag == null) return null;
            
        return new TagResponseTo
        {
            Id = tag.id,
            Name = tag.Name
        };
    }

    public async Task<TagResponseTo> CreateAsync(TagRequestTo tagRequest)
    {
        if (string.IsNullOrEmpty(tagRequest.Name))
            throw new ArgumentException("Name is required");
        if (tagRequest.Name.Length < 2 || tagRequest.Name.Length > 32)
            throw new ArgumentException("Name must be between 2-32 characters");

        var tag = new Tag
        {
            Name = tagRequest.Name
        };

        await _repository.CreateAsync(tag);
            
        return new TagResponseTo
        {
            Id = tag.id,
            Name = tag.Name
        };
    }

    public async Task<TagResponseTo?> UpdateAsync(TagRequestTo tagRequest)
    {
        if (string.IsNullOrEmpty(tagRequest.Name))
            throw new ArgumentException("Name is required");
        if (tagRequest.Name.Length < 2 || tagRequest.Name.Length > 32)
            throw new ArgumentException("Name must be between 2-32 characters");

        var tag = await _repository.GetByIdAsync(tagRequest.Id);
        if (tag == null) return null;

        tag.Name = tagRequest.Name;

        await _repository.UpdateAsync(tag);
            
        return new TagResponseTo
        {
            Id = tag.id,
            Name = tag.Name
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var tag = await _repository.GetByIdAsync(id);
        if (tag == null) return false;

        // Load related topics
        await _context.Entry(tag)
            .Collection(t => t.Topics)
            .LoadAsync();

        // Remove tag from all topics
        foreach (var topic in tag.Topics.ToList())
        {
            topic.Tags.Remove(tag);
        }

        return await _repository.DeleteAsync(id);
    }
}