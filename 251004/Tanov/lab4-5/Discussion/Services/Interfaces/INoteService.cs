using Discussion.DTO.RequestDTO;
using Discussion.DTO.ResponseDTO;

namespace Discussion.Services.Interfaces;

public interface INoteService
{
    Task<IEnumerable<NoteResponseDTO>> GetMessagesAsync();

    Task<NoteResponseDTO> GetMessageByIdAsync(long id);

    Task<NoteResponseDTO> CreateMessageAsync(NoteRequestDTO message);

    Task<NoteResponseDTO> UpdateMessageAsync(NoteRequestDTO message);

    Task DeleteMessageAsync(long id);
}