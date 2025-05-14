using System.Linq.Expressions;
using LAB1.DTOs;
using LAB1.Services;
using Microsoft.AspNetCore.Mvc;

namespace LAB1.Controllers;

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
    public async Task<ActionResult<IEnumerable<TopicResponseTo>>> GetTopics()
    {
        var topics = await _topicService.GetAllAsync();
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
            return BadRequest(ex.Message);
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
            //return BadRequest(ex.Message);
            //return BadRequest(new TopicResponseTo());
            return BadRequest();
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