using Microsoft.AspNetCore.Mvc;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services;

namespace Publisher.Controllers
{
    [ApiController]
    [Route("api/v1.0/[controller]")]
    public class StickersController : ControllerBase
    {
        private readonly StickerService _stickerService;

        public StickersController(StickerService stickerService)
        {
            _stickerService = stickerService;
        }

        [HttpPost]
        [ProducesResponseType(typeof(StickerResponseTo), StatusCodes.Status201Created)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public IActionResult CreateSticker([FromBody] StickerRequestTo stickerRequestTo)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var createdSticker = _stickerService.CreateSticker(stickerRequestTo);
                return CreatedAtAction(nameof(GetStickerById), new { id = createdSticker.Id }, createdSticker);
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

        [HttpGet("{id}")]//, Name = "GetStickerById")]
        [ProducesResponseType(typeof(StickerResponseTo), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public IActionResult GetStickerById(long id)
        {
            try
            {
                var sticker = _stickerService.GetStickerById(id);
                return sticker is not null ? Ok(sticker) : NotFound($"Sticker with ID {id} not found."); // 404 - not found, 200 - ok
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
        [ProducesResponseType(typeof(IEnumerable<StickerResponseTo>), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public IActionResult GetAllStickers()
        {
            try
            {
                var sticker = _stickerService.GetAllStickers();
                return Ok(sticker);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        [HttpPut]
        [ProducesResponseType(typeof(StickerResponseTo), StatusCodes.Status200OK)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public IActionResult UpdateSticker([FromBody] StickerRequestTo stickerRequestTo)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }              

                var updatedSticker = _stickerService.UpdateSticker(stickerRequestTo);
                return Ok(updatedSticker);
            }
            catch (Exception ex) when (ex is ArgumentException or
                             ArgumentNullException)
            {
                return BadRequest(ex.Message); // 400 - null/invalid Ids, empty login
            }
            catch (InvalidOperationException ex)
            {
                return Conflict(ex.Message); // 409
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
        public IActionResult DeleteSticker(long id)
        {
            try
            {
                bool isDeleted = _stickerService.DeleteSticker(id);
                if (!isDeleted)
                {
                    return NotFound(new ProblemDetails()
                    {
                        Title = "Not Found",
                        Detail = $"Reaction with ID {id} not found",
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

        [HttpGet("by-news/{newsId}")]
        [ProducesResponseType(typeof(IEnumerable<StickerResponseTo>), StatusCodes.Status200OK)]
        public IActionResult GetStickersByNewsId(long newsId)
        {
            try
            {
                var stickers = _stickerService.GetStickersByNewsId(newsId);
                return Ok(stickers);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(ex.Message);
            }
            catch (Exception)
            {
                return StatusCode(500);
            }
        }
    }
}