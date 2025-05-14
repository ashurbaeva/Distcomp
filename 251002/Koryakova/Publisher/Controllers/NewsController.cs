using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using Publisher.Data;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entities;
using Publisher.Services;
using Publisher.Storage;

namespace Publisher.Controllers
{
    [ApiController]
    [Route("api/v1.0/[controller]")]
    public class NewsController : ControllerBase
    {
        private readonly NewsService _newsService;

        public NewsController(NewsService newsService)
        {
            _newsService = newsService;
        }

        [HttpPost]
        [ProducesResponseType(typeof(NewsResponseTo), StatusCodes.Status201Created)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public IActionResult CreateNews([FromBody] NewsRequestTo newsRequestTo)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var createdNews = _newsService.CreateNews(newsRequestTo);
                return CreatedAtAction(nameof(GetNewsById), new { id = createdNews.Id }, createdNews);
            }
            catch (InvalidOperationException ex) when (ex.Message.Contains("Duplicate"))
            {
                return StatusCode(403, new ProblemDetails
                {
                    Title = "Duplicate Content",
                    Detail = ex.Message,
                    Status = 403
                });
            }
            catch (DbUpdateException ex) when (ex.InnerException is PostgresException pgEx && pgEx.SqlState == "23505")
            {
                // Fallback constraint violation
                return StatusCode(403, "Duplicate title detected");
            }
            catch (InvalidOperationException ex)
            {
                return Conflict(ex.Message); // 409
            }
            catch (Exception ex) when (ex is ArgumentException or
                             ArgumentNullException)
            {
                return BadRequest(ex.Message); // 400 - null/invalid Ids, empty login
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message + " Internal server error");
            }
        }

        [HttpGet("{id}", Name = "GetNewsById")]
        [ProducesResponseType(typeof(NewsResponseTo), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public IActionResult GetNewsById(long id)
        {
            try
            {
                var news = _newsService.GetNewsById(id);
                return news is not null ? Ok(news) : NotFound($"News with ID {id} not found."); // 404 - not found, 200 - ok
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
        [ProducesResponseType(typeof(IEnumerable<NewsResponseTo>), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public IActionResult GetAllNews()
        {
            try
            {
                var news = _newsService.GetAllNews();
                return Ok(news);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        [HttpPut]//("{id}")]
        [ProducesResponseType(typeof(NewsResponseTo), StatusCodes.Status200OK)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public IActionResult UpdateNews([FromBody] NewsRequestTo newsRequestTo)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                /*if (id != newsRequestTo.Id)
                {
                    return BadRequest("Route ID and DTO ID must match."); // 400
                }*/
                //newsRequestTo.Id = id;
                var updatedNews = _newsService.UpdateNews(newsRequestTo);
                return Ok(updatedNews);
            }
            catch (Exception ex) when (ex is ArgumentException or
                             ArgumentNullException)
            {
                return BadRequest(ex.Message); // 400 - null/invalid Ids, empty login
            }
            catch (InvalidOperationException ex)
            {
                return Conflict(ex.Message); // 409 - login/id taken
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
        public IActionResult DeleteNews(long id)
        {
            try
            {
                bool isDeleted = _newsService.DeleteNews(id);
                if (!isDeleted)
                {
                    return NotFound(new ProblemDetails()
                    {
                        Title = "Not Found",
                        Detail = $"News with ID {id} not found",
                        Status = 404
                    });
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

        [HttpGet("search")]
        [ProducesResponseType(typeof(IEnumerable<NewsResponseTo>), 200)]
        public IActionResult SearchNews(
        [FromQuery] string? stickerName,
        [FromQuery] long? stickerId,
        [FromQuery] string? editorLogin,
        [FromQuery] string? title,
        [FromQuery] string? content)
        {
            var searchRequest = new NewsSearchRequestDto
            {
                StickerName = stickerName,
                StickerId = stickerId,
                EditorLogin = editorLogin,
                Title = title,
                Content = content
            };

            try
            {
                var news = _newsService.SearchNews(searchRequest);
                return Ok(news);
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal server error" + ex.Message);
            }
        }
    }
}
