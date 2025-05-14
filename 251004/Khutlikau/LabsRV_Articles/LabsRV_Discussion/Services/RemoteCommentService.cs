using LabsRV_Discussion.Models.DTO;

namespace LabsRV_Discussion.Services
{
    public class RemoteCommentService
    {
        private readonly HttpClient _httpClient;

        public RemoteCommentService(HttpClient httpClient) => _httpClient = httpClient;

        public async Task<CommentResponseDto> CreateAsync(CommentRequestDto request)
        {
            var response = await _httpClient.PostAsJsonAsync("", request);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<CommentResponseDto>();
        }

        public async Task<CommentResponseDto> GetByIdAsync(int id)
        {
            var response = await _httpClient.GetAsync($"{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<CommentResponseDto>();
        }

        public async Task DeleteAsync(int id)
        {
            await _httpClient.DeleteAsync($"{id}");
        }
    }
}
