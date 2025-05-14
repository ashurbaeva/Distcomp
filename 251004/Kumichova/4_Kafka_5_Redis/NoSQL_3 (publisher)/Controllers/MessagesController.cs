using LAB2.DTOs;
using LAB2.Interfaces;
using LAB2.Services;
using Microsoft.AspNetCore.Mvc;

namespace LAB2.Controllers;

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
    public async Task<ActionResult<IEnumerable<MessageResponseTo>>> GetMessages([FromQuery] QueryParams queryParams)
    {
        var messages = await _messageService.GetAllAsync(queryParams);
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
            return BadRequest(new { errorCode = 40004, errorMessage = ex.Message });
        }
    }

    [HttpPut]
    public async Task<ActionResult<MessageResponseTo>> UpdateMessage([FromBody] MessageRequestTo messageRequest)
    {
        try
        {
            var message = await _messageService.UpdateAsync(messageRequest);
            if (message == null) return NotFound();
            return Ok(message);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { errorCode = 40004, errorMessage = ex.Message });
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