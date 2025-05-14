using Microsoft.AspNetCore.Mvc;
using Publisher.Kafka;
using Shared.Models.DTOs.Requests;

namespace Publisher.Controllers
{
    [ApiController]
    [Route("api/v1.0/Reactions")]
    public class ReactionsController : ControllerBase
    {
        private readonly PublisherProducer _producer;

        public ReactionsController(PublisherProducer producer)
        {
            _producer = producer;
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] ReactionRequestTo request)
        {
            await _producer.SendReactionCreateAsync(request);
            return Accepted("Reaction creation request sent.");
        }

        [HttpPut]
        public async Task<IActionResult> Update([FromBody] ReactionRequestTo request)
        {
            if (string.IsNullOrWhiteSpace(request.Id))
                return BadRequest("Id is required for update.");

            await _producer.SendReactionUpdateAsync(request);
            return Accepted("Reaction update request sent.");
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            await _producer.SendReactionDeleteAsync(id);
            return Accepted("Reaction delete request sent.");
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(string id)
        {
            await _producer.SendReactionGetByIdAsync(id);
            return Accepted("Reaction fetch-by-id request sent.");
        }

        [HttpGet]
        public async Task<IActionResult> GetAll([FromQuery] string? country)
        {
            await _producer.SendReactionGetAllAsync(country);
            return Accepted("Reaction get-all request sent.");
        }
    }


}
