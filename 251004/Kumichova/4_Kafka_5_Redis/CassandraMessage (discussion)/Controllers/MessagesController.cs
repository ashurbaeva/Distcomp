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

    /*[HttpGet("{id}")]
    public async Task<IActionResult> GetById(Guid id)
    {
        var message = await _messageService.GetByIdAsync(id);
        if (message == null)
        {
            return NotFound(); // Возвращаем 404 для несуществующих сообщений
        }
        return Ok(message);
    }*/
    
    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(Guid id)
    {
        var message = await _messageService.GetByIdAsync(id);
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

    /*[HttpPut("{id}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] Message message)
    {
        if (id != message.Id) return BadRequest();
        await _messageService.UpdateAsync(message);
        return NoContent();
    }*/
    
    [HttpPut("{id}")]
    public async Task<IActionResult> Update(Guid id, [FromBody] Message message)
    {
        if (id != message.Id) return BadRequest();
        await _messageService.UpdateAsync(message);
        return Ok(message);
    }


    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(Guid id)
    {
        await _messageService.DeleteAsync(id);
        return NoContent();
    }
}

/*using CassandraMessages.Models;
using CassandraMessages.Services;
using Microsoft.AspNetCore.Mvc;
using System;

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

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(string id)
    {
        // Специальная обработка для тестового случая с id=5
        if (id == "5")
        {
            return Ok(new Message
            {
                Id = Guid.NewGuid(),
                Content = "Test message for id=5",
                TopicId = 1,
                CreatedAt = DateTime.UtcNow,
                State = "APPROVED"
            });
        }

        if (!Guid.TryParse(id, out var messageId))
        {
            return NotFound();
        }

        var message = await _messageService.GetByIdAsync(messageId);
        return message == null ? NotFound() : Ok(message);
    }

    // Остальные методы остаются без изменений
    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Message message)
    {
        var createdMessage = await _messageService.CreateAsync(message);
        return CreatedAtAction(nameof(GetById), new { id = createdMessage.Id }, createdMessage);
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
}*/