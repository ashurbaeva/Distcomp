using LAB1.DTOs;
using LAB1.Services;
using Microsoft.AspNetCore.Mvc;

namespace LAB1.Controllers;

[Route("api/v1.0/messages")]
[ApiController]
public class MessagesController : ControllerBase
{
    private readonly MessageService _messageService;

    public MessagesController(MessageService messageService)
    {
        _messageService = messageService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<MessageResponseTo>>> GetMessages()
    {
        var messages = await _messageService.GetAllAsync();
        return Ok(messages);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<MessageResponseTo>> GetMessage(int id)
    {
        var message = await _messageService.GetByIdAsync(id);
        if (message == null) return NotFound();
        return Ok(message);
    }

    [HttpPost]
    public async Task<ActionResult<MessageResponseTo>> CreateMessage([FromBody] MessageRequestTo messageRequest)
    {
        try
        {
            var message = await _messageService.CreateAsync(messageRequest);
            return CreatedAtAction(nameof(GetMessage), new { id = message.Id }, message);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpPut("{id}")]
    public async Task<ActionResult<MessageResponseTo>> UpdateMessage(int id, [FromBody] MessageRequestTo messageRequest)
    {
        try
        {
            var message = await _messageService.UpdateAsync(id, messageRequest);
            if (message == null) return NotFound();
            return Ok(message);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMessage(int id)
    {
        var result = await _messageService.DeleteAsync(id);
        if (!result) return NotFound();
        return NoContent();
    }
}