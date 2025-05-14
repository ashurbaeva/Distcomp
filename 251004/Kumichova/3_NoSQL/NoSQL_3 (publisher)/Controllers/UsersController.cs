using LAB2.DTOs;
using LAB2.Interfaces;
using LAB2.Services;
using Microsoft.AspNetCore.Mvc;

namespace LAB2.Controllers;

[Route("api/v1.0/users")]
[ApiController]
public class UsersController : ControllerBase
{
    private readonly UserService _userService;

    public UsersController(UserService userService)
    {
        _userService = userService;
    }

    [HttpGet]
    public async Task<ActionResult<IEnumerable<UserResponseTo>>> GetUsers([FromQuery] QueryParams queryParams)
    {
        var users = await _userService.GetAllAsync(queryParams);
        return Ok(users);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<UserResponseTo>> GetUser(int id)
    {
        var user = await _userService.GetByIdAsync(id);
        if (user == null) return NotFound();
        return Ok(user);
    }

    [HttpPost]
    public async Task<ActionResult<UserResponseTo>> CreateUser([FromBody] UserRequestTo userRequest)
    {
        try
        {
            var user = await _userService.CreateAsync(userRequest);
            return CreatedAtAction(nameof(GetUser), new { id = user.Id }, user);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { errorCode = 40001, errorMessage = ex.Message });
        }
        catch (InvalidOperationException ex)
        {
            return StatusCode(StatusCodes.Status403Forbidden); // 409 Conflict
            //return Conflict(new { errorCode = 40301, errorMessage = ex.Message });
        }
    }

    [HttpPut]
    public async Task<ActionResult<UserResponseTo>> UpdateUser([FromBody] UserRequestTo userRequest)
    {
        try
        {
            var user = await _userService.UpdateAsync(userRequest);
            if (user == null) return NotFound();
            return Ok(user);
        }
        catch (ArgumentException ex)
        {
            return BadRequest(new { errorCode = 40001, errorMessage = ex.Message });
        }
        catch (InvalidOperationException ex)
        {
            return Conflict(new { errorCode = 40901, errorMessage = ex.Message });
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteUser(int id)
    {
        var result = await _userService.DeleteAsync(id);
        if (!result) return NotFound();
        return NoContent();
    }
}