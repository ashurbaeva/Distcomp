using LabsRV_Articles.Models.DTO;
using LabsRV_Articles.Services;
using Microsoft.AspNetCore.Mvc;

namespace LabsRV_Articles.Controllers
{
    [ApiController]
    [Route("api/v1.0/comments")]
    public class CommentProxyController : ControllerBase
    {
        private readonly RemoteCommentService _remoteCommentService;
        
        public CommentProxyController(RemoteCommentService remoteCommentService)
        {
            _remoteCommentService = remoteCommentService;
        }


        [HttpPost]
        public async Task<IActionResult> Create([FromBody] CommentRequestDto request)
        {
            var createdComment = await _remoteCommentService.CreateAsync(request);
            return CreatedAtAction(nameof(GetById), new { id = createdComment.Id}, createdComment);
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var comments = await _remoteCommentService.GetAllAsync();
            return Ok(comments);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var comment = await _remoteCommentService.GetByIdAsync(id);
            return Ok(comment);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, [FromBody] CommentRequestDto request)
        {
            var comment = await _remoteCommentService.UpdateAsync(id, request);
            return Ok(comment);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            await _remoteCommentService.DeleteAsync(id);
            return NoContent();
        }
    }
}
