using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IMessageService
{
    Task<IEnumerable<NoteResponseDTO>> GetMessagesAsync();

    Task<NoteResponseDTO> GetMessageByIdAsync(long id);

    Task<NoteResponseDTO> CreateMessageAsync(NoteRequestDTO message);

    Task<NoteResponseDTO> UpdateMessageAsync(NoteRequestDTO message);

    Task DeleteMessageAsync(long id);
}