using LabsRV_Articles.Models.DTO;
using LabsRV_Articles.Services;
using Microsoft.AspNetCore.Mvc;

namespace LabsRV_Articles.Services
{
    public class RemoteCommentService
    {
        private readonly HttpClient _httpClient;

        public RemoteCommentService(HttpClient httpClient) => _httpClient = httpClient;

        // Создание комментария
        public async Task<CommentResponseDto> CreateAsync(CommentRequestDto request)
        {
            var response = await _httpClient.PostAsJsonAsync("", request);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<CommentResponseDto>();
        }

        // Получение всех комментариев
        public async Task<IEnumerable<CommentResponseDto>> GetAllAsync()
        {
            var response = await _httpClient.GetAsync("");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<IEnumerable<CommentResponseDto>>();
        }

        public async Task<CommentResponseDto> UpdateAsync(int id, CommentRequestDto request)
        {
            var response = await _httpClient.PutAsJsonAsync($"{id}", request);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<CommentResponseDto>();
        }

        public async Task<CommentResponseDto> GetByIdAsync(int id)
        {
            var response = await _httpClient.GetAsync($"{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<CommentResponseDto>();
        }

        // Удаление комментария
        public async Task DeleteAsync(int id)
        {
            var response = await _httpClient.DeleteAsync($"{id}");
            response.EnsureSuccessStatusCode();
        }
    }
}
