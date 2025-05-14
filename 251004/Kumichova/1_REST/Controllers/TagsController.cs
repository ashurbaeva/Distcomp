using LAB1.DTOs;
using LAB1.Services;
using Microsoft.AspNetCore.Mvc;

namespace LAB1.Controllers;

[Route("api/v1.0/tags")]
[ApiController]
public class TagsController : ControllerBase
{
    private readonly TagService _tagService;

    public TagsController(TagService tagService)
    {
        _tagService = tagService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<TagResponseTo>>> GetTags()
    {
        var tags = await _tagService.GetAllAsync();
        return Ok(tags);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<TagResponseTo>> GetTag(int id)
    {
        var tag = await _tagService.GetByIdAsync(id);
        if (tag == null) return NotFound();
        return Ok(tag);
    }

    [HttpPost]
    public async Task<ActionResult<TagResponseTo>> CreateTag([FromBody] TagRequestTo tagRequest)
    {
        try
        {
            var tag = await _tagService.CreateAsync(tagRequest);
            return CreatedAtAction(nameof(GetTag), new { id = tag.Id }, tag);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpPut("{id}")]
    public async Task<ActionResult<TagResponseTo>> UpdateTag(int id, [FromBody] TagRequestTo tagRequest)
    {
        try
        {
            var tag = await _tagService.UpdateAsync(id, tagRequest);
            if (tag == null) return NotFound();
            return Ok(tag);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteTag(int id)
    {
        var result = await _tagService.DeleteAsync(id);
        if (!result) return NotFound();
        return NoContent();
    }
}