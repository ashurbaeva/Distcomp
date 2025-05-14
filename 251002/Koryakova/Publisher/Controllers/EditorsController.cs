using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services;

namespace Publisher.Controllers
{
    [ApiController]
    [Route("api/v1.0/[controller]")]
    public class EditorsController : ControllerBase
    {
        private readonly EditorService _editorService;

        public EditorsController(EditorService editorService)
        {
            _editorService = editorService;
        }

        [HttpPost]
        [ProducesResponseType(typeof(EditorResponseTo), StatusCodes.Status201Created)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status403Forbidden)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public IActionResult CreateEditor([FromBody] EditorRequestTo editorRequestTo)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var result = _editorService.CreateEditor(editorRequestTo);
                return CreatedAtAction(nameof(GetEditorById), new { id = result.Id }, result);
            }
            catch (InvalidOperationException ex) when (ex.Message == "Login already exists")
            {
                return StatusCode(403, new
                {
                    title = "Forbidden",
                    status = 403,
                    detail = ex.Message
                });
            }
            catch (Exception ex) when (ex is ArgumentException or
                              ArgumentNullException)
            {
                return BadRequest(ex.Message); // 400 - null/invalid Ids, empty login
            }

            catch(InvalidOperationException ex)
            {
                return Conflict(ex.Message); // 409 - login/id taken
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message + "Internal server error");
            }
        }

        [HttpGet("{id}", Name = "GetEditorById")]
        [ProducesResponseType(typeof(EditorResponseTo), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public IActionResult GetEditorById(long id)
        {
            try
            {
                var editor = _editorService.GetEditorById(id);
                return editor is not null ? Ok(editor) : NotFound($"Editor {id} not found."); // 404 - not found, 200 - ok
            }
            catch (ArgumentException ex)
            {
                return BadRequest(ex.Message); // 400 - invalid format
            }
            catch (Exception ex) 
            {
                return StatusCode(500, ex.Message); // 500 - unespected error
            }
        }

        [HttpGet]
        [ProducesResponseType(typeof(IEnumerable<EditorResponseTo>), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public IActionResult GetAllEditors()
        {
            try
            {
                var editors = _editorService.GetAllEditors();
                return Ok(editors);
           }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
           }
        }

        [HttpPut] //("{id}")]
        [ProducesResponseType(typeof(EditorResponseTo), StatusCodes.Status200OK)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public IActionResult UpdateEditor([FromBody] EditorRequestTo editorRequestTo)
        {
            try
            {
                if(!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                /*if(id != null)
                {
                    editorRequestTo.Id = id;
                }*/
                /*
                if(id != editorRequestTo.Id)
                {
                    return BadRequest("Route ID and DTO ID must match."); // 400
                }
                */
                var result = _editorService.UpdateEditor(editorRequestTo);
                return Ok(result);
            }
            catch (Exception ex) when (ex is ArgumentException or
                             ArgumentNullException)
            {
                return BadRequest(ex.Message); // 400 - null/invalid Ids, empty login
            }
            catch (InvalidOperationException ex) when (ex.Message.Contains("not found"))
            {
                return NotFound(ex.Message); // 404 - not found
            }
            catch (InvalidOperationException ex) when (ex.Message.Contains("taken"))
            {
                return Conflict(ex.Message); // 409 - login/id taken
            }
            catch (InvalidOperationException ex)
            {
                return Conflict(ex.Message);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message + "Internal server error");
            }
        }

        [HttpDelete("{id}")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]       // Success
        [ProducesResponseType(StatusCodes.Status400BadRequest)]      // Invalid ID (e.g., negative)
        [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status404NotFound)]         // ID not found
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] // Unexpected
        public IActionResult DeleteEditor(long id)
        {
            try
            {
                bool isDeleted = _editorService.DeleteEditor(id);
                if(!isDeleted)
                {
                    return NotFound(new ProblemDetails()
                    {
                        Title = "Not Found",
                        Detail = $"Editor with ID {id} not found",
                        Status = 404
                    }); // 404
                }
                return NoContent(); // 204 successful delete
            }
            catch (ArgumentException ex)
            {
                return BadRequest(ex.Message); // 400 (invalid ID)
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Internal server error, {ex.Message}"); // Fallback
            }
        }

        [HttpGet("by-news/{newsId}")]
        [ProducesResponseType(typeof(EditorResponseTo), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public IActionResult GetEditorByNewsId(long newsId)
        {
            try
            {
                var editor = _editorService.GetEditorByNewsId(newsId);
                return Ok(editor);
            }
            catch (InvalidOperationException ex)
            {
                return NotFound(ex.Message);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }
}