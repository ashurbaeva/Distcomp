using CassandraMessages.Models;
using CassandraMessages.Services;
using Microsoft.AspNetCore.Mvc;

namespace CassandraMessages.Controllers;

[ApiController]
[Route("api/v1.0/messages")]
public class MessagesController : ControllerBase
{
    private readonly MessageService _messageService;

    public MessagesController(MessageService messageService)
    {
        _messageService = messageService;
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Message message)
    {
        var createdMessage = await _messageService.CreateAsync(message);
        return CreatedAtAction(nameof(GetById), new { id = createdMessage.Id }, createdMessage);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(string id)
    {
        var message = await _messageService.GetAllAsync(10);
        return message == null ? NotFound() : Ok(message);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll(int pageSize = 10)
    {
        var messages = await _messageService.GetAllAsync(pageSize);
        return Ok(messages);
    }

    [HttpGet("by-topic/{topicId}")]
    public async Task<IActionResult> GetByTopicId(int topicId)
    {
        var messages = await _messageService.GetByTopicIdAsync(topicId);
        return Ok(messages);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] Message message)
    {
        if (id != message.Id) return BadRequest();
        await _messageService.UpdateAsync(message);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(Guid id)
    {
        await _messageService.DeleteAsync(id);
        return NoContent();
    }
}