using Microsoft.AspNetCore.Mvc;
using Shared.Models.DTOs.Requests;
using Shared.Models.DTOs.Responses;
using Discussion.Services;
using Discussion.Models.Entities;
using Shared.Models.Queries;
using MongoDB.Driver;
using System.Linq.Expressions;
using Microsoft.Extensions.Options;
//using 

namespace Discussion.Controllers
{
    [ApiController]
    [Route("api/v1.0/[controller]")]
    public class ReactionsController : ControllerBase
    {
        private readonly ReactionService _reactionService;

        public ReactionsController(ReactionService reactionService)
        {
            _reactionService = reactionService;
        }

        [HttpPost]
        [ProducesResponseType(typeof(ReactionResponseTo), StatusCodes.Status201Created)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status409Conflict)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public async Task<IActionResult> CreateReaction([FromBody] ReactionRequestTo reactionRequestTo)//, CancellationToken ct = default)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var createdReaction = await _reactionService.CreateReactionAsync(reactionRequestTo);
                return CreatedAtAction(nameof(GetReactionById), new { id = createdReaction.Id }, createdReaction);
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

        [HttpGet("{id}", Name = "GetReactionById")]
        [ProducesResponseType(typeof(ReactionResponseTo), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public async Task<IActionResult> GetReactionById(string id)
        {
            try
            {
                var reaction = await _reactionService.GetReactionByIdAsync(id);
                return reaction != null
                 ? Ok(reaction)
                 : NotFound(new { error = "Not found" }); // 404 - not found, 200 - ok
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
        [ProducesResponseType(typeof(IEnumerable<ReactionResponseTo>), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status500InternalServerError)]
        public async Task<IActionResult> GetAllReactions(
            [FromQuery] string? country,      // Filter by country (partition key)
            [FromQuery] long? newsId,         // Filter by newsId (FK)
            [FromQuery] string? sortBy,       // Sort field (country/newsId/content/id)
            [FromQuery] int? pageNumber,      // Pagination
            [FromQuery] int? pageSize = 20)
        {
            try
            {
                var options = new QueryOptions<Reaction>();
                Expression<Func<Reaction, bool>> baseFilter = r => true;

                // Apply country filter if provided
                if (!string.IsNullOrWhiteSpace(country))
                {
                    Expression<Func<Reaction, bool>> countryFilter = r => r.Country == country.ToLower();
                    baseFilter = CombineExpressions(baseFilter, countryFilter);
                }

                // Apply newsId filter if provided
                if (newsId.HasValue)
                {
                    Expression<Func<Reaction, bool>> newsFilter = r => r.NewsId == newsId.Value;
                    baseFilter = CombineExpressions(baseFilter, newsFilter);
                }

                // Only assign if we have more than just the base filter
                if (baseFilter.Body is not ConstantExpression constant || !(bool)constant.Value!)
                {
                    options.Filter = baseFilter;
                }

                // Rest of your existing code...
                if (!string.IsNullOrWhiteSpace(sortBy))
                {
                    options.OrderBy = sortBy.ToLower() switch
                    {
                        "country" => q => q.OrderBy(r => r.Country),
                        "newsid" => q => q.OrderBy(r => r.NewsId),
                        "content" => q => q.OrderBy(r => r.Content),
                        "id" => q => q.OrderBy(r => r.Id),
                        _ => q => q.OrderBy(r => r.Id)
                    };
                }

                if (pageNumber.HasValue || pageSize.HasValue)
                {
                    options.PageNumber = pageNumber ?? 1;
                    options.PageSize = Math.Min(pageSize ?? 20, 100);
                }

                var reactions = await _reactionService.GetAllReactionsAsync(options);
                return Ok(reactions);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        [HttpPut] //("{id}")]
        [ProducesResponseType(typeof(ReactionResponseTo), StatusCodes.Status200OK)] // success
        [ProducesResponseType(StatusCodes.Status400BadRequest)] // Validation / bad input
        [ProducesResponseType(StatusCodes.Status404NotFound)] // login taken
        [ProducesResponseType(StatusCodes.Status500InternalServerError)] //  any other unexpected
        public async Task<IActionResult> UpdateReaction([FromBody]ReactionRequestTo reactionRequestTo)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                /*if (id != reactionRequestTo.Id)
                {
                    return BadRequest("Route ID and DTO ID must match."); // 400
                }*/
                //reactionRequestTo.Id = id;

                var updatedReaction = await _reactionService.CreateReactionAsync(reactionRequestTo);
                return Ok(updatedReaction);
            }
            catch (Exception ex) when (ex is ArgumentException or
                             ArgumentNullException)
            {
                return BadRequest(ex.Message); // 400 - null/invalid Ids, empty login
            }
            catch (InvalidOperationException ex) when (ex.Message.Contains("not found"))
            {
                return NotFound(ex.Message); // 404
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
        public async Task<IActionResult> DeleteReaction(string id)
        {
            try
            {
                var isDeleted = await _reactionService.DeleteReactionAsync(id);
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
        [ProducesResponseType(typeof(IEnumerable<ReactionResponseTo>), StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public async Task<IActionResult> GetReactionsByNewsId(long newsId)
        {
            try
            {
                var reactions = await _reactionService.GetReactionsByNewsIdAsync(newsId);
                return reactions.Any() ? Ok(reactions) : NotFound();
            }
            catch (ArgumentException ex)
            {
                return BadRequest(ex.Message);
            }
            catch (InvalidOperationException ex)
            {
                return NotFound(ex.Message);
            }
            catch (Exception)
            {
                return StatusCode(500, "Internal server error");
            }
        }

        // Helper method to combine expressions
        private static Expression<Func<T, bool>> CombineExpressions<T>(
            Expression<Func<T, bool>> first,
            Expression<Func<T, bool>> second)
        {
            var parameter = Expression.Parameter(typeof(T));

            var leftVisitor = new ReplaceExpressionVisitor(first.Parameters[0], parameter);
            var left = leftVisitor.Visit(first.Body);

            var rightVisitor = new ReplaceExpressionVisitor(second.Parameters[0], parameter);
            var right = rightVisitor.Visit(second.Body);

            return Expression.Lambda<Func<T, bool>>(
                Expression.AndAlso(left!, right!), parameter);
        }

        // Expression visitor for replacing parameters
        private class ReplaceExpressionVisitor : ExpressionVisitor
        {
            private readonly Expression _oldValue;
            private readonly Expression _newValue;

            public ReplaceExpressionVisitor(Expression oldValue, Expression newValue)
            {
                _oldValue = oldValue;
                _newValue = newValue;
            }

            public override Expression? Visit(Expression? node)
            {
                return node == _oldValue ? _newValue : base.Visit(node);
            }
        }
    }

    
}