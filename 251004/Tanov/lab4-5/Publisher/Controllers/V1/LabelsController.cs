﻿using Publisher.DTO.RequestDTO;
using Publisher.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Publisher.Controllers;

[ApiController]
[Route("api/v1.0/[controller]")]
public class LabelsController : ControllerBase
{
    private readonly ILabelService _markService;

    public LabelsController(ILabelService markService)
    {
        _markService = markService;
    }

    [HttpGet]
    public async Task<IActionResult> GetMarks()
    {
        var marks = await _markService.GetMarksAsync();
        return Ok(marks);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetMarkById(long id)
    {
        var mark = await _markService.GetMarkByIdAsync(id);
        return Ok(mark);
    }

    [HttpPost]
    public async Task<IActionResult> CreateMark([FromBody] LabelRequestDTO mark)
    {
        var createdMark = await _markService.CreateMarkAsync(mark);
        return CreatedAtAction(nameof(CreateMark), new { id = createdMark.Id }, createdMark);
    }

    [HttpPut]
    public async Task<IActionResult> UpdateMark([FromBody] LabelRequestDTO mark)
    {
        var updatedMark = await _markService.UpdateMarkAsync(mark);
        return Ok(updatedMark);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteMark(long id)
    {
        await _markService.DeleteMarkAsync(id);
        return NoContent();
    }
}