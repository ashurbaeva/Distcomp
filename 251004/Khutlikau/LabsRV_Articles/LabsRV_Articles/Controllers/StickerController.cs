using LabsRV_Articles.Models.DTO;
using LabsRV_Articles.Services;
using Microsoft.AspNetCore.Mvc;

namespace LabsRV_Articles.Controllers
{
    [ApiController]
    [Route("api/v1.0/stickers")]
    public class StickerController : ControllerBase
    {
        private readonly StickerService _stickerService;

        public StickerController(StickerService stickerService)
        {
            _stickerService = stickerService;
        }

        [HttpPost]
        public IActionResult Create([FromBody] StickerRequestDto request)
        {
            var response = _stickerService.Create(request);
            return CreatedAtAction(nameof(GetById), new { id = response.Id }, response);
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            var response = _stickerService.GetAll();
            return Ok(response);
        }

        [HttpGet("{id}")]
        public IActionResult GetById(int id)
        {
            var response = _stickerService.GetById(id);
            return Ok(response);
        }

        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] StickerRequestDto request)
        {
            var response = _stickerService.Update(id, request);
            return Ok(response);
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            _stickerService.Delete(id);
            return NoContent();
        }
    }
}
