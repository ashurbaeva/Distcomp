using LAB2.DTOs;
using LAB2.Interfaces;
using LAB2.Services;
using Microsoft.AspNetCore.Mvc;

namespace LAB2.Controllers;

[Route("api/v1.0/topics")]
[ApiController]
public class TopicsController : ControllerBase
{
    private readonly TopicService _topicService;

    public TopicsController(TopicService topicService)
    {
        _topicService = topicService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<TopicResponseTo>>> GetTopics([FromQuery] QueryParams queryParams)
    {
        var topics = await _topicService.GetAllAsync(queryParams);
        return Ok(topics);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<TopicResponseTo>> GetTopic(int id)
    {
        var topic = await _topicService.GetByIdAsync(id);
        if (topic == null) return NotFound();
        return Ok(topic);
    }

    [HttpPost]
    public async Task<ActionResult<TopicResponseTo>> CreateTopic([FromBody] TopicRequestTo topicRequest)
    {
        try
        {
            var topic = await _topicService.CreateAsync(topicRequest);
            return CreatedAtAction(nameof(GetTopic), new { id = topic.Id }, topic);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { errorCode = 40002, errorMessage = ex.Message });
        }
        catch (InvalidOperationException ex)
        {
            return StatusCode(StatusCodes.Status403Forbidden); // 409 Conflict
            //return Conflict(new { errorCode = 40302, errorMessage = ex.Message });
        }
    }

    [HttpPut]
    public async Task<ActionResult<TopicResponseTo>> UpdateTopic([FromBody] TopicRequestTo topicRequest)
    {
        try
        {
            var topic = await _topicService.UpdateAsync(topicRequest);
            if (topic == null) return NotFound();
            return Ok(topic);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { errorCode = 40002, errorMessage = ex.Message });
        }
        catch (InvalidOperationException ex)
        {
            return Conflict(new { errorCode = 40902, errorMessage = ex.Message });
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteTopic(int id)
    {
        var result = await _topicService.DeleteAsync(id);
        if (!result) return NotFound();
        return NoContent();
    }
}