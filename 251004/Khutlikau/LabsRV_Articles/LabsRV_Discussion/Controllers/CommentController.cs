using LabsRV_Discussion.Models.DTO;
using LabsRV_Discussion.Services;
using Microsoft.AspNetCore.Mvc;

namespace LabsRV_Discussion.Controllers;

[ApiController]
[Route("api/v1.0/comments")]
public class CommentController : ControllerBase
{
    private readonly CommentService _service;

    public CommentController(CommentService service)
    {
        _service = service;
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] CommentRequestDto request)
    {
        var response = await _service.CreateAsync(request);
        return CreatedAtAction(nameof(GetById), new { id = response.Id }, response);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var response = await _service.GetByIdAsync(id);
        return Ok(response);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var response = await _service.GetAllAsync();
        return Ok(response);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        await _service.DeleteAsync(id);
        return NoContent();
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, [FromBody] CommentRequestDto request)
    {
        var response = await _service.UpdateAsync(id, request);
        return Ok(response);
    }
}
