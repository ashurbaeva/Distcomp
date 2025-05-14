using LAB1.Domain;
using LAB1.DTOs;
using LAB1.Interfaces;

namespace LAB1.Services;

public class TagService
{
    private readonly IRepository<Tag> _repository;

    public TagService(IRepository<Tag> repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<TagResponseTo>> GetAllAsync()
    {
        var tags = await _repository.GetAllAsync();
        return tags.Select(t => new TagResponseTo
        {
            Id = t.Id,
            Name = t.Name
        });
    }

    public async Task<TagResponseTo?> GetByIdAsync(int id)
    {
        var tag = await _repository.GetByIdAsync(id);
        if (tag == null) return null;
        
        return new TagResponseTo
        {
            Id = tag.Id,
            Name = tag.Name
        };
    }

    public async Task<TagResponseTo> CreateAsync(TagRequestTo tagRequest)
    {
        if (string.IsNullOrEmpty(tagRequest.Name))
            throw new ArgumentException("Name is required");

        var tag = new Tag
        {
            Name = tagRequest.Name
        };

        await _repository.CreateAsync(tag);
        
        return new TagResponseTo
        {
            Id = tag.Id,
            Name = tag.Name
        };
    }

    public async Task<TagResponseTo?> UpdateAsync(int id, TagRequestTo tagRequest)
    {
        var tag = await _repository.GetByIdAsync(id);
        if (tag == null) return null;

        if (string.IsNullOrEmpty(tagRequest.Name))
            throw new ArgumentException("Name is required");

        tag.Name = tagRequest.Name;

        await _repository.UpdateAsync(tag);
        
        return new TagResponseTo
        {
            Id = tag.Id,
            Name = tag.Name
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}